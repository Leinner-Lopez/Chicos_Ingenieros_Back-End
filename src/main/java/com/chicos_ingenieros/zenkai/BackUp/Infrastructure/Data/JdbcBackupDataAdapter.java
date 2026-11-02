package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Data;

import com.chicos_ingenieros.zenkai.BackUp.Domain.BackupDataPort;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Dumps and reloads the business tables as a JSON document. Rows are read and written with plain SQL so the
 * original primary keys are preserved (JPA would generate new ids and break the foreign keys).
 * The {@code backup} history table is deliberately not part of the snapshot.
 */
@Component
@RequiredArgsConstructor
public class JdbcBackupDataAdapter implements BackupDataPort {

    private static final int FORMAT_VERSION = 1;
    private static final int BATCH_SIZE = 500;

    /** Parents first: the order used to insert; the reverse is used to delete. */
    private static final List<String> TABLES = List.of("category", "product", "lot", "user", "sale", "sale_details");

    private static final String USER_TABLE = "user";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String USER_EMAIL_COLUMN = "email";
    private static final String SALE_TABLE = "sale";

    private final JdbcTemplate jdbc;
    private final ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    @Override
    @Transactional(readOnly = true)
    public int exportData(OutputStream out) {
        int total = 0;
        try (JsonGenerator generator = mapper.getFactory().createGenerator(out)) {
            generator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
            generator.writeStartObject();
            generator.writeNumberField("formatVersion", FORMAT_VERSION);
            generator.writeStringField("createdAt", LocalDateTime.now().toString());
            generator.writeObjectFieldStart("tables");
            for (String table : TABLES) {
                generator.writeArrayFieldStart(table);
                total += writeTable(generator, table);
                generator.writeEndArray();
            }
            generator.writeEndObject();
            generator.writeEndObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return total;
    }

    @Override
    @Transactional
    public void replaceData(InputStream in, String keepUserEmail) {
        Long keepUserId = findKeepUserId(keepUserEmail);
        deleteAll(keepUserId);
        try (JsonParser parser = mapper.getFactory().createParser(in)) {
            readBackup(parser, keepUserId, keepUserEmail);
        } catch (IOException e) {
            throw new IlegalActionException("Backup file is not valid: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void purgeData(String keepUserEmail) {
        deleteAll(findKeepUserId(keepUserEmail));
    }

    // ---------- export ----------

    private int writeTable(JsonGenerator generator, String table) {
        int[] count = {0};
        jdbc.query("SELECT * FROM `" + table + "`", (RowCallbackHandler) rs -> {
            try {
                writeRow(generator, rs);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            count[0]++;
        });
        return count[0];
    }

    private void writeRow(JsonGenerator generator, ResultSet rs) throws SQLException, IOException {
        ResultSetMetaData meta = rs.getMetaData();
        generator.writeStartObject();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            generator.writeFieldName(meta.getColumnLabel(i));
            writeValue(generator, readValue(rs, i, meta.getColumnType(i)));
        }
        generator.writeEndObject();
    }

    private Object readValue(ResultSet rs, int index, int sqlType) throws SQLException {
        return switch (sqlType) {
            case Types.DATE, Types.TIME, Types.TIMESTAMP, Types.TIME_WITH_TIMEZONE, Types.TIMESTAMP_WITH_TIMEZONE ->
                    rs.getString(index);
            case Types.DECIMAL, Types.NUMERIC -> rs.getBigDecimal(index);
            default -> rs.getObject(index);
        };
    }

    private void writeValue(JsonGenerator generator, Object value) throws IOException {
        switch (value) {
            case null -> generator.writeNull();
            case Boolean b -> generator.writeBoolean(b);
            case BigDecimal d -> generator.writeNumber(d);
            case BigInteger i -> generator.writeNumber(i);
            case Double d -> generator.writeNumber(d);
            case Float f -> generator.writeNumber(f);
            case Number n -> generator.writeNumber(n.longValue());
            default -> generator.writeString(value.toString());
        }
    }

    // ---------- delete ----------

    private Long findKeepUserId(String keepUserEmail) {
        List<Long> ids = jdbc.queryForList("SELECT " + USER_ID_COLUMN + " FROM `" + USER_TABLE + "` WHERE "
                + USER_EMAIL_COLUMN + " = ?", Long.class, keepUserEmail);
        if (ids.isEmpty()) {
            throw new IlegalActionException("Administrator user " + keepUserEmail + " not found");
        }
        return ids.getFirst();
    }

    private void deleteAll(Long keepUserId) {
        for (String table : TABLES.reversed()) {
            if (USER_TABLE.equals(table)) {
                jdbc.update("DELETE FROM `" + table + "` WHERE " + USER_ID_COLUMN + " <> ?", keepUserId);
            } else {
                jdbc.update("DELETE FROM `" + table + "`");
            }
        }
    }

    // ---------- restore ----------

    private void readBackup(JsonParser parser, Long keepUserId, String keepUserEmail) throws IOException {
        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new IlegalActionException("Backup file is not valid: JSON object expected");
        }
        int version = -1;
        boolean tablesRead = false;
        while (parser.nextToken() == JsonToken.FIELD_NAME) {
            String field = parser.currentName();
            parser.nextToken();
            if ("formatVersion".equals(field)) {
                version = parser.getIntValue();
            } else if ("tables".equals(field)) {
                if (version != FORMAT_VERSION) {
                    throw new IlegalActionException("Unsupported backup format version: " + version);
                }
                readTables(parser, keepUserId, keepUserEmail);
                tablesRead = true;
            } else {
                parser.skipChildren();
            }
        }
        if (!tablesRead) {
            throw new IlegalActionException("Backup file is not valid: no tables found");
        }
    }

    private void readTables(JsonParser parser, Long keepUserId, String keepUserEmail) throws IOException {
        // Backup user ids that map to the preserved administrator (same id or same email)
        Map<Long, Long> userRemap = new HashMap<>();
        while (parser.nextToken() == JsonToken.FIELD_NAME) {
            String table = parser.currentName();
            if (!TABLES.contains(table)) {
                throw new IlegalActionException("Backup file is not valid: unknown table " + table);
            }
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IlegalActionException("Backup file is not valid: array expected for table " + table);
            }
            restoreTable(parser, table, keepUserId, keepUserEmail, userRemap);
        }
    }

    private void restoreTable(JsonParser parser, String table, Long keepUserId, String keepUserEmail,
                              Map<Long, Long> userRemap) throws IOException {
        Set<String> allowedColumns = columnsOf(table);
        List<String> columns = null;
        String sql = null;
        List<Object[]> batch = new ArrayList<>();

        while (parser.nextToken() == JsonToken.START_OBJECT) {
            JsonNode row = mapper.readTree(parser);

            if (USER_TABLE.equals(table) && isPreservedAdmin(row, keepUserId, keepUserEmail)) {
                userRemap.put(row.path(USER_ID_COLUMN).asLong(), keepUserId);
                continue;
            }

            if (columns == null) {
                columns = new ArrayList<>();
                for (String column : (Iterable<String>) row::fieldNames) {
                    if (!allowedColumns.contains(column)) {
                        throw new IlegalActionException("Backup file is not valid: unknown column " + table + "." + column);
                    }
                    columns.add(column);
                }
                sql = buildInsert(table, columns);
            }

            Object[] values = new Object[columns.size()];
            for (int i = 0; i < values.length; i++) {
                Object value = toJavaValue(row.get(columns.get(i)));
                if (SALE_TABLE.equals(table) && USER_ID_COLUMN.equals(columns.get(i)) && value instanceof Long userId) {
                    value = userRemap.getOrDefault(userId, userId);
                }
                values[i] = value;
            }
            batch.add(values);

            if (batch.size() >= BATCH_SIZE) {
                jdbc.batchUpdate(sql, batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            jdbc.batchUpdate(sql, batch);
        }
    }

    private boolean isPreservedAdmin(JsonNode row, Long keepUserId, String keepUserEmail) {
        return row.path(USER_ID_COLUMN).asLong() == keepUserId
                || keepUserEmail.equalsIgnoreCase(row.path(USER_EMAIL_COLUMN).asText(null));
    }

    private Set<String> columnsOf(String table) {
        return jdbc.query("SELECT * FROM `" + table + "` LIMIT 0", rs -> {
            Set<String> columns = new HashSet<>();
            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                columns.add(meta.getColumnLabel(i));
            }
            return columns;
        });
    }

    private String buildInsert(String table, List<String> columns) {
        StringJoiner names = new StringJoiner("`, `", "`", "`");
        StringJoiner marks = new StringJoiner(", ");
        for (String column : columns) {
            names.add(column);
            marks.add("?");
        }
        return "INSERT INTO `" + table + "` (" + names + ") VALUES (" + marks + ")";
    }

    private Object toJavaValue(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        if (node.isIntegralNumber()) {
            return node.canConvertToLong() ? (Object) node.longValue() : node.bigIntegerValue();
        }
        if (node.isNumber()) {
            return node.decimalValue();
        }
        if (node.isBoolean()) {
            return node.booleanValue();
        }
        return node.asText();
    }
}
