package com.skillswap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ResourceNotFoundException – thrown when a requested resource doesn't exist.
 *
 * Example: GET /api/users/999 where user 999 doesn't exist.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND) automatically sets the HTTP
 * response code to 404 when this exception reaches the controller.
 *
 * However, since we have a @RestControllerAdvice (GlobalExceptionHandler),
 * that handler intercepts this exception and formats a consistent JSON error body.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Convenience constructor: "User not found with id: 42"
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
    }
}
