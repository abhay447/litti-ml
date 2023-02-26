package com.litti.ml.model.predictor;

import com.litti.ml.feature.FeatureFetchRouter;
import com.litti.ml.model.entities.ModelMetadata;
import com.litti.ml.model.entities.ModelOutputMetadata;
import com.litti.ml.model.entities.PredictionRequest;
import com.litti.ml.model.entities.PredictionResponse;
import jakarta.xml.bind.JAXBException;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.EvaluatorUtil;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PMMLPredictor extends AbstractPredictor {

  static Logger logger = LogManager.getLogger(PMMLPredictor.class);
  private final ModelMetadata modelMetadata;

  // TODO: find a better abstraction for feature fetch, it should happen before model predictor
  private final FeatureFetchRouter featureFetchRouter;
  private final Evaluator evaluator;
  private final Map<String, ModelOutputMetadata> outputFieldsMap;

  public PMMLPredictor(ModelMetadata modelMetadata, FeatureFetchRouter featureFetchRouter)
      throws JAXBException, IOException, ParserConfigurationException, SAXException {
    super(modelMetadata, featureFetchRouter);
    this.modelMetadata = modelMetadata;
    this.featureFetchRouter = featureFetchRouter;
    this.evaluator =
        new LoadingModelEvaluatorBuilder().load(new File(modelMetadata.modelLocation())).build();
    evaluator.verify();

    this.outputFieldsMap =
        modelMetadata.outputs().stream()
            .map(output -> Map.entry(output.name(), output))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public PredictionResponse predictSingle(PredictionRequest input) {
    try {
      final Map<String, String> dimensions =
          input.getInputs().entrySet().stream()
              .map(entry -> Map.entry(entry.getKey(), entry.getValue().toString()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      final Map<String, ?> features =
          this.featureFetchRouter.fetchFeatures(modelMetadata.features(), dimensions);

      logger.info(new JSONObject(features));
      final Map<String, ?> outputs =
          EvaluatorUtil.decodeAll(evaluator.evaluate(features)).entrySet().stream()
              .filter(entry -> outputFieldsMap.containsKey(entry.getKey()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      if (!outputs.keySet().equals(outputFieldsMap.keySet())) {
        throw new RuntimeException(
            "Unable to retrieve all output fields, please check the "
                + "prediction request for missing params");
      }
      return new PredictionResponse(input.getId(), outputs, null);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e);
      return new PredictionResponse(input.getId(), null, e.getMessage());
    }
  }

  @Override
  public Set<PredictionResponse> predictSet(Set<PredictionRequest> inputs) {
    return inputs.stream().map(this::predictSingle).collect(Collectors.toSet());
  }
}
