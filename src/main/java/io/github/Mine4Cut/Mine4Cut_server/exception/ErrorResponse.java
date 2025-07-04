package io.github.Mine4Cut.Mine4Cut_server.exception;

import java.time.Instant;

public record ErrorResponse (
        String timeStamp,
        int status,
        String errorCode,
        String message,
        String path
) {
    public static ErrorResponse of(ErrorCode errorCode, String path) {
        return new ErrorResponse(
                Instant.now().toString(),
                errorCode.getStatus(),
                errorCode.getCode(),
                errorCode.getMessage(),
                path
        );
    }
}
