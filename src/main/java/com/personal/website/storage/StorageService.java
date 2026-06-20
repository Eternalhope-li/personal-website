package com.personal.website.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Storage service interface for file storage.
 * Implementations: FileSystemStorageService (local), MinioStorageService (S3-compatible).
 * Switch between them via Spring profiles or @Conditional.
 */
public interface StorageService {

    /**
     * Upload a file and return its accessible URL.
     */
    String upload(String fileName, MultipartFile file) throws Exception;

    /**
     * Delete a file by its URL/path.
     */
    void delete(String fileUrl) throws Exception;

    /**
     * Get the base URL for serving files.
     */
    String getBaseUrl();

    /**
     * Get the storage type name (for display).
     */
    String getType();
}