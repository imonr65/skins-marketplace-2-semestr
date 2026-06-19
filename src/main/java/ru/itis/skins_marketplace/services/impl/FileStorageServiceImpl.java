package ru.itis.skins_marketplace.services.impl;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.skins_marketplace.models.FileInfo;
import ru.itis.skins_marketplace.repositories.FileInfoRepository;
import ru.itis.skins_marketplace.services.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileInfoRepository fileInfoRepository;

    @Value("${storage.path}")
    private String storagePath;

    @Override
    public String save(MultipartFile uploadFile) {
        String storageName = UUID.randomUUID() + "_" + uploadFile.getOriginalFilename();
        FileInfo file = FileInfo.builder()
                .url(storagePath + "/" + storageName)
                .storageFileName(storageName)
                .originalFileName(uploadFile.getOriginalFilename())
                .type(uploadFile.getContentType())
                .size(uploadFile.getSize())
                .build();
        try {
            Files.copy(uploadFile.getInputStream(), Paths.get(storagePath, storageName));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        fileInfoRepository.save(file);
        return file.getStorageFileName();
    }

    @Override
    public String saveAvatar(MultipartFile uploadFile) {
        String storageName = UUID.randomUUID() + "_" + uploadFile.getOriginalFilename();
        Path avatarDir = Paths.get(storagePath, "avatars");
        try {
            if (!Files.exists(avatarDir)) Files.createDirectories(avatarDir);
            Path filePath = avatarDir.resolve(storageName);
            Files.copy(uploadFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось сохранить аватар", e);
        }
        FileInfo file = FileInfo.builder()
                .url("/uploads/avatars/" + storageName)
                .storageFileName(storageName)
                .originalFileName(uploadFile.getOriginalFilename())
                .type(uploadFile.getContentType())
                .size(uploadFile.getSize())
                .build();
        fileInfoRepository.save(file);
        return "/uploads/avatars/" + storageName;
    }

    @Override
    public void writeFileToResponse(String filename, HttpServletResponse response) {

    }

    @Override
    public Long findFileByName(String filename) {
        return 0L;
    }
}
