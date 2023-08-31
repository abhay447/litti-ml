package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ArtifactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtifactRepository extends JpaRepository<ArtifactEntity, UUID> {}
