package com.yo.webtoon.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yo.webtoon.exception.WebtoonException;
import com.yo.webtoon.model.constant.ErrorCode;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    private final AmazonS3 amazonS3Client;

    @Value("${aws.s3.region}")
    private String region;

    public String putObject(String imgKey, MultipartFile multipartFile) {
        String bucketName = "donation-webtoon";

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

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, imgKey);
    }

}
