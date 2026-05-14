package com.chicos_ingenieros.zenkai.Auth.Application;

import com.chicos_ingenieros.zenkai.Auth.Infrastructure.AuthResponse;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.LoginRequest;
import com.chicos_ingenieros.zenkai.Auth.Infrastructure.RegisterRequest;
import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Users.Application.UserService;
import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUserCase {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userMapper.userToUserEntity(userService.findUserByEmail(request.getUsername()));
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token)
                .email(user.getUsername())
                .userId(userService.findUserByEmail(user.getUsername()).getUserId())
                .role(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .documentNumber(request.getDocumentNumber())
                .phoneNumber(request.getPhoneNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(request.getPassword())
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        User userDB = userService.saveUser(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(userMapper.userToUserEntity(user)))
                .email(userDB.getEmail())
                .userId(userDB.getUserId())
                .role(userDB.getRole().name())
                .build();
    }
}
