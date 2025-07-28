package io.github.Mine4Cut.Mine4Cut_server.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
    HttpStatus status,
    String errorCode,
    String message,
    String path,
    String timestamp
) {

    public static ErrorResponse of(ErrorCode errorCode, Exception ex, String path) {
        return new ErrorResponse(
            errorCode.getStatus(),
            errorCode.getCode(),
            ex.getMessage(),
            path,
            LocalDateTime.now().toString()
        );
    }
}
