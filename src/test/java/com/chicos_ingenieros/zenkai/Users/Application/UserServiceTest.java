package com.chicos_ingenieros.zenkai.Users.Application;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserRepository;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.Mapper.UserDTOMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Spy
    private UserDTOMapper mapper = Mappers.getMapper(UserDTOMapper.class);

    @InjectMocks
    private UserService service;

    User newUser = User.builder()
            .userId(null)
            .firstName("Carlos")
            .lastName("Pérez")
            .email("carlos@zenkai.com")
            .documentNumber("123456789")
            .password("plainPassword")
            .role(Role.ADMIN)
            .status(UserStatus.ACTIVE)
            .build();

    User savedUser = User.builder()
            .userId(1L)
            .firstName("Carlos")
            .lastName("Pérez")
            .email("carlos@zenkai.com")
            .documentNumber("123456789")
            .password("encodedPassword")
            .role(Role.ADMIN)
            .status(UserStatus.ACTIVE)
            .build();

    User editUser = User.builder()
            .userId(1L)
            .firstName("Luis")
            .lastName("García")
            .email("luis@zenkai.com")
            .documentNumber("123456789")
            .password(null)
            .phoneNumber("3001234567")
            .role(Role.STORE)
            .status(UserStatus.ACTIVE)
            .build();

    // ─── saveUser ───────────────────────────────────────────────

    @Test
    void saveUser_success() {
        when(repository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(repository.findByDocumentNumber(newUser.getDocumentNumber())).thenReturn(null);
        when(encoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(repository.save(newUser)).thenReturn(savedUser);

        User result = service.saveUser(newUser);

        assertEquals(savedUser, result);
        verify(encoder, times(1)).encode("plainPassword");
        verify(repository, times(1)).save(newUser);
    }

    @Test
    void saveUser_duplicateEmail_throwsException() {
        when(repository.findByEmail(newUser.getEmail())).thenReturn(savedUser);

        assertThrows(ResourceDuplicateException.class, () -> service.saveUser(newUser));
        verify(repository, never()).save(any());
    }

    @Test
    void saveUser_duplicateDocument_throwsException() {
        when(repository.findByEmail(newUser.getEmail())).thenReturn(null);
        when(repository.findByDocumentNumber(newUser.getDocumentNumber())).thenReturn(savedUser);

        assertThrows(ResourceDuplicateException.class, () -> service.saveUser(newUser));
        verify(repository, never()).save(any());
    }

    // ─── findUserById ────────────────────────────────────────────

    @Test
    void findUserById_success() {
        when(repository.findById(1L)).thenReturn(savedUser);

        User result = service.findUserById(1L);

        assertEquals(savedUser, result);
    }

    @Test
    void findUserById_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.findUserById(99L));
    }

    // ─── findUserByEmail ─────────────────────────────────────────

    @Test
    void findUserByEmail_success() {
        when(repository.findByEmail("carlos@zenkai.com")).thenReturn(savedUser);

        User result = service.findUserByEmail("carlos@zenkai.com");

        assertEquals(savedUser, result);
    }

    @Test
    void findUserByEmail_notFound_throwsException() {
        when(repository.findByEmail("noexiste@zenkai.com")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,
                () -> service.findUserByEmail("noexiste@zenkai.com"));
    }

    // ─── findAllUsers ────────────────────────────────────────────

    @Test
    void findAllUsers() {
        when(repository.findAll()).thenReturn(List.of(savedUser));

        List<UserDTO> result = service.findAllUsers();

        assertEquals("Carlos", result.getFirst().firstName()); // ajusta al campo real del DTO
    }

    // ─── updateUser ──────────────────────────────────────────────

    @Test
    void updateUser_withPassword() {
        User userWithPass = User.builder()
                .firstName("Luis").lastName("García")
                .email("luis@zenkai.com").phoneNumber("3001234567")
                .role(Role.STORE).status(UserStatus.ACTIVE)
                .password("newPassword")
                .build();

        when(repository.findById(1L)).thenReturn(savedUser);
        when(encoder.encode("newPassword")).thenReturn("newEncoded");
        when(repository.save(savedUser)).thenReturn(editUser);

        User result = service.updateUser(1L, userWithPass);

        assertEquals(editUser, result);
        verify(encoder, times(1)).encode("newPassword");
    }

    @Test
    void updateUser_withoutPassword_doesNotEncode() {
        when(repository.findById(1L)).thenReturn(savedUser);
        when(repository.save(savedUser)).thenReturn(editUser);

        service.updateUser(1L, editUser);

        verify(encoder, never()).encode(anyString());
    }

    @Test
    void deleteUserById_success() {
        when(repository.findById(1L)).thenReturn(savedUser);

        service.deleteUserById(1L);

        assertEquals(UserStatus.INACTIVE, savedUser.getStatus());
        verify(repository, times(1)).save(savedUser);
    }

    @Test
    void deleteUserById_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteUserById(99L));
        verify(repository, never()).save(any());
    }

    @Test
    void countUsers() {
        when(repository.countUsers()).thenReturn(5L);

        Long result = service.countUsers();

        assertEquals(5L, result);
    }
}