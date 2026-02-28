package com.brandmanagement.exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /* ─── Validation errors ─────────────────────────────────────────── */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }
        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    /* ─── Not found ─────────────────────────────────────────────────── */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    /* ─── Duplicate brand ───────────────────────────────────────────── */
    @ExceptionHandler(DuplicateBrandException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateBrandException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    /* ─── Brand linked to zone ──────────────────────────────────────── */
    @ExceptionHandler(BrandLinkedToZoneException.class)
    public ResponseEntity<Map<String, Object>> handleLinkedToZone(BrandLinkedToZoneException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    /* ─── Generic ───────────────────────────────────────────────────── */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage(), null);
    }

    /* ─── Helper ─────────────────────────────────────────────────────── */
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message, Object details) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        if (details != null) body.put("details", details);
        return ResponseEntity.status(status).body(body);
    }
}
