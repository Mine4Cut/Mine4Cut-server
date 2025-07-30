package io.github.Mine4Cut.Mine4Cut_server.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.github.Mine4Cut.Mine4Cut_server.config.s3.S3Properties;
import io.github.Mine4Cut.Mine4Cut_server.exception.CustomException;
import io.github.Mine4Cut.Mine4Cut_server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {
    private final AmazonS3 amazonS3;

    private final S3Properties s3Properties;


    // TODO 파일 확장자 어떤걸로 받을지
    @Transactional
    public String uploadFrame(MultipartFile file) throws IOException {
        String fileName = getFileName(file);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(
            s3Properties.getBucket(), fileName, file.getInputStream(), metadata
        ));

        return getPublicUrl(fileName);
    }

    @Transactional
    public void deleteFrameImage(String imageUrl) throws CustomException{
        try {
            amazonS3.deleteObject(s3Properties.getBucket(), getFileNameFromUrl(imageUrl));
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.DELETION_FAILED);
        }
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
            s3Properties.getBucket(),
            amazonS3.getRegionName(),
            fileName);
    }

    private String getFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }

    private String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
