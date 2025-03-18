package com.ratingsystem.RatingSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String,Object>> handleUndefinedRoutes(NoHandlerFoundException exception){

        Map<String, Object> message = new HashMap<>();
        message.put("status", HttpStatus.NOT_FOUND);
        message.put("message", exception.getMessage());
        return ResponseEntity.badRequest().body(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleFiledValidationException(MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String> map = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(MailSendFailure.class)
    public ResponseEntity<?> handleMailSendFailure(MailSendFailure mailSendFailure){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mailSendFailure.getMessage());
    }

}
