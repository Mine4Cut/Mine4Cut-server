package io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FrameRepository extends JpaRepository<Frame, Long> {
    List<Frame> findAllByUserId(Long userId);
    Optional<Frame> findByFrameName(String frameName);
    boolean existsByFrameName(String frameName);
}