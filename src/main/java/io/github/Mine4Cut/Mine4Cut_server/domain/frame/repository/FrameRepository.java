package io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FrameRepository extends JpaRepository<Frame, Long> {

    Page<Frame> findAllByUserId(Long userId, Pageable pageable);

    @Query("""
      select f from Frame f
      where lower(f.frameName) like lower(concat('%', :kw, '%'))
      or lower(f.nicknameSnapshot) like lower(concat('%', :kw, '%'))
    """)
    Page<Frame> searchByKeyword(@Param("kw") String keyword, Pageable pageable);

    @Query("""
      SELECT f FROM Frame f JOIN SavedFrame s ON f.id = s.frameId
      WHERE s.userId = :userId
      ORDER BY s.createdAt DESC
    """)
    Page<Frame> findSavedFramesByUserId(
        @Param("userId") Long userId, Pageable pageable
    );

    List<Frame> findAllByDeletedTrueAndDeletedAtBefore(LocalDateTime frameCutoff);
}