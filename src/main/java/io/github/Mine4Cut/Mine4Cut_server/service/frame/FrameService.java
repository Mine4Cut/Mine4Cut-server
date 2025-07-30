package io.github.Mine4Cut.Mine4Cut_server.service.frame;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository.FrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository.FrameLikeRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.CreateFrameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class FrameService {

    private final FrameRepository frameRepository;

    private final FrameLikeRepository frameLikeRepository;

    @Transactional
    public CreateFrameDto createFrame(Long userId,
                                      String nickname,
                                      String frameName,
                                      String imageUrl
    ) {
        frameRepository.save(Frame.builder()
            .userId(userId)
            .nicknameSnapshot(nickname)
            .frameName(frameName)
            .imageUrl(imageUrl)
            .build());

        return CreateFrameDto.of(userId, nickname, frameName, imageUrl);
    }

    @Transactional
    public String deleteFrame(Long userId, Long frameId) throws AccessDeniedException {
        Frame frame = frameRepository.findById(frameId)
            .orElseThrow(() -> new NotFoundException("프레임을 찾을 수 없습니다."));

        if(!frame.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인의 프레임만 삭제할 수 있습니다.");
        }

        frameRepository.delete(frame);

        frameLikeRepository.deleteByFrameId(frameId);

        return frame.getImageUrl();
    }
}