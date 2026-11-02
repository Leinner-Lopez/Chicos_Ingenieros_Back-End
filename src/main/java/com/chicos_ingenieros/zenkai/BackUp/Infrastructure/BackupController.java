package com.chicos_ingenieros.zenkai.BackUp.Infrastructure;

import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupCreateUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupHistoryUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupPurgeUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Application.UseCases.BackupRestoreUseCase;
import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;
import com.chicos_ingenieros.zenkai.BackUp.Infrastructure.DTO.BackupOperationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/backups")
public class BackupController {

    private final BackupCreateUseCase createUseCase;
    private final BackupHistoryUseCase historyUseCase;
    private final BackupRestoreUseCase restoreUseCase;
    private final BackupPurgeUseCase purgeUseCase;

    @PostMapping
    public ResponseEntity<Backup> create(Principal principal) {
        Backup backup = createUseCase.createBackup(principal.getName());
        return new ResponseEntity<>(backup, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Backup>> getHistory() {
        return new ResponseEntity<>(historyUseCase.findAllBackups(), HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        Backup backup = historyUseCase.findBackupById(id);
        Resource file = new InputStreamResource(historyUseCase.openBackupFile(id));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(backup.getSizeBytes())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(backup.getFileName()).build().toString())
                .body(file);
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<BackupOperationResponse> restore(@PathVariable Long id, Principal principal) {
        Backup safetyBackup = restoreUseCase.restoreBackup(id, principal.getName());
        return new ResponseEntity<>(new BackupOperationResponse("Backup restored successfully", safetyBackup), HttpStatus.OK);
    }

    @PostMapping("/purge")
    public ResponseEntity<BackupOperationResponse> purge(Principal principal) {
        Backup safetyBackup = purgeUseCase.purgeDatabase(principal.getName());
        return new ResponseEntity<>(new BackupOperationResponse("Database emptied successfully", safetyBackup), HttpStatus.OK);
    }
}
