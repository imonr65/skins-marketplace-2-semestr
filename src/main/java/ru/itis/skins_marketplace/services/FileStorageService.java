package ru.itis.skins_marketplace.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String save(MultipartFile uploadFile);

    String saveAvatar(MultipartFile avatar);

    void writeFileToResponse(String filename, HttpServletResponse response);

    Long findFileByName(String filename);
}
