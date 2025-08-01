package io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity.FrameLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface FrameLikeRepository extends JpaRepository<FrameLike, Long> {

    @Modifying
    int deleteByUserIdAndFrameId(Long userId, Long frameId);

    void deleteByFrameId(Long frameId);
}