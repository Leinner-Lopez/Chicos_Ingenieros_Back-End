package com.chicos_ingenieros.zenkai.BackUp.Application;

import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupCreateUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupHistoryUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupPurgeUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupRestoreUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Domain.*;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class BackupService implements BackupCreateUseCase, BackupHistoryUseCase, BackupRestoreUseCase, BackupPurgeUseCase {

    private static final DateTimeFormatter FILE_TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private final BackupRepository repository;
    private final BackupStoragePort storage;
    private final BackupDataPort dataPort;

    @Override
    public Backup createBackup(String createdBy) {
        return generateBackup(BackupType.MANUAL, createdBy);
    }

    @Override
    public List<Backup> findAllBackups() {
        return repository.findAll();
    }

    @Override
    public Backup findBackupById(Long id) {
        return repository.findById(id);
    }

    @Override
    public InputStream openBackupFile(Long id) {
        return storage.openForRead(repository.findById(id).getFileName());
    }

    @Override
    public Backup restoreBackup(Long id, String adminEmail) {
        Backup backup = repository.findById(id);
        verifyIntegrity(backup);

        Backup safetyBackup = generateBackup(BackupType.PRE_RESTORE, adminEmail);
        try (InputStream in = new GZIPInputStream(storage.openForRead(backup.getFileName()))) {
            dataPort.replaceData(in, adminEmail);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        log.info("Backup {} restored by {}; safety backup {}", backup.getFileName(), adminEmail, safetyBackup.getFileName());
        return safetyBackup;
    }

    @Override
    public Backup purgeDatabase(String adminEmail) {
        Backup safetyBackup = generateBackup(BackupType.PRE_PURGE, adminEmail);
        dataPort.purgeData(adminEmail);
        log.info("Database purged by {}; safety backup {}", adminEmail, safetyBackup.getFileName());
        return safetyBackup;
    }

    private Backup generateBackup(BackupType type, String createdBy) {
        LocalDateTime now = LocalDateTime.now();
        String fileName = "backup-" + type.name().toLowerCase().replace('_', '-') + "-" + now.format(FILE_TIMESTAMP) + ".json.gz";

        int records;
        try (OutputStream out = new GZIPOutputStream(storage.openForWrite(fileName))) {
            records = dataPort.exportData(out);
        } catch (IOException | RuntimeException e) {
            storage.delete(fileName);
            if (e instanceof IOException io) {
                throw new UncheckedIOException(io);
            }
            throw (RuntimeException) e;
        }

        return repository.save(Backup.builder()
                .fileName(fileName)
                .createdAt(now)
                .createdBy(createdBy)
                .sizeBytes(storage.size(fileName))
                .recordCount(records)
                .checksum(storage.sha256(fileName))
                .type(type)
                .build());
    }

    private void verifyIntegrity(Backup backup) {
        if (!backup.getChecksum().equals(storage.sha256(backup.getFileName()))) {
            throw new IlegalActionException("Backup file is corrupted or has been modified");
        }
    }
}
