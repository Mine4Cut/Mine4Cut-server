package io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Long> {

    List<Frame> findAllByUserId(Long userId);

    Optional<Frame> findByFrameName(String frameName);

    boolean existsByFrameName(String frameName);

    @Query("""
      select f from Frame f
       where lower(f.frameName) like lower(concat('%', :kw, '%'))
          or lower(f.nicknameSnapshot) like lower(concat('%', :kw, '%'))
    """)
    Page<Frame> searchByKeyword(@Param("kw") String keyword, Pageable pageable);
}