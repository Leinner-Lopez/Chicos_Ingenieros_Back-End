package com.chicos_ingenieros.zenkai.BackUp.Infrastructure;

import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupCreateUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupHistoryUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupPurgeUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupRestoreUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;
import com.chicos_ingenieros.zenkai.BackUp.Domain.BackupType;
import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BackupController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class BackupControllerTest {

    private static final Principal ADMIN = () -> "admin@zenkai.com";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BackupCreateUseCase createUseCase;

    @MockitoBean
    private BackupHistoryUseCase historyUseCase;

    @MockitoBean
    private BackupRestoreUseCase restoreUseCase;

    @MockitoBean
    private BackupPurgeUseCase purgeUseCase;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private Backup sample;

    @BeforeEach
    void setUp() {
        sample = Backup.builder().backupId(1L).fileName("backup-manual-x.json.gz").sizeBytes(5L)
                .type(BackupType.MANUAL).createdBy("admin@zenkai.com").build();
    }

    @Test
    void create_ShouldReturnCreatedBackup() throws Exception {
        when(createUseCase.createBackup("admin@zenkai.com")).thenReturn(sample);

        mockMvc.perform(post("/api/backups").principal(ADMIN))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.backupId").value(1L))
                .andExpect(jsonPath("$.type").value("MANUAL"));
    }

    @Test
    void getHistory_ShouldReturnList() throws Exception {
        when(historyUseCase.findAllBackups()).thenReturn(List.of(sample));

        mockMvc.perform(get("/api/backups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].fileName").value("backup-manual-x.json.gz"));
    }

    @Test
    void download_ShouldReturnFileAsAttachment() throws Exception {
        when(historyUseCase.findBackupById(1L)).thenReturn(sample);
        when(historyUseCase.openBackupFile(1L)).thenReturn(new ByteArrayInputStream("12345".getBytes()));

        mockMvc.perform(get("/api/backups/{id}/download", 1L))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", org.hamcrest.Matchers.containsString("backup-manual-x.json.gz")))
                .andExpect(content().bytes("12345".getBytes()));
    }

    @Test
    void restore_ShouldReturnSafetyBackup() throws Exception {
        Backup safety = Backup.builder().backupId(2L).type(BackupType.PRE_RESTORE).build();
        when(restoreUseCase.restoreBackup(1L, "admin@zenkai.com")).thenReturn(safety);

        mockMvc.perform(post("/api/backups/{id}/restore", 1L).principal(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.safetyBackup.type").value("PRE_RESTORE"));
    }

    @Test
    void purge_ShouldReturnSafetyBackup() throws Exception {
        Backup safety = Backup.builder().backupId(3L).type(BackupType.PRE_PURGE).build();
        when(purgeUseCase.purgeDatabase("admin@zenkai.com")).thenReturn(safety);

        mockMvc.perform(post("/api/backups/purge").principal(ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.safetyBackup.type").value("PRE_PURGE"));
    }
}
