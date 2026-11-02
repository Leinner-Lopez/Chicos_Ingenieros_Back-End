package com.chicos_ingenieros.zenkai.BackUp.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Backup {
    private Long backupId;
    private String fileName;
    private LocalDateTime createdAt;
    private String createdBy;
    private Long sizeBytes;
    private Integer recordCount;
    private String checksum;
    private BackupType type;
}
