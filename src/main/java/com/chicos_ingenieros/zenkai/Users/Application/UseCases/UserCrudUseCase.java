package com.chicos_ingenieros.zenkai.Users.Application.UseCases;

import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;

import java.util.List;

public interface UserCrudUseCase {
    User saveUser(User user);
    User findUserById(Long id);
    User findUserByEmail(String email);
    List<UserDTO> findAllUsers();
    List<UserDTO> findUsersByStatus(UserStatus status);
    Long countUsersByStatus(UserStatus status);
    User updateUser(Long id, User user);
    User toggleUserStatus(Long id);
    void deleteUserById(Long id);
}
