package dto

type PredictionRequest struct {
	Id     string
	Inputs map[string]interface{}
}

type PredictionResponse struct {
	Id           string
	ErrorMessage string
	Outputs      map[string]interface{}
}

type BatchPredictionRequest struct {
	BatchPredictionId  string
	PredictionRequests []PredictionRequest
}

type BatchPredictionResponse struct {
	BatchPredictionId  string
	PredictionRequests []PredictionRequest
}
