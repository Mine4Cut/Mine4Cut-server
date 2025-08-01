package io.github.Mine4Cut.Mine4Cut_server.service.like;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository.FrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.entity.FrameLike;
import io.github.Mine4Cut.Mine4Cut_server.domain.frameLike.repository.FrameLikeRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.like.dto.LikeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final FrameLikeRepository frameLikeRepository;
    private final FrameRepository frameRepository;

    @Transactional
    public LikeDto toggleLike(Long userId, Long frameId) {

        Frame frame = frameRepository.findById(frameId)
            .orElseThrow(() -> new NotFoundException("프레임을 찾을 수 없습니다."));

        if(frameLikeRepository.deleteByUserIdAndFrameId(userId, frameId) > 0) {
            frame.decreaseLike();

            return LikeDto.of(false, frame.getLikeCount());
        }

        frameLikeRepository.save(FrameLike.builder()
            .userId(userId)
            .frameId(frameId)
            .build());

        frame.increaseLike();

        return LikeDto.of(true, frame.getLikeCount());
    }
}
