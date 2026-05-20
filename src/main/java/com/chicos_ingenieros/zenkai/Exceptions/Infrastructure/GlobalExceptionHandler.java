package com.chicos_ingenieros.zenkai.Exceptions.Infrastructure;

import com.chicos_ingenieros.zenkai.Exceptions.Domain.ErrorDetails;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.IlegalActionException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceDuplicateException;
import com.chicos_ingenieros.zenkai.Exceptions.Domain.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request){
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "NOT FOUND");
        log.error(error.toString());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<ErrorDetails> handleResourceDuplicateException(ResourceDuplicateException ex, WebRequest request){
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "DUPLICATE"
        );
        log.error(error.toString());
        return new ResponseEntity<>(error,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IlegalActionException.class)
    public ResponseEntity<ErrorDetails> handleIllegalActionException(IlegalActionException ex, WebRequest request) {
        ErrorDetails error = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                "ILLEGAL"
        );
        log.error(error.toString());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
