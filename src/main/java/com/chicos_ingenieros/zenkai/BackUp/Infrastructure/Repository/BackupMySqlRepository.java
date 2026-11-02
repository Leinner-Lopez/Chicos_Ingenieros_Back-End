package com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.BackUp.Domain.Backup;
import com.chicos_ingenieros.zenkai.BackUp.Domain.BackupRepository;
import com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Entity.BackupEntity;
import com.chicos_ingenieros.zenkai.BackUp.Infrastructure.Mapper.BackupMapper;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BackupMySqlRepository implements BackupRepository {

    private final SpringBackupRepository repository;
    private final BackupMapper mapper;

    @Override
    public Backup save(Backup backup) {
        BackupEntity saved = repository.save(mapper.backupToBackupEntity(backup));
        return mapper.backupEntityToBackup(saved);
    }

    @Override
    public Backup findById(Long id) {
        return repository.findById(id).map(mapper::backupEntityToBackup).orElseThrow(() ->
                new ResourceNotFoundException("Backup with id " + id + " not found")
        );
    }

    @Override
    public List<Backup> findAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream().map(mapper::backupEntityToBackup).toList();
    }
}
