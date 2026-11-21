package com.skillswap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * BadRequestException – thrown for invalid business logic requests.
 *
 * Examples:
 *   - Student tries to send a request to themselves
 *   - Duplicate pending request
 *   - EXCHANGE request without a skill to teach
 *   - Trying to add a skill the user already has
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
