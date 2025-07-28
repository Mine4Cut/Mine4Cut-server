package io.github.Mine4Cut.Mine4Cut_server.exception.dto;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public record ErrorResponse(
    String code,
    String message,
    String path,
    String timestamp
) {

    public static ErrorResponse of(String code, String message, String path) {
        return new ErrorResponse(
            code,
            message,
            path,
            LocalDateTime.now().toString()
        );
    }
}
