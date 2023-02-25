package com.litti.ml.predictor;

import com.litti.ml.entities.ModelMetadata;
import com.litti.ml.entities.ModelOutputMetadata;
import com.litti.ml.entities.PredictionRequest;
import com.litti.ml.entities.PredictionResponse;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;

public class PMMLPredictor extends AbstractPredictor {

  private final ModelMetadata modelMetadata;
  private final Evaluator evaluator;
  private final Map<String, ModelOutputMetadata> outputFieldsMap;
  static Logger logger = LogManager.getLogger(PMMLPredictor.class);

  public PMMLPredictor(ModelMetadata modelMetadata)
      throws JAXBException, IOException, ParserConfigurationException, SAXException {
    super(modelMetadata);
    this.modelMetadata = modelMetadata;
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
      final Map<String, ?> outputs =
          EvaluatorUtil.decodeAll(evaluator.evaluate(input.getInputs())).entrySet().stream()
              .filter(entry -> outputFieldsMap.containsKey(entry.getKey()))
              .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
      if(!outputs.keySet().equals(outputFieldsMap.keySet())) {
        throw new RuntimeException("Unable to retrieve all output fields, please check the " +
                "prediction request for missing params");
      }
      return new PredictionResponse(input.getId(), outputs, null);
    } catch (Exception e) {
      logger.error(e);
      return new PredictionResponse(input.getId(), null, e.getMessage());
    }
  }

  @Override
  public Set<PredictionResponse> predictSet(Set<PredictionRequest> inputs) {
    return inputs.stream().map(entry -> predictSingle(entry)).collect(Collectors.toSet());
  }
}
