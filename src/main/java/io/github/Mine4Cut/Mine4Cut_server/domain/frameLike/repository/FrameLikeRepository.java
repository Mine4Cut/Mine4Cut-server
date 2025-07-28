package io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity.FrameLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FrameLikeRepository extends JpaRepository<FrameLike, Long> {

    boolean existsByUserIdAndFrameId(Long userId, Long frameId);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM FrameLike fl WHERE fl.userId = :userId AND fl.frameId = :frameId")
    int deleteLike(@Param("userId") Long userId, @Param("frameId") Long frameId);
}