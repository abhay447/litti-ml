package logging

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"fmt"
)

func LogModelBatchPrediction(batchModelLogRecord BatchModelLogRecord) {
	modelLogRecords := buildmodelLogRecords(batchModelLogRecord)
	logToConsole(modelLogRecords)
}

func buildmodelLogRecords(batchModelLogRecord BatchModelLogRecord) []ModelLogRecord {
	inputsMap := map[string]dto.PredictionRequest{}
	for _, req := range batchModelLogRecord.BatchReq.PredictionRequests {
		inputsMap[req.Id] = req
	}
	modelLogRecords := make([]ModelLogRecord, len(batchModelLogRecord.BatchRes.PredictionResponses))
	for i, predResp := range batchModelLogRecord.BatchRes.PredictionResponses {
		modelLogRecords[i] = ModelLogRecord{
			BatchPredictionId: batchModelLogRecord.BatchReq.BatchPredictionId,
			PredictionId:      predResp.Id,
			Inputs:            inputsMap[predResp.Id].Inputs,
			Outputs:           predResp.Outputs,
			ModelName:         batchModelLogRecord.ModelName,
			ModelVersion:      batchModelLogRecord.ModelVersion,
		}
	}
	return modelLogRecords
}

func logToConsole(modelLogRecords []ModelLogRecord) {
	for _, modelLogRecord := range modelLogRecords {
		fmt.Printf("%+v\n", modelLogRecord)
	}
}
