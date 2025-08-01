package io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.entity.SavedFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface SavedFrameRepository extends JpaRepository<SavedFrame, Long> {

    @Modifying
    int deleteByUserIdAndFrameId(Long userId, Long frameId);

    void deleteByFrameId(Long frameId);
}
