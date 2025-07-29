package io.github.Mine4Cut.Mine4Cut_server.config.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(S3Properties.class)
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(s3Properties.getAccessKey(),
            s3Properties.getSecretKey());
        return AmazonS3ClientBuilder.standard()
            .withRegion(s3Properties.getRegion())
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .build();
    }
}
