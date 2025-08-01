package io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.repository;

import io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.entity.SavedFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavedFrameRepository extends JpaRepository<SavedFrame, Long> {

    @Modifying
    @Query("DELETE FROM SavedFrame sf WHERE sf.userId = :userId AND sf.frameId = :frameId")
    int deleteSave(@Param("userId") Long userId, @Param("frameId") Long frameId);

    void deleteByFrameId(Long frameId);
}
