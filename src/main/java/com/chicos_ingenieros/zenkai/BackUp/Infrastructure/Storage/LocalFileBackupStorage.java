package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Storage;

import com.chicos_ingenieros.zenkai.BackUp.Domain.BackupStoragePort;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class LocalFileBackupStorage implements BackupStoragePort {

    private final Path root;

    public LocalFileBackupStorage(@Value("${backup.storage-path:./backups}") String storagePath) {
        this.root = Path.of(storagePath).toAbsolutePath().normalize();
    }

    @Override
    public OutputStream openForWrite(String fileName) {
        try {
            Files.createDirectories(root);
            return Files.newOutputStream(resolve(fileName), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InputStream openForRead(String fileName) {
        Path file = resolve(fileName);
        if (!Files.isRegularFile(file)) {
            throw new ResourceNotFoundException("Backup file " + fileName + " not found in storage");
        }
        try {
            return Files.newInputStream(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long size(String fileName) {
        try {
            return Files.size(resolve(fileName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String sha256(String fileName) {
        try (InputStream in = openForRead(fileName)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                digest.update(buffer, 0, read);
            }
            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            Files.deleteIfExists(resolve(fileName));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Path resolve(String fileName) {
        Path file = root.resolve(fileName).normalize();
        if (!file.startsWith(root)) {
            throw new IlegalActionException("Invalid backup file name");
        }
        return file;
    }
}
