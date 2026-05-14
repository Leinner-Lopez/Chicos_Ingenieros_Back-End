package com.chicos_ingenieros.zenkai.Auth.Infrastructure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String documentNumber;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
}
