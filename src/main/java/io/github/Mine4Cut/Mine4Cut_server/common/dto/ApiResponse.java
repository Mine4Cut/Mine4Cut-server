package io.github.Mine4Cut.Mine4Cut_server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> of(Boolean success, String message, T data) {
        return ApiResponse.<T>builder()
                .success(success)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
