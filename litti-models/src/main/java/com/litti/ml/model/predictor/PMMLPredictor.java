package com.litti.ml.model.predictor;

import com.google.common.io.Resources;
import com.litti.ml.entities.model.ModelMetadata;
import com.litti.ml.entities.model.ModelOutputMetadata;
import com.litti.ml.entities.model.PredictionResponse;
import com.litti.ml.feature.FeatureFetchRouter;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

public class PMMLPredictor extends AbstractPredictor {

  static Logger logger = LogManager.getLogger(PMMLPredictor.class);
  private final Evaluator evaluator;
  private final Map<String, ModelOutputMetadata> outputFieldsMap;

  public PMMLPredictor(ModelMetadata modelMetadata, FeatureFetchRouter featureFetchRouter)
      throws JAXBException, IOException, ParserConfigurationException, SAXException {
    super(modelMetadata, featureFetchRouter);
    this.evaluator =
        new LoadingModelEvaluatorBuilder()
            .load(new File(Resources.getResource(modelMetadata.modelLocation()).getFile()))
            .build();
    evaluator.verify();

    this.outputFieldsMap =
        modelMetadata.outputs().stream()
            .map(output -> Map.entry(output.name(), output))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public PredictionResponse predictSingle(String predictionId, Map<String, ?> features) {
    try {
      final Map<String, ?> outputs =
          EvaluatorUtil.decodeAll(evaluator.evaluate(features)).entrySet().stream()
              .filter(entry -> outputFieldsMap.containsKey(entry.getKey()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      if (!outputs.keySet().equals(outputFieldsMap.keySet())) {
        throw new RuntimeException(
            "Unable to retrieve all output fields, please check the "
                + "prediction request for missing params");
      }
      return new PredictionResponse(predictionId, outputs, null);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e);
      return new PredictionResponse(predictionId, null, e.getMessage());
    }
  }

  @Override
  public Set<PredictionResponse> predictSet(Map<String, Map<String, ?>> predictSetMap) {
    return predictSetMap.entrySet().stream()
        .map(entry -> this.predictSingle(entry.getKey(), entry.getValue()))
        .collect(Collectors.toSet());
  }
}
