package io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity.FrameLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameLikeRepository extends JpaRepository<FrameLike, Long> {

    boolean existsByUserIdAndFrameId(Long userId, Long frameId);

    void deleteByUserIdAndFrameId(Long userId, Long frameId);
}