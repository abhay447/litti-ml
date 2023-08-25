package com.litti.ml.model.loader;

import com.litti.ml.entities.management.client.LittiManagementClient;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

public class LittiManagementModelLoader implements ModelLoader {
  private final LittiManagementClient littiManagementClient;

  public LittiManagementModelLoader(LittiManagementClient littiManagementClient) {
    this.littiManagementClient = littiManagementClient;
  }

  @Override
  public ModelLoadingResult loadAllModels() throws IOException {
    return new ModelLoadingResult(
        new HashSet<>(littiManagementClient.listModels()), Collections.emptySet());
  }
}
