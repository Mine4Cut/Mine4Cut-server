package io.github.Mine4Cut.Mine4Cut_server.service.save;

import io.github.Mine4Cut.Mine4Cut_server.domain.frame.entity.Frame;
import io.github.Mine4Cut.Mine4Cut_server.domain.frame.repository.FrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.entity.SavedFrame;
import io.github.Mine4Cut.Mine4Cut_server.domain.savedFrame.repository.SavedFrameRepository;
import io.github.Mine4Cut.Mine4Cut_server.exception.NotFoundException;
import io.github.Mine4Cut.Mine4Cut_server.service.save.dto.SaveDto;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SaveService {

    private final SavedFrameRepository savedFrameRepository;
    private final FrameRepository frameRepository;

    @Transactional
    public SaveDto toggleSave(Long userId, Long frameId) throws BadRequestException{

        Frame frame =  frameRepository.findById(frameId)
            .orElseThrow(() -> new NotFoundException("프레임을 찾을 수 없습니다."));

        if(frame.getUserId().equals(userId)) {
            throw new BadRequestException("본인의 프레임은 저장할 수 없습니다.");
        }

        if(savedFrameRepository.deleteSave(userId, frameId) > 0) {
            frame.decreaseSave();

            return SaveDto.of(false, frame.getSaveCount());
        }

        savedFrameRepository.save(SavedFrame.builder()
            .userId(userId)
            .frameId(frameId)
            .build());

        frame.increaseSave();

        return SaveDto.of(true, frame.getSaveCount());
    }
}
