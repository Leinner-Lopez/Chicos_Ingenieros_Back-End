package com.chicos_ingenieros.zenkai.Auth.Infrastructure;

import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    String email;
    Long userId;
    String token;
    String role;
}
