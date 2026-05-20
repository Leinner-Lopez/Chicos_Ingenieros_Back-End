package com.chicos_ingenieros.zenkai.Users.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Users.Application.UseCases.UserCountUseCase;
import com.chicos_ingenieros.zenkai.Users.Application.UseCases.UserCrudUseCase;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserRepository;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserCrudUseCase, UserCountUseCase {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final UserDTOMapper mapper;

    @Override
    public User saveUser(User user) {
        User userDB = repository.findByEmail(user.getEmail());
        User userDB2 = repository.findByDocumentNumber(user.getDocumentNumber());
        if(userDB != null || userDB2 != null) {
            throw new ResourceDuplicateException("User already exists in the system");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        User user = repository.findById(id);
        if(user == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User userDB = repository.findByEmail(email);
        if(userDB == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        return userDB;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> findAllUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::userToUserDTO)
                .toList();
    }

    @Override
    public User updateUser(Long id, User user) {
        User userDb = repository.findById(id);
        userDb.setFirstName(user.getFirstName());
        userDb.setLastName(user.getLastName());
        userDb.setEmail(user.getEmail());
        userDb.setPhoneNumber(user.getPhoneNumber());
        userDb.setRole(user.getRole());
        userDb.setStatus(user.getStatus());
        if(user.getPassword() != null && !user.getPassword().isEmpty()){
            userDb.setPassword(encoder.encode(user.getPassword()));
        }
        return repository.save(userDb);
    }

    @Override
    public void deleteUserById(Long id) {
        User userDb = repository.findById(id);
        if(userDb == null){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
        userDb.setStatus(UserStatus.INACTIVE);
        repository.save(userDb);
    }

    @Override
    public Long countUsers() {
        return repository.countUsers();
    }
}
