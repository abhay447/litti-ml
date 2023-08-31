package com.litti.ml.management.service;

import com.litti.ml.management.config.ArtifactStorageConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ArtifactStorageService {

  private static final Logger logger = LogManager.getLogger(ArtifactStorageService.class);

  @Autowired private ArtifactStorageConfig artifactStorageConfig;

  public String store(MultipartFile file) {
    final Path rootLocation = Paths.get(this.artifactStorageConfig.getRootLocation());
    if (file.isEmpty()) {
      throw new RuntimeException("Failed to store empty file.");
    }
    Path destinationFile =
        rootLocation
            .resolve(Paths.get(UUID.randomUUID() + "/" + file.getOriginalFilename()))
            .normalize()
            .toAbsolutePath();
    try (InputStream inputStream = file.getInputStream()) {
      Files.createDirectories(destinationFile);
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file.", e);
    }

    return destinationFile.toAbsolutePath().toString();
  }
}
