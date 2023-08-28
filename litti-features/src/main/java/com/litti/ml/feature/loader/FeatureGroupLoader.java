package com.litti.ml.feature.loader;

import java.io.IOException;

public interface FeatureGroupLoader {

  FeatureGroupLoadingResult loadAllFeatureGroups() throws IOException;
}
