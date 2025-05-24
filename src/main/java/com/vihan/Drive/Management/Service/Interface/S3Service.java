package com.vihan.Drive.Management.Service.Interface;

import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;

public interface S3Service {

    String uploadFile(MultipartFile file, String userId, String folderPath);

    byte[] downloadFile(String key);

    String generatePresignedUrl(String key, Duration expiration);

    void deleteFile(String key);
}