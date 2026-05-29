package com.chicos_ingenieros.zenkai.Users.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByDocumentNumber(String documento);
    List<UserEntity> findByStatus(UserStatus status);
    Long countByStatus(UserStatus status);
}
