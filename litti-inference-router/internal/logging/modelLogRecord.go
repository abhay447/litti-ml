package logging

import "com/litti/ml/litti-inference-router/internal/dto"

type ModelLogRecord struct {
	BatchPredictionId string
	PredictionId      string
	ModelName         string
	ModelVersion      string
	Inputs            map[string]interface{}
	Outputs           map[string]interface{}
}

type BatchModelLogRecord struct {
	ModelName    string
	ModelVersion string
	BatchReq     dto.BatchPredictionRequest
	BatchRes     dto.BatchPredictionResponse
}
