package io.github.Mine4Cut.Mine4Cut_server.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private final String message;
    private final T data;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> of(String message, T data) {
        return ApiResponse.<T>builder()
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    public static <T> ApiResponse<T> ofSuccess(T data) {
        return ApiResponse.<T>builder()
            .message("SUCCESS")
            .data(data)
            .build();
    }
}