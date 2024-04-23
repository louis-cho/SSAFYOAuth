package com.ssafy.authorization.common.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) throws IOException {
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("지원되지 않는 파일 형식입니다.");
        }
        String fileName = generateFileName(file.getOriginalFilename());
        File fileObj = convertMultiPartFileToFile(file, fileName);
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, fileObj));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } finally {
            // S3 업로드 후 임시 파일 삭제
            if (fileObj.delete()) {
                log.info("임시 파일이 성공적으로 삭제되었습니다: {}", fileObj.getName());
            } else {
                log.warn("임시 파일 삭제 실패: {}", fileObj.getName());
            }
        }
    }

    private String generateFileName(String originalFilename) {
        // 확장자를 유지하기 위해 원본 파일 이름에서 확장자 추출
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // UUID를 사용하여 랜덤 파일 이름 생성
        return UUID.randomUUID().toString() + extension;
    }

    private File convertMultiPartFileToFile(MultipartFile file, String fileName) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + fileName);

        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
        }

        return convertFile;
    }
}
