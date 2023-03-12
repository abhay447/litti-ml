package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelDeploymentGroupLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModelDeploymentGroupLinkRepository
    extends JpaRepository<ModelDeploymentGroupLinkEntity, UUID> {}
