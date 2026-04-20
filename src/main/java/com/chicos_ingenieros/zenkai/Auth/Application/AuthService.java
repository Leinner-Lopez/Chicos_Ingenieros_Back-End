package com.chicos_ingenieros.zenkai.Auth.Application;

import com.chicos_ingenieros.zenkai.Auth.Infrastructure.AuthResponse;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.LoginRequest;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.RegisterRequest;
import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Users.Application.UserService;
import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Entity.UserEntity;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userMapper.userToUserEntity(userService.findUserByEmail(request.getUsername()));
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token)
                .email(user.getUsername())
                .userId(userService.findUserByEmail(user.getUsername()).getUser_id())
                .build();
    }

    public AuthResponse register(RegisterRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .documentNumber(request.getDocument_number())
                .phone_number(request.getPhone_number())
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .password(request.getPassword())
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        User userDB = userService.saveUser(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(userMapper.userToUserEntity(user)))
                .email(userDB.getEmail())
                .userId(userDB.getUser_id())
                .build();
    }
}
