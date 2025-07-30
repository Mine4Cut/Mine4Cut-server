package io.github.Mine4Cut.Mine4Cut_server.service.save.dto;

public record SaveDto(
    boolean saved,
    int saveCount
) {
    public static SaveDto of(boolean saved, int saveCount) {
        return new SaveDto(saved, saveCount);
    }
}
