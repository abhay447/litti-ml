package com.litti.ml.model.loader;

import java.io.IOException;

public interface ModelLoader {

  ModelLoadingResult loadAllModels() throws IOException;
}
