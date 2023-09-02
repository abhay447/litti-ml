package model

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"com/litti/ml/litti-inference-router/internal/feature"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strconv"
	"time"
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
	fmt.Printf("%+v", deployedModels)
	for _, deployedModel := range deployedModels {
		modelKey := deployedModel.Name + "#" + deployedModel.Version
		modelMap[modelKey] = deployedModel
	}
}

func EnrichModelFeatures(model string, version string, batchReq dto.BatchPredictionRequest) []dto.PredictionRequest {
	deployedModel := modelMap[model+"#"+version]
	var featureGroups map[string]bool = make(map[string]bool)
	var featureMetaMap = map[string]FeatureEntry{}
	for _, feature := range deployedModel.Features {
		featureGroups[feature.FeatureGroup] = true
		featureMetaMap[feature.Name+"#"+feature.Version] = feature
	}
	reqRawFeaturesMap, err := feature.FetchRawFeatureRows(featureGroups, batchReq)
	if err == nil {
		fmt.Println(err.Error())
	}
	newReqs := []dto.PredictionRequest{}
	for _, req := range batchReq.PredictionRequests {
		req.Inputs = feature.MergeMaps(req.Inputs, extractFeatureValues(reqRawFeaturesMap[req.Id], featureMetaMap))
		newReqs = append(newReqs, dto.PredictionRequest{Id: req.Id, Inputs: req.Inputs})
	}
	return newReqs
}

func extractFeatureValues(rawFeatures map[string]feature.FeatureStoreRecord, featureMetaMap map[string]FeatureEntry) map[string]interface{} {
	var featureValsMap = map[string]interface{}{}
	for featureKey, featureEntry := range featureMetaMap {
		record, ok := rawFeatures[featureKey]
		featureValsMap[featureEntry.Name] = parseFeatureRecordValue(record, ok, featureEntry)
	}
	return featureValsMap
}

func parseFeatureRecordValue(featureStoreRecord feature.FeatureStoreRecord, featureFound bool, featureEntry FeatureEntry) interface{} {
	rawValue := featureEntry.DefaultValue
	if featureFound && featureStoreRecord.ValidTo < int(time.Now().Unix()) {
		rawValue = featureStoreRecord.Value
	}
	switch featureEntry.DataType {
	case "PRIMITIVE#DOUBLE":
		val, _ := strconv.ParseFloat(rawValue, 64)
		return val
	case "PRIMITIVE#BOOL":
		val, _ := strconv.ParseBool(rawValue)
		return val
	case "PRIMITIVE#LONG":
		val, _ := strconv.ParseInt(rawValue, 10, 64)
		return val
	default:
		return rawValue
	}
}
