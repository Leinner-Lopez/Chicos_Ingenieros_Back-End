package com.chicos_ingenieros.zenkai.Exceptions.Infrastructure;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ErrorDetails;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        webRequest = new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void handleResourceNotFoundException_debeRetornar404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");

        ResponseEntity<ErrorDetails> response = handler.handleResourceNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recurso no encontrado", response.getBody().getMessage());
        assertEquals("NOT FOUND", response.getBody().getErrorCode());
    }

    @Test
    void handleResourceDuplicateException_debeRetornar409() {
        ResourceDuplicateException ex = new ResourceDuplicateException("Recurso duplicado");

        ResponseEntity<ErrorDetails> response = handler.handleResourceDuplicateException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recurso duplicado", response.getBody().getMessage());
        assertEquals("DUPLICATE", response.getBody().getErrorCode());
    }

    @Test
    void handleIllegalActionException_debeRetornar400() {
        IlegalActionException ex = new IlegalActionException("Acción no permitida");

        ResponseEntity<ErrorDetails> response = handler.handleIllegalActionException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acción no permitida", response.getBody().getMessage());
        assertEquals("ILLEGAL", response.getBody().getErrorCode());
    }

    @Test
    void todosLosHandlers_debenRetornarTimestampNoNulo() {
        ResourceNotFoundException ex = new ResourceNotFoundException("test");

        ResponseEntity<ErrorDetails> response = handler.handleResourceNotFoundException(ex, webRequest);

        assertNotNull(response.getBody().getTimeStamp());
    }
}