package dto

type PredictionRequest struct {
	Id     string                 `json:"id"    validate:"required"`
	Inputs map[string]interface{} `json:"inputs"    validate:"required"`
}

type PredictionResponse struct {
	Id           string                 `json:"id"    validate:"required"`
	ErrorMessage string                 `json:"errorMessage"    validate:"required"`
	Outputs      map[string]interface{} `json:"outputs"    validate:"required"`
}

type BatchPredictionRequest struct {
	BatchPredictionId  string              `json:"batchPredictionId"    validate:"required"`
	PredictionRequests []PredictionRequest `json:"predictionRequests"    validate:"required"`
}

type BatchPredictionResponse struct {
	BatchPredictionId   string               `json:"batchPredictionId"    validate:"required"`
	PredictionResponses []PredictionResponse `json:"predictionResponses"    validate:"required"`
}
