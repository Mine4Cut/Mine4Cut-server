package io.github.Mine4Cut.Mine4Cut_server.exception;

import java.util.List;
import java.util.Objects;

import io.github.Mine4Cut.Mine4Cut_server.common.dto.ApiResponse;
import io.github.Mine4Cut.Mine4Cut_server.exception.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF-8");
        return resHeaders;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustom(CustomException ex, WebRequest req) {
        HttpHeaders resHeaders = getHttpHeaders();

        String path = ((ServletWebRequest) req).getRequest().getRequestURI();

        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getMessage(), ex.getMessage(), path);

        return ResponseEntity
            .status(Objects.requireNonNull(errorCode.getStatus()))
            .headers(resHeaders)
            .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<String> errors = result.getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, WebRequest req) {
        HttpHeaders resHeaders = getHttpHeaders();

        String path = ((ServletWebRequest) req).getRequest().getRequestURI();

        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getMessage(), ex.getMessage(), path);

        HttpStatus status = errorCode.getStatus();
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(errorResponse, resHeaders, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, WebRequest req) {
        HttpHeaders resHeaders = getHttpHeaders();

        String path = ((ServletWebRequest) req).getRequest().getRequestURI();

        ErrorCode errorCode = ErrorCode.NOT_FOUND;

        return ResponseEntity
            .status(errorCode.getStatus())
            .headers(resHeaders)
            .body(ErrorResponse.of(errorCode.getMessage(), ex.getReason(), path));
    }
}