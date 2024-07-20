package com.yo.webtoon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public String getImgUrl(String imgKey) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, imgKey);
    }

    @Async
    public void putObject(String imgKey, MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        PutObjectRequest putObjectRequest;
        try {
            putObjectRequest = new PutObjectRequest(
                bucketName,
                imgKey,
                multipartFile.getInputStream(),
                objectMetadata);
        } catch (IOException e) {
            throw new WebtoonException(ErrorCode.S3_IO_EXCEPTION);
        }

        amazonS3Client.putObject(putObjectRequest);
    }

}
