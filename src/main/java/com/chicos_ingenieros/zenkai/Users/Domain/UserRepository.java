package com.chicos_ingenieros.zenkai.Users.Domain;


import java.util.List;

public interface UserRepository {

    User save(User product);

    User findByEmail(String email);

    User findByDocumentNumber(String documento);

    User findById(Long id);

    List<User> findAll();

    List<User> findByStatus(UserStatus status);

    Long countByStatus(UserStatus status);

    void deleteById(Long id);

    Long countUsers();
}
