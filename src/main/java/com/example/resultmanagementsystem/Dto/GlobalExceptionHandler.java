package com.example.resultmanagementsystem.Dto;

import com.example.resultmanagementsystem.Dto.MetaDTO;
import com.example.resultmanagementsystem.Dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("JSON parsing error: ", ex);

        String errorMessage = "Invalid JSON format";
        String detailedMessage = ex.getMessage();

        // Extract more meaningful error messages from the exception
        if (detailedMessage != null) {
            if (detailedMessage.contains("JSON parse error")) {
                if (detailedMessage.contains("Unexpected character")) {
                    errorMessage = "Invalid JSON syntax: Unexpected character found";
                } else if (detailedMessage.contains("was expecting")) {
                    errorMessage = "Invalid JSON syntax: Missing comma or incorrect structure";
                } else if (detailedMessage.contains("Unexpected end-of-input")) {
                    errorMessage = "Invalid JSON syntax: Incomplete JSON structure";
                } else {
                    errorMessage = "Invalid JSON format provided";
                }
            } else if (detailedMessage.contains("Required request body is missing")) {
                errorMessage = "Request body is required but not provided";
            }
        }

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("BAD_REQUEST")
                        .message("Invalid request format")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation error: ", ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("VALIDATION_ERROR")
                        .message("Validation failed for one or more fields")
                        .build())
                .error(errors)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDTO> handleMissingParams(MissingServletRequestParameterException ex) {
        log.error("Missing parameter error: ", ex);

        String errorMessage = String.format("Required parameter '%s' is missing", ex.getParameterName());

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("MISSING_PARAMETER")
                        .message("Required parameter is missing")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Type mismatch error: ", ex);

        String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(), ex.getName(), ex.getRequiredType().getSimpleName());

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("TYPE_MISMATCH")
                        .message("Parameter type mismatch")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ResponseDTO> handleDateTimeParseException(DateTimeParseException ex) {
        log.error("DateTime parsing error: ", ex);

        String errorMessage = String.format("Invalid date/time format: %s. Expected format: yyyy-MM-ddTHH:mm:ss",
                ex.getParsedString());

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("DATETIME_PARSE_ERROR")
                        .message("Invalid date/time format")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ResponseDTO> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("JSON processing error: ", ex);

        String errorMessage = "Error processing JSON data: " + ex.getOriginalMessage();

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("JSON_PROCESSING_ERROR")
                        .message("JSON processing failed")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .statusDescription("INVALID_ARGUMENT")
                        .message("Invalid argument provided")
                        .build())
                .error(ex.getMessage())
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }



    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: ", ex);

        String errorMessage = "Data integrity constraint violated";
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("unique constraint") || ex.getMessage().contains("Duplicate entry")) {
                errorMessage = "A record with this information already exists";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                errorMessage = "Cannot perform operation due to related data dependencies";
            }
        }

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.CONFLICT.value())
                        .statusDescription("CONFLICT")
                        .message("Data conflict occurred")
                        .build())
                .error(errorMessage)
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ResponseDTO> handleDataAccessException(DataAccessException ex) {
        log.error("Database access error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusDescription("DATABASE_ERROR")
                        .message("Database operation failed")
                        .build())
                .error("A database error occurred. Please try again later.")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO> handleAuthenticationException(AuthenticationException ex) {
        log.error("Authentication error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .statusDescription("UNAUTHORIZED")
                        .message("Authentication failed")
                        .build())
                .error("Invalid credentials or authentication token")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        log.error("Bad credentials error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.UNAUTHORIZED.value())
                        .statusDescription("UNAUTHORIZED")
                        .message("Invalid credentials")
                        .build())
                .error("The provided email or password is incorrect")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO> handleAccessDeniedException(AccessDeniedException ex) {
        log.error("Access denied error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .statusDescription("FORBIDDEN")
                        .message("Access denied")
                        .build())
                .error("You don't have permission to access this resource")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDTO> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.error("No handler found error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .statusDescription("NOT_FOUND")
                        .message("Endpoint not found")
                        .build())
                .error(String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL()))
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime error: ", ex);

        // Determine status based on error message
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String statusDescription = "INTERNAL_SERVER_ERROR";

        if (ex.getMessage() != null) {
            String message = ex.getMessage().toLowerCase();
            if (message.contains("not found")) {
                status = HttpStatus.NOT_FOUND;
                statusDescription = "NOT_FOUND";
            } else if (message.contains("unauthorized") || message.contains("access denied")) {
                status = HttpStatus.FORBIDDEN;
                statusDescription = "FORBIDDEN";
            } else if (message.contains("already exists") || message.contains("duplicate")) {
                status = HttpStatus.CONFLICT;
                statusDescription = "CONFLICT";
            }
        }

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(status.value())
                        .statusDescription(statusDescription)
                        .message("An error occurred while processing your request")
                        .build())
                .error(ex.getMessage())
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGenericException(Exception ex) {
        log.error("Unexpected error: ", ex);

        ResponseDTO errorResponse = ResponseDTO.builder()
                .meta(MetaDTO.builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .statusDescription("INTERNAL_SERVER_ERROR")
                        .message("An unexpected error occurred")
                        .build())
                .error("Internal server error. Please contact support if the problem persists.")
                .correlationId(UUID.randomUUID().toString())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}