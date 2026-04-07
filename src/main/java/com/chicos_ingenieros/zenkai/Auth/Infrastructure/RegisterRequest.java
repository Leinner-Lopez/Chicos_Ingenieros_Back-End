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
    private String document_number;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private String password;
}
