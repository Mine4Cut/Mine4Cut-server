package io.github.Mine4Cut.Mine4Cut_server.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_INPUT"),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR");

    private final HttpStatus status;
    private final String code;

    ErrorCode(HttpStatus status, final String code) {
        this.status = status;
        this.code = code;
    }
}
