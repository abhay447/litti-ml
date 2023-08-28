package com.litti.ml.feature.loader;

import com.google.common.collect.Sets;
import com.litti.ml.entities.management.client.LittiManagementClient;

import java.io.IOException;
import java.util.Set;

public class LittiManagementFGLoader implements FeatureGroupLoader {

  private final LittiManagementClient littiManagementClient;

  public LittiManagementFGLoader(LittiManagementClient littiManagementClient) {
    this.littiManagementClient = littiManagementClient;
  }

  @Override
  public FeatureGroupLoadingResult loadAllFeatureGroups() throws IOException {
    return new FeatureGroupLoadingResult(
        Sets.newHashSet(this.littiManagementClient.listFeatureGroups()), Set.of());
  }
}
