package io.github.Mine4Cut.Mine4Cut_server.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse (
        HttpStatus status,
        String errorCode,
        String message,
        String path,
        String timestamp
        ) {
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                path,
                LocalDateTime.now().toString()
                );
    }
}
