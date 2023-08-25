package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureGroupStoreLinkEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureGroupStoreLinkRepository
    extends JpaRepository<FeatureGroupStoreLinkEntity, UUID> {}
