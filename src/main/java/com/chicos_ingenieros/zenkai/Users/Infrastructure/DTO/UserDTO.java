package com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO;

import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;

public record UserDTO(Long userId, String firstName, String lastName, String email, Role role, UserStatus status) {
}
