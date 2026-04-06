package com.chicos_ingenieros.zenkai.Users.Application;

import com.chicos_ingenieros.zenkai.Products.Domain.Product;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;

    public User saveUser(User user) {
        return repository.save(user);
    }

    public User findUserById(Long id) {
        return repository.findById(id);
    }

    public User findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User updateUser(Long id, User user) {
        User UserDB = repository.findById(id);
        UserDB.setFirst_name(user.getFirst_name());
        UserDB.setLast_name(user.getLast_name());
        UserDB.setEmail(user.getEmail());
        UserDB.setPassword(user.getPassword());
        UserDB.setStatus(user.getStatus());
        return repository.save(UserDB);
    }

    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
