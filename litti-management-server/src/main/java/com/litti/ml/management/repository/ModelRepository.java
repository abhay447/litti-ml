package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelRepository extends JpaRepository<ModelEntity, UUID> {}
