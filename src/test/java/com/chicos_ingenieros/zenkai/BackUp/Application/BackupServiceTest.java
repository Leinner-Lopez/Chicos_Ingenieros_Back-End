package com.chicos_ingenieros.zenkai.BackUp.Application;

import com.chicos_ingenieros.zenkai.BackUp.Domain.*;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackupServiceTest {

    private static final String ADMIN = "admin@zenkai.com";

    @Mock
    private BackupRepository repository;

    @Mock
    private BackupStoragePort storage;

    @Mock
    private BackupDataPort dataPort;

    @InjectMocks
    private BackupService service;

    private Backup stored;

    @BeforeEach
    void setUp() {
        stored = Backup.builder().backupId(1L).fileName("backup-manual-x.json.gz").checksum("abc")
                .type(BackupType.MANUAL).build();
    }

    private void stubBackupGeneration() {
        when(storage.openForWrite(anyString())).thenReturn(new ByteArrayOutputStream());
        when(dataPort.exportData(any(OutputStream.class))).thenReturn(42);
        when(storage.size(anyString())).thenReturn(1234L);
        when(storage.sha256(anyString())).thenReturn("hash");
        when(repository.save(any(Backup.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private static byte[] gzip() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (GZIPOutputStream gz = new GZIPOutputStream(bytes)) {
            gz.write("{}".getBytes());
        }
        return bytes.toByteArray();
    }

    @Test
    void createBackup_ShouldPersistHistoryEntry() {
        stubBackupGeneration();

        Backup result = service.createBackup(ADMIN);

        assertEquals(BackupType.MANUAL, result.getType());
        assertEquals(ADMIN, result.getCreatedBy());
        assertEquals(42, result.getRecordCount());
        assertEquals(1234L, result.getSizeBytes());
        assertEquals("hash", result.getChecksum());
        assertTrue(result.getFileName().startsWith("backup-manual-") && result.getFileName().endsWith(".json.gz"));
    }

    @Test
    void createBackup_WhenExportFails_ShouldDeleteFileAndNotSaveHistory() {
        when(storage.openForWrite(anyString())).thenReturn(new ByteArrayOutputStream());
        when(dataPort.exportData(any(OutputStream.class))).thenThrow(new IllegalStateException("db down"));

        assertThrows(IllegalStateException.class, () -> service.createBackup(ADMIN));

        verify(storage).delete(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    void findAllBackups_ShouldReturnHistory() {
        when(repository.findAll()).thenReturn(List.of(stored));

        assertEquals(1, service.findAllBackups().size());
    }

    @Test
    void restoreBackup_ShouldCreateSafetyBackupBeforeReplacingData() throws Exception {
        stubBackupGeneration();
        when(repository.findById(1L)).thenReturn(stored);
        when(storage.sha256("backup-manual-x.json.gz")).thenReturn("abc");
        when(storage.openForRead("backup-manual-x.json.gz")).thenReturn(new ByteArrayInputStream(gzip()));

        Backup safety = service.restoreBackup(1L, ADMIN);

        assertEquals(BackupType.PRE_RESTORE, safety.getType());
        InOrder order = inOrder(dataPort);
        order.verify(dataPort).exportData(any(OutputStream.class));
        order.verify(dataPort).replaceData(any(), eq(ADMIN));
    }

    @Test
    void restoreBackup_WhenChecksumDiffers_ShouldFailWithoutTouchingData() {
        when(repository.findById(1L)).thenReturn(stored);
        when(storage.sha256("backup-manual-x.json.gz")).thenReturn("tampered");

        assertThrows(IlegalActionException.class, () -> service.restoreBackup(1L, ADMIN));

        verifyNoInteractions(dataPort);
        verify(repository, never()).save(any());
    }

    @Test
    void restoreBackup_WhenBackupDoesNotExist_ShouldThrowNotFound() {
        when(repository.findById(9L)).thenThrow(new ResourceNotFoundException("Backup with id 9 not found"));

        assertThrows(ResourceNotFoundException.class, () -> service.restoreBackup(9L, ADMIN));

        verifyNoInteractions(dataPort);
    }

    @Test
    void purgeDatabase_ShouldCreateSafetyBackupAndThenPurge() {
        stubBackupGeneration();
        ArgumentCaptor<Backup> saved = ArgumentCaptor.forClass(Backup.class);

        Backup safety = service.purgeDatabase(ADMIN);

        assertEquals(BackupType.PRE_PURGE, safety.getType());
        InOrder order = inOrder(dataPort, repository);
        order.verify(dataPort).exportData(any(OutputStream.class));
        order.verify(repository).save(saved.capture());
        order.verify(dataPort).purgeData(ADMIN);
    }
}
