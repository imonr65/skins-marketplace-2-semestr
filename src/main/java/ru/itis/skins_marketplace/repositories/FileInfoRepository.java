package ru.itis.skins_marketplace.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.skins_marketplace.models.FileInfo;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    Optional<FileInfo> findFileInfoByOriginalFileName(String originalFileName);
}
