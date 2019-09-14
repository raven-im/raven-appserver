package com.raven.appserver.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: bbpatience
 * @date: 2019/7/17
 * @description: S3Config
 **/
@Configuration
public class S3Config {

    @Value("${s3.region}")
    private String region;

    @Value("${s3.key}")
    private String awsKey;

    @Value("${s3.id}")
    private String awsId;

    @Bean
    public AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSCredentialsProvider() {
                @Override
                public AWSCredentials getCredentials() {
                    return new AWSCredentials() {
                        @Override
                        public String getAWSAccessKeyId() {
                            return awsId;
                        }

                        @Override
                        public String getAWSSecretKey() {
                            return awsKey;
                        }
                    };
                }

                @Override
                public void refresh() {

                }
            })
            .build();
    }
}
