package server

import (
	"bytes"
	"com/litti/ml/litti-inference-router/internal/config"
	"com/litti/ml/litti-inference-router/internal/dto"
	"com/litti/ml/litti-inference-router/internal/logging"
	"com/litti/ml/litti-inference-router/internal/model"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"net/http"
	"os"
	"strings"

	"github.com/go-playground/validator/v10"
)

func getRoot(w http.ResponseWriter, r *http.Request) {
	fmt.Printf("got / request\n")
	io.WriteString(w, "This is my website!\n")
}

func validatePath(r *http.Request) (string, string, error) {
	var tokens []string = strings.Split(r.URL.Path, "/")[1:]
	fmt.Printf("Tokens: %+v", tokens)

	if len(tokens) != 3 {
		return "", "", errors.New("invalid url spec, use <base_url>/predict/<model_name>/<model_version>")
	}
	return tokens[1], tokens[2], nil
}

func validateRequest(r *http.Request) (dto.BatchPredictionRequest, error) {
	reqBytes, _ := io.ReadAll(r.Body)
	var p dto.BatchPredictionRequest
	err := json.Unmarshal(reqBytes, &p)
	if err != nil {
		return p, err
	}
	// validate
	validate := validator.New()
	err = validate.Struct(p)
	if err != nil {
		return p, err
	}
	return p, nil
}

func createForwardingRequest(r *http.Request, modelName string, version string) (*dto.BatchPredictionRequest, []byte, error) {
	batchReq, err := validateRequest(r)
	if err != nil {
		return nil, nil, err
	}
	enrichedBatchReq := dto.BatchPredictionRequest{
		BatchPredictionId:  batchReq.BatchPredictionId,
		PredictionRequests: model.EnrichModelFeatures(modelName, version, batchReq),
	}
	batchReqJSON, err := json.Marshal(enrichedBatchReq)
	if err != nil {
		return nil, nil, err
	}
	return &enrichedBatchReq, batchReqJSON, nil
}

func validateResponse(respBytes []byte) (*dto.BatchPredictionResponse, error) {
	var p dto.BatchPredictionResponse
	err := json.Unmarshal(respBytes, &p)
	if err != nil {
		return nil, err
	}
	// validate
	validate := validator.New()
	err = validate.Struct(p)
	if err != nil {
		return nil, err
	}
	return &p, nil
}

func forwardRequestToRuntime(modelName string, version string, batchReqBytes []byte) (*dto.BatchPredictionResponse, []byte, int, error) {
	requestURL := fmt.Sprintf(config.RouterConfig.INFERENCE_SERVER_URL+"/predict/%s/%s", modelName, version)
	req, err := http.NewRequest(http.MethodPost, requestURL, bytes.NewBuffer(batchReqBytes))
	if err != nil {
		return nil, nil, 400, err
	}
	client := &http.Client{}
	res, err := client.Do(req)
	if err != nil {
		return nil, nil, 400, err
	}
	resBodyBytes, _ := io.ReadAll(res.Body)
	resStatus := res.StatusCode
	if resStatus != 200 {
		errorMsg := fmt.Sprintf("Non 200 status code: %d, body: %s", res.StatusCode, string(resBodyBytes))
		return nil, nil, 503, errors.New(errorMsg)
	}
	resp, err := validateResponse(resBodyBytes)
	if err != nil {
		return nil, nil, 503, err
	}
	return resp, resBodyBytes, 200, nil
}

func predict(w http.ResponseWriter, r *http.Request) {
	modelName, version, err := validatePath(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	fmt.Printf("got /predict request for model: %s version: %s\n", modelName, version)
	batchReq, batchReqBytes, err := createForwardingRequest(r, modelName, version)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	resp, respBytes, status, err := forwardRequestToRuntime(modelName, version, batchReqBytes)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	logging.LogModelBatchPrediction(logging.BatchModelLogRecord{
		ModelName:    modelName,
		ModelVersion: version,
		BatchReq:     *batchReq,
		BatchRes:     *resp,
	})
	w.WriteHeader(status)
	w.Write(respBytes)
}

func StartServer() {
	http.HandleFunc("/", getRoot)
	http.HandleFunc("/predict/", predict)

	err := http.ListenAndServe(config.RouterConfig.SERVER_HOST+":"+config.RouterConfig.SERVER_PORT, nil)
	if errors.Is(err, http.ErrServerClosed) {
		fmt.Printf("server closed\n")
	} else if err != nil {
		fmt.Printf("error starting server: %s\n", err)
		os.Exit(1)
	}
}
