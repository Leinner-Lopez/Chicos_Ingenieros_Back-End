package com.chicos_ingenieros.zenkai.Config.Application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    // Clave Base64 válida de 256 bits (32 bytes) — solo para tests
    private static final String TEST_SECRET =
            "dGVzdFNlY3JldEtleVBhcmFaZW5rYWlQcm9qZWN0MTIz";

    UserDetails userDetails = new User(
            "carlos@zenkai.com",
            "encodedPassword",
            List.of(new SimpleGrantedAuthority("CUSTOMER"))
    );

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Inyecta el @Value manualmente sin necesitar Spring context
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
    }

    // ─── getToken ────────────────────────────────────────────────

    @Test
    void getToken_returnsNonNullToken() {
        String token = jwtService.getToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
        // Un JWT siempre tiene 3 partes separadas por punto
        assertEquals(3, token.split("\\.").length);
    }

    // ─── getUserNameFromToken ────────────────────────────────────

    @Test
    void getUserNameFromToken_returnsCorrectEmail() {
        String token = jwtService.getToken(userDetails);

        String username = jwtService.getUserNameFromToken(token);

        assertEquals("carlos@zenkai.com", username);
    }

    // ─── isTokenValid ────────────────────────────────────────────

    @Test
    void isTokenValid_withCorrectUser_returnsTrue() {
        String token = jwtService.getToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertTrue(valid);
    }

    @Test
    void isTokenValid_withWrongUser_returnsFalse() {
        String token = jwtService.getToken(userDetails);

        UserDetails otherUser = new User(
                "otro@zenkai.com",
                "pass",
                List.of(new SimpleGrantedAuthority("CUSTOMER"))
        );

        boolean valid = jwtService.isTokenValid(token, otherUser);

        assertFalse(valid);
    }

    // ─── getExpirationDate ───────────────────────────────────────

    @Test
    void getExpirationDate_isInTheFuture() {
        String token = jwtService.getToken(userDetails);

        Date expiration = jwtService.getExpirationDate(token);

        assertTrue(expiration.after(new Date()));
    }

    // ─── getClaim / role ─────────────────────────────────────────

    @Test
    void getToken_containsRoleClaim() {
        String token = jwtService.getToken(userDetails);

        String role = jwtService.getClaim(token, claims -> claims.get("role", String.class));

        assertEquals("CUSTOMER", role);
    }

    // ─── token alterado ──────────────────────────────────────────

    @Test
    void getUserNameFromToken_withTamperedToken_throwsException() {
        String token = jwtService.getToken(userDetails);
        // Alterar la firma del token
        String tampered = token.substring(0, token.lastIndexOf('.') + 1) + "firmaFalsa";

        assertThrows(Exception.class, () -> jwtService.getUserNameFromToken(tampered));
    }
}