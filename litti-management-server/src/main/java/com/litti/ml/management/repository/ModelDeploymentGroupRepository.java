package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelDeploymentGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelDeploymentGroupRepository
    extends JpaRepository<ModelDeploymentGroupEntity, UUID> {}
