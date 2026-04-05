package com.c1.donguri.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class S3Uploader {

    private final S3Client s3Client;
    private static Map<String, String> envMap;
    
    public S3Uploader() {
        envMap = EnvLoader.loadEnv(".env");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                envMap.get("AWS_S3_ACCESS_KEY"),
                envMap.get("AWS_S3_SECRET_KEY")
        );

        this.s3Client = S3Client.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String upload(InputStream inputStream, String fileName, String contentType, long contentLength) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(envMap.get("AWS_S3_BUCKET_NAME"))
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(request,
                software.amazon.awssdk.core.sync.RequestBody.fromInputStream(inputStream, contentLength));

        // S3 URL 생성
        return "https://" + envMap.get("AWS_S3_BUCKET_NAME") + ".s3.amazonaws.com/" + fileName;
    }


    public String uploadLocalFile(String localFilePath, String fileName) {
        String contentType = "image/png";

        File file = new File(localFilePath + "/" + fileName);

        try (InputStream inputStream = new FileInputStream(file)) {

            String fileUrl = upload(
                    inputStream,
                    fileName,
                    contentType,
                    file.length()
            );

            System.out.println("s3-fileUrl: " + fileUrl);

            return fileUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
