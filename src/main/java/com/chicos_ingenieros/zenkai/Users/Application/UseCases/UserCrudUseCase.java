package com.chicos_ingenieros.zenkai.Users.Application.UseCases;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;

import java.util.List;

public interface UserCrudUseCase {
    User saveUser(User user);
    User findUserById(Long id);
    User findUserByEmail(String email);
    List<UserDTO> findAllUsers();
    User updateUser(Long id, User user);
    void deleteUserById(Long id);
}
