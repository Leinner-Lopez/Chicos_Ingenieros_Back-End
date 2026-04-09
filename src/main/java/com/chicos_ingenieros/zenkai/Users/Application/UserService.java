package com.chicos_ingenieros.zenkai.Users.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User saveUser(User user) {
        User userDB = repository.findByEmail(user.getEmail());
        if(userDB != null) {
            throw new ResourceDuplicateException("User already exists in the system");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public User findUserById(Long id) {
        User user = repository.findById(id);
        if(user == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return user;
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
        UserDB.setPhone_number(user.getPhone_number());
        UserDB.setStatus(user.getStatus());
        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            UserDB.setPassword(encoder.encode(user.getPassword()));
        }
        return repository.save(UserDB);
    }

    public void deleteUserById(Long id) {
        User UserDB = repository.findById(id);
        if(UserDB == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        repository.deleteById(id);
    }
}
