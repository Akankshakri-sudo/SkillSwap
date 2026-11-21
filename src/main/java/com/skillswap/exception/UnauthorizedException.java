package com.skillswap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UnauthorizedException – thrown when a user tries to do something
 * they're not allowed to do.
 *
 * Examples:
 *   - Student B tries to accept a request meant for Student C
 *   - Student tries to edit another student's profile
 *   - Non-participant tries to mark a session as completed
 *
 * HTTP 403 Forbidden means "you are authenticated but not authorized".
 * (401 Unauthorized means "you are not authenticated at all".)
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
