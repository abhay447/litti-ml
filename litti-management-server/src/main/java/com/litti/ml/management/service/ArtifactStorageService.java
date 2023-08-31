package com.litti.ml.management.service;

import com.litti.ml.management.config.ArtifactStorageConfig;
import com.litti.ml.management.entiites.ArtifactEntity;
import com.litti.ml.management.repository.ArtifactRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArtifactStorageService {

  private static final Logger logger = LogManager.getLogger(ArtifactStorageService.class);

  private static final String STORAGE_TYPE_VOLUME_MOUNT = "VOLUME_MOUNT";

  @Autowired private ArtifactStorageConfig artifactStorageConfig;

  @Autowired private ArtifactRepository artifactRepository;

  public ArtifactEntity store(MultipartFile file) {
    final UUID artifactId = UUID.randomUUID();
    final Path rootLocation = Paths.get(this.artifactStorageConfig.getRootLocation());
    if (file.isEmpty()) {
      throw new RuntimeException("Failed to store empty file.");
    }
    Path destinationFile =
        rootLocation
            .resolve(Paths.get(artifactId + "/" + file.getOriginalFilename()))
            .normalize()
            .toAbsolutePath();
    try (InputStream inputStream = file.getInputStream()) {
      Files.createDirectories(destinationFile);
      Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException("Failed to store file.", e);
    }
    final ArtifactEntity artifactEntity =
        new ArtifactEntity(artifactId, STORAGE_TYPE_VOLUME_MOUNT, destinationFile);
    this.artifactRepository.save(artifactEntity);
    return artifactEntity;
  }

  public Optional<ArtifactEntity> get(String artifactId) {
    return artifactRepository.findById(UUID.fromString(artifactId));
  }
}
