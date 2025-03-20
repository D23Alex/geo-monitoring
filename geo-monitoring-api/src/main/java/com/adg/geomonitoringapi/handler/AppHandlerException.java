package com.adg.geomonitoringapi.handler;

import com.adg.geomonitoringapi.exception.EntityNotFoundException;
import com.adg.geomonitoringapi.exception.UnsupportedDtoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class AppHandlerException {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Error handler: Entity not found: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Entity Not Found", ex.getMessage());
    }

    @ExceptionHandler(UnsupportedDtoException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedDtoException(UnsupportedDtoException ex) {
        log.error("Error handler: Unsupported DTO: {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Unsupported DTO", ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Failed to read the HTTP message: {}", ex.getMessage());
        String errorMessage = "Invalid or malformed JSON in the request body";
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Malformed JSON", errorMessage);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, status);
    }
}
