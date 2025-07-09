package io.github.Mine4Cut.Mine4Cut_server.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PageResponse<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    public static <T> PageResponse<T> from(Page<T> pageData) {
        return PageResponse.<T>builder()
                .content(Collections.unmodifiableList(pageData.getContent()))
                .page(pageData.getNumber())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }
}