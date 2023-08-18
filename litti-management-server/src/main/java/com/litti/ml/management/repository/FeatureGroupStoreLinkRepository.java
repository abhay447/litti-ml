package com.litti.ml.management.repository;

import com.litti.ml.management.entiites.FeatureGroupStoreLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeatureGroupStoreLinkRepository
    extends JpaRepository<FeatureGroupStoreLinkEntity, UUID> {}
