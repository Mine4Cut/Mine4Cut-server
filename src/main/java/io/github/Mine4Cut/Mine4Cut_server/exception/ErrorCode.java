package io.github.Mine4Cut.Mine4Cut_server.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"해당 내용을 찾을 수 없습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_INPUT"),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR"),

    DELETION_FAILED(HttpStatus.FAILED_DEPENDENCY, "삭제에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }
}
