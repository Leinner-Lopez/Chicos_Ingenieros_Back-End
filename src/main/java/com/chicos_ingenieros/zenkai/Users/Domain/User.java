package com.chicos_ingenieros.zenkai.Users.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long user_id;
    private String documentNumber;
    private String first_name;
    private String last_name;
    private String email;
    private String phone_number;
    private String password;
    private Role role;
    private UserStatus status;
}
