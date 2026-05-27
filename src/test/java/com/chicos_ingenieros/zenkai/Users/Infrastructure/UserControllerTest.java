package com.chicos_ingenieros.zenkai.Users.Infrastructure;

import com.chicos_ingenieros.zenkai.Config.Application.JwtService;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import com.chicos_ingenieros.zenkai.Users.Application.UserService;
import com.chicos_ingenieros.zenkai.Users.Domain.Role;
import com.chicos_ingenieros.zenkai.Users.Domain.User;
import com.chicos_ingenieros.zenkai.Users.Domain.UserStatus;
import com.chicos_ingenieros.zenkai.Users.Infrastructure.DTO.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private User userSample;
    private UserDTO userDTOSample;

    @BeforeEach
    void setUp() {
        userSample = User.builder()
                .userId(1L)
                .documentNumber("1234567890")
                .firstName("Juan")
                .lastName("Pérez")
                .email("juan@test.com")
                .phoneNumber("3001234567")
                .password("encodedPassword")
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        userDTOSample = new UserDTO(1L, "Juan", "Pérez", "juan@test.com", Role.CUSTOMER);
    }

    // ─── POST /api/users ────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /api/users - Debería guardar un usuario y retornar 201 Created")
    void save_ShouldReturnCreatedUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(userSample);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSample)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@test.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        verify(userService, times(1)).saveUser(any(User.class));
    }

    @Test
    @DisplayName("POST /api/users - Debería retornar 409 Conflict cuando el usuario ya existe")
    void save_ShouldReturn409WhenDuplicateUser() throws Exception {
        when(userService.saveUser(any(User.class)))
                .thenThrow(new ResourceDuplicateException("User already exists in the system"));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSample)))
                .andExpect(status().isConflict());

        verify(userService, times(1)).saveUser(any(User.class));
    }

    // ─── GET /api/users ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/users - Debería retornar la lista de usuarios como DTO y 200 OK")
    void getAll_ShouldReturnUserDTOList() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of(userDTOSample));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].email").value("juan@test.com"));

        verify(userService, times(1)).findAllUsers();
    }

    @Test
    @DisplayName("GET /api/users - Debería retornar lista vacía cuando no hay usuarios")
    void getAll_ShouldReturnEmptyListWhenNoUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(userService, times(1)).findAllUsers();
    }

    // ─── GET /api/users/{id} ─────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/users/{id} - Debería retornar el usuario correspondiente y 200 OK")
    void findById_ShouldReturnUser() throws Exception {
        when(userService.findUserById(1L)).thenReturn(userSample);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));

        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    @DisplayName("GET /api/users/{id} - Debería retornar 404 cuando el usuario no existe")
    void findById_ShouldReturn404WhenNotFound() throws Exception {
        when(userService.findUserById(99L))
                .thenThrow(new ResourceNotFoundException("User with id 99 not found"));

        mockMvc.perform(get("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserById(99L);
    }

    // ─── GET /api/users/email/{email} ────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/users/email/{email} - Debería retornar el usuario por email y 200 OK")
    void findByEmail_ShouldReturnUser() throws Exception {
        when(userService.findUserByEmail("juan@test.com")).thenReturn(userSample);

        mockMvc.perform(get("/api/users/email/{email}", "juan@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("juan@test.com"))
                .andExpect(jsonPath("$.firstName").value("Juan"));

        verify(userService, times(1)).findUserByEmail("juan@test.com");
    }

    @Test
    @DisplayName("GET /api/users/email/{email} - Debería retornar 404 cuando el email no existe")
    void findByEmail_ShouldReturn404WhenNotFound() throws Exception {
        when(userService.findUserByEmail("noexiste@test.com"))
                .thenThrow(new ResourceNotFoundException("User with email noexiste@test.com not found"));

        mockMvc.perform(get("/api/users/email/{email}", "noexiste@test.com"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findUserByEmail("noexiste@test.com");
    }

    // ─── GET /api/users/count ────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/users/count - Debería retornar el total de usuarios y 200 OK")
    void countUsers_ShouldReturnTotalCount() throws Exception {
        when(userService.countUsers()).thenReturn(10L);

        mockMvc.perform(get("/api/users/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(userService, times(1)).countUsers();
    }

    // ─── PUT /api/users ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("PUT /api/users - Debería actualizar un usuario y retornar 200 OK")
    void update_ShouldReturnUpdatedUser() throws Exception {
        User updatedUser = User.builder()
                .userId(1L)
                .firstName("Carlos")
                .lastName("López")
                .email("carlos@test.com")
                .phoneNumber("3109876543")
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Carlos"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    @DisplayName("PUT /api/users - Debería retornar 404 cuando el usuario a actualizar no existe")
    void update_ShouldReturn404WhenUserNotFound() throws Exception {
        when(userService.updateUser(eq(99L), any(User.class)))
                .thenThrow(new ResourceNotFoundException("User with id 99 not found"));

        User nonExistent = User.builder().userId(99L).firstName("X").build();

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistent)))
                .andExpect(status().isNotFound());
    }

    // ─── DELETE /api/users/{id} ──────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /api/users/{id} - Debería eliminar (desactivar) el usuario y retornar 200 OK")
    void delete_ShouldReturnStatusOk() throws Exception {
        doNothing().when(userService).deleteUserById(1L);

        mockMvc.perform(delete("/api/users/{id}", 1L))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUserById(1L);
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Debería retornar 404 cuando el usuario no existe")
    void delete_ShouldReturn404WhenNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User with id 99 not found"))
                .when(userService).deleteUserById(99L);

        mockMvc.perform(delete("/api/users/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUserById(99L);
    }
}
