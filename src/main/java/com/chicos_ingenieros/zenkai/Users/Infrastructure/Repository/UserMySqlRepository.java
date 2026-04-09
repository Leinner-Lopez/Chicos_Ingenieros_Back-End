package com.chicos_ingenieros.zenkai.Users.Infrastructure.Repository;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserRepository;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserMySqlRepository implements UserRepository {

    private final SpringUserRepository repository;
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        UserEntity saved = repository.save(mapper.userToUserEntity(user));
        return mapper.userEntityToUser(saved);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.userEntityToUser(repository.findByEmail(email));
    }

    @Override
    public User findByDocumentNumber(String documento) {
        return mapper.userEntityToUser(repository.findByDocumentNumber(documento));
    }

    @Override
    public User findById(Long id) {
        return repository.findById(id).map(mapper::userEntityToUser).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::userEntityToUser).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
