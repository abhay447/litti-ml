package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelDeploymentGroupEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelDeploymentGroupRepository
    extends JpaRepository<ModelDeploymentGroupEntity, UUID> {}
