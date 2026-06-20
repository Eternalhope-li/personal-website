package com.personal.website.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@ConditionalOnMissingBean(MinioStorageService.class)
public class FileSystemStorageService implements StorageService {

    private final String uploadDir;
    private final String baseUrl = "/uploads";

    public FileSystemStorageService(@Value("${upload.dir:./uploads}") String uploadDir) {
        this.uploadDir = uploadDir.replace("\\", "/");
        File dir = new File(this.uploadDir);
        if (!dir.exists()) dir.mkdirs();
        File avatarsDir = new File(this.uploadDir + "/avatars");
        if (!avatarsDir.exists()) avatarsDir.mkdirs();
    }

    @Override
    public String upload(String fileName, MultipartFile file) throws Exception {
        Path targetPath = Paths.get(uploadDir + "/" + fileName);
        File parent = targetPath.getParent().toFile();
        if (!parent.exists()) parent.mkdirs();
        Files.copy(file.getInputStream(), targetPath);
        return baseUrl + "/" + fileName;
    }

    @Override
    public void delete(String fileUrl) throws Exception {
        if (fileUrl == null || fileUrl.isEmpty()) return;
        String relativePath = fileUrl.replace(baseUrl + "/", "");
        Path filePath = Paths.get(uploadDir + "/" + relativePath);
        Files.deleteIfExists(filePath);
    }

    @Override
    public String getBaseUrl() { return baseUrl; }

    @Override
    public String getType() { return "filesystem"; }
}
