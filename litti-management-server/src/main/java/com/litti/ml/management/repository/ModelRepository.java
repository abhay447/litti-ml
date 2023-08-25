package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<ModelEntity, UUID> {}
