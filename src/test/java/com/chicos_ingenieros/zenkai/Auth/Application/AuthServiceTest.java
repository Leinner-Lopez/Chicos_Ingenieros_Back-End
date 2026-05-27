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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;
    @Mock private AuthenticationManager authenticationManager;
    @Mock
    private UserEntity userDetails;

    @InjectMocks
    private AuthService service;

    // ─── Fixtures ────────────────────────────────────────────────

    User domainUser = User.builder()
            .userId(1L)
            .firstName("Carlos").lastName("Pérez")
            .email("carlos@zenkai.com")
            .documentNumber("123456789")
            .phoneNumber("3001234567")
            .password("encodedPassword")
            .role(Role.CUSTOMER)
            .status(UserStatus.ACTIVE)
            .build();

    // ─── login ───────────────────────────────────────────────────

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest("carlos@zenkai.com", "plainPassword");
        when(userDetails.getUsername()).thenReturn("carlos@zenkai.com");
        when(userDetails.getAuthorities()).thenAnswer(inv ->
                List.of(new SimpleGrantedAuthority("CUSTOMER")));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null); // authenticate() no retorna nada útil, solo valida
        when(userService.findUserByEmail("carlos@zenkai.com")).thenReturn(domainUser);
        when(userMapper.userToUserEntity(domainUser)).thenReturn(userDetails);
        when(jwtService.getToken(userDetails)).thenReturn("mocked.jwt.token");

        AuthResponse result = service.login(request);

        assertNotNull(result);
        assertEquals("mocked.jwt.token", result.getToken());
        assertEquals("carlos@zenkai.com", result.getEmail());
        assertEquals(1L, result.getUserId());
        assertEquals("CUSTOMER", result.getRole());
        // findUserByEmail se llama 2 veces: una para obtener UserDetails, otra para el userId
        verify(userService, times(2)).findUserByEmail("carlos@zenkai.com");
    }

    @Test
    void login_badCredentials_throwsException() {
        LoginRequest request = new LoginRequest("carlos@zenkai.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> service.login(request));
        verify(userService, never()).findUserByEmail(any());
        verify(jwtService, never()).getToken(any());
    }

    // ─── register ────────────────────────────────────────────────

    @Test
    void register_success() {
        RegisterRequest request = RegisterRequest.builder()
                .email("carlos@zenkai.com")
                .documentNumber("123456789")
                .phoneNumber("3001234567")
                .firstName("Carlos")
                .lastName("Pérez")
                .password("plainPassword")
                .build();

        when(userService.saveUser(any(User.class))).thenReturn(domainUser);
        when(userMapper.userToUserEntity(any(User.class))).thenReturn(userDetails);
        when(jwtService.getToken(userDetails)).thenReturn("mocked.jwt.token");

        AuthResponse result = service.register(request);

        assertNotNull(result);
        assertEquals("mocked.jwt.token", result.getToken());
        assertEquals("carlos@zenkai.com", result.getEmail());
        assertEquals(1L, result.getUserId());
        assertEquals("CUSTOMER", result.getRole());
        verify(userService, times(1)).saveUser(any(User.class));
        verify(jwtService, times(1)).getToken(any());
    }

    @Test
    void register_duplicateUser_throwsException() {
        RegisterRequest request = RegisterRequest.builder()
                .email("carlos@zenkai.com")
                .documentNumber("123456789")
                .password("plainPassword")
                .build();

        when(userService.saveUser(any(User.class)))
                .thenThrow(new com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException(
                        "User already exists"));

        assertThrows(
                com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException.class,
                () -> service.register(request)
        );
        verify(jwtService, never()).getToken(any());
    }
}