package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Entity;

import com.chicos_ingenieros.zenkai.BackUp.Domain.BackupType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "backup")
public class BackupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backupId;

    private String fileName;
    private LocalDateTime createdAt;
    private String createdBy;
    private Long sizeBytes;
    private Integer recordCount;
    private String checksum;

    @Enumerated(EnumType.STRING)
    private BackupType type;
}
