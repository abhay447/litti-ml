package com.litti.ml.model.logger;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModelConsoleLogger extends ModelLogger {

  private static final Logger logger = LogManager.getLogger(ModelConsoleLogger.class);
  private final Gson gson;

  public ModelConsoleLogger() {
    this.gson = new Gson();
  }

  @Override
  public void logModelPrediction(ModelLogRecord modelLogRecord) {
    logger.info(gson.toJson(modelLogRecord));
  }
}
