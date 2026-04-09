package com.chicos_ingenieros.zenkai.Users.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByDocumentNumber(String documento);
}
