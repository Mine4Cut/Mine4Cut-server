package io.github.Mine4Cut.Mine4Cut_server.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(CustomException customException, WebRequest req) {
        HttpHeaders resHeaders = new HttpHeaders();
        resHeaders.add("Content-Type", "application/json;charset=UTF08");

        String path = ((ServletWebRequest) req).getRequest().getRequestURI();

        ErrorCode errorCode = customException.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, path);

        HttpStatus status = HttpStatus.resolve(errorCode.getStatus());
        if(status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(errorResponse, resHeaders, status);
    }
}