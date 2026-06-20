package com.personal.website.storage;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * S3-compatible storage using MinIO (or any S3 API).
 * Activate by setting storage.type=minio in application.properties.
 */
@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;
    private final String bucketName;
    private final String baseUrl;

    public MinioStorageService() throws Exception {
        String endpoint = System.getenv().getOrDefault("MINIO_ENDPOINT", "http://127.0.0.1:9000");
        String accessKey = System.getenv().getOrDefault("MINIO_ACCESS_KEY", "minioadmin");
        String secretKey = System.getenv().getOrDefault("MINIO_SECRET_KEY", "minioadmin");
        this.bucketName = System.getenv().getOrDefault("MINIO_BUCKET", "personal-website");
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        // Ensure bucket exists
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        this.baseUrl = endpoint + "/" + bucketName;
    }

    @Override
    public String upload(String fileName, MultipartFile file) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        }
        // Return presigned URL for temporary access
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .expiry(24, TimeUnit.HOURS)
                .build()
        );
    }

    @Override
    public void delete(String fileUrl) throws Exception {
        // Extract object name from URL
        String objectName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        minioClient.removeObject(
            RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
        );
    }

    @Override
    public String getBaseUrl() { return baseUrl; }

    @Override
    public String getType() { return "minio-s3"; }
}