package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ArtifactEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<ArtifactEntity, UUID> {}
