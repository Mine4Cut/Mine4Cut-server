package io.github.Mine4Cut.Mine4Cut_server.shceduler;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository.FrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.user.repository.UserRepository;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.FrameService;
import io.github.Mine4Cut.Mine4Cut_server.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PurgeScheduler {

    private final UserRepository userRepository;
    private final FrameRepository frameRepository;

    private final FrameService frameService;
    private final StorageService storageService;

    // 이렇게 하는것보다 얌에 넣는게 낫겠죠?
    private static final long USER_RETENTION_DAYS = 30;
    private static final long FRAME_RETENTION_DAYS = 1;

    @Scheduled(cron = "0 0 1 * * ?")
    public void purgeSoftDeleted() {
        purgeDeletedUsers();

        purgeDeletedFrames();
    }

    @Transactional
    private void purgeDeletedUsers() {
        LocalDateTime userCutoff = LocalDateTime.now().minusDays(USER_RETENTION_DAYS);

        userRepository.deleteAllByDeletedTrueAndDeletedAtBefore(userCutoff);
    }

    @Transactional
    private void purgeDeletedFrames() { // TODO 배치 방식 고려해봐야할듯
        LocalDateTime frameCutoff = LocalDateTime.now().minusDays(FRAME_RETENTION_DAYS);

        List<Frame> deletedFrames = frameRepository
            .findAllByDeletedTrueAndDeletedAtBefore(frameCutoff);

        for (Frame frame : deletedFrames) {
            storageService.deleteFrameImage(frameService.hardDeleteFrame(frame));
        }
    }
}
