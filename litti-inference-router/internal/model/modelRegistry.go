package model

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"com/litti/ml/litti-inference-router/internal/feature"
	"com/litti/ml/litti-inference-router/internal/util"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
)

var modelMap map[string]ModelDeploymentMetadata = make(map[string]ModelDeploymentMetadata)

func LoadModelRegistry() {
	resp, err := http.Get("http://localhost:8081/models")
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	var deployedModels []ModelDeploymentMetadata
	err = json.Unmarshal(respBody, &deployedModels)
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	// fmt.Printf("%+v", deployedModels)
	for _, deployedModel := range deployedModels {
		modelKey := deployedModel.Name + "#" + deployedModel.Version
		modelMap[modelKey] = deployedModel
	}
}

func EnrichModelFeatures(model string, version string, batchReq dto.BatchPredictionRequest) []dto.PredictionRequest {
	deployedModel := modelMap[model+"#"+version]
	var featureGroups map[string]bool = make(map[string]bool)
	var featureMetaMap = map[string]dto.FeatureEntry{}
	for _, feature := range deployedModel.Features {
		featureGroups[feature.FeatureGroup] = true
		featureMetaMap[feature.Name+"#"+feature.Version] = feature
	}
	reqRawFeaturesMap, err := feature.FetchRawFeatureRows(featureGroups, batchReq)
	if err != nil {
		fmt.Println(err.Error())
	}
	newReqs := []dto.PredictionRequest{}
	for _, req := range batchReq.PredictionRequests {
		req.Inputs = util.MergeMaps(req.Inputs, extractFeatureValues(reqRawFeaturesMap[req.Id], featureMetaMap))
		newReqs = append(newReqs, dto.PredictionRequest{Id: req.Id, Inputs: req.Inputs})
	}
	return newReqs
}

func extractFeatureValues(rawFeatures map[string]dto.FeatureStoreRecord, featureMetaMap map[string]dto.FeatureEntry) map[string]interface{} {
	var featureValsMap = map[string]interface{}{}
	for featureKey, featureEntry := range featureMetaMap {
		record, ok := rawFeatures[featureKey]
		featureValsMap[featureEntry.Name] = util.ParseFeatureRecordValue(record, ok, featureEntry)
	}
	return featureValsMap
}
