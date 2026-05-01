package com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO;

import com.chicos_ingenieros.zenkai.Users.Domain.Role;

public record UserDTO(Long user_id, String first_name, String last_name, String email, Role role) {
}
