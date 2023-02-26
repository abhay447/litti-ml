package com.litti.ml.feature.store;

import com.litti.ml.feature.entities.FeatureGroup;
import com.litti.ml.feature.entities.FeatureMetadata;
import java.util.List;
import java.util.Map;

public interface FeatureStore {

  String name();

  Map<String, ?> fetchFeatures(
      List<FeatureMetadata> featureMetadataList,
      FeatureGroup featureGroup,
      Map<String, String> inputDimensions);
}
