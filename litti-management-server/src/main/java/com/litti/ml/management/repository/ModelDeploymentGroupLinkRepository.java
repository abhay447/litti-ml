package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.ModelDeploymentGroupLinkEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelDeploymentGroupLinkRepository
    extends JpaRepository<ModelDeploymentGroupLinkEntity, UUID> {}
