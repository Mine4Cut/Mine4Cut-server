package io.github.Mine4Cut.Mine4Cut_server.config.s3;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "aws")
@Data
@Component
@Validated
public class S3Properties {

    @NotBlank
    private String bucket;
    @NotBlank
    private String region;
    @NotBlank
    private String accessKey;
    @NotBlank
    private String secretKey;
}
