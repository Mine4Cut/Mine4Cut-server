package io.github.Mine4Cut.Mine4Cut_server.service.frame;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository.FrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository.FrameLikeRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.repository.SavedFrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.CreateFrameDto;
import io.github.Mine4Cut.Mine4Cut_server.service.frame.dto.FrameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class FrameService {

    private final FrameRepository frameRepository;

    private final FrameLikeRepository frameLikeRepository;

    private final SavedFrameRepository savedFrameRepository;

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
    public void deleteFrame(Long userId, Long frameId) throws AccessDeniedException {
        Frame frame = frameRepository.findById(frameId)
            .orElseThrow(() -> new NotFoundException("프레임을 찾을 수 없습니다."));

        if(!frame.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인의 프레임만 삭제할 수 있습니다.");
        }

        frame.softDelete();
    }

    public Page<FrameDto> searchFrames(
        String keyword, Pageable pageable
    ) {
        return frameRepository
            .searchByKeyword(keyword, pageable).map(FrameDto::from);
    }

    public Page<FrameDto> getMyFrames(
        Long userId, Pageable pageable
    ) {
        return frameRepository
            .findAllByUserId(userId, pageable).map(FrameDto::from);
    }

    public Page<FrameDto> getSavedFrames(
        Long userId, Pageable pageable
    ) {
        return frameRepository
            .findSavedFramesByUserId(userId, pageable).map(FrameDto::from);
    }

    public String hardDeleteFrame(Frame frame) {
        frameRepository.delete(frame);

        frameLikeRepository.deleteByFrameId(frame.getId());

        savedFrameRepository.deleteByFrameId(frame.getId());

        return frame.getImageUrl();
    }
}