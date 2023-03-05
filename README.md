# Litti-ML

Litti-ML is an extensible ML inference engine, which natively understands model and feature metadata in inference
context.

## High Level Prediction flow

![Diagram](docs/resources/generated-images/prediction-flow.png)

1. Request is sent from client to API Server, which sends it model registry.
2. Model Registry picks a relevant ModelPredictionManager based on request path for relevant model name and version.
3. ModelPredictionManager firstly send requst to a FeatureFetchRouter which has 3 main responsibilities:
    1. Extract Features from feature store
    2. Override feature store fetched features with any value for same feature supplied by input
    3. Add default values for any feature that was not fetched from feature store and not present in request inputs.
4. The final featureRow value its then returned to ModelPredictionManager which forwards it to an appropriate
   ModelPredictor.
5. ModelPredictor uses the trained model to transform the featureRow into predictions and returns outputs to
   ModelPredictionManager.
6. ModelPredictionManager logs predictions and featureRow and returns predictions back through prediction call stack
   finally to the client.

## Goals:

1. Define specifications and add examples for multi-framework inference support for most jvm based frameworks.
2. Define specifications and add examples for multi-framework inference support for non-jvm based frameworks.
3. Define specifications for an extensible integration of inference feature stores.
4. Define specifications for model and feature store inference telemetry for platform engineers.