package server

import (
	"bytes"
	"com/litti/ml/litti-inference-router/internal/dto"
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

func validateRequest(r *http.Request) ([]byte, error) {
	reqBytes, _ := io.ReadAll(r.Body)
	var p dto.BatchPredictionRequest
	err := json.Unmarshal(reqBytes, &p)
	if err != nil {
		return []byte{}, err
	}
	// validate
	validate := validator.New()
	err = validate.Struct(p)
	if err != nil {
		return []byte{}, err
	}
	return reqBytes, nil
}

func predict(w http.ResponseWriter, r *http.Request) {
	model, version, err := validatePath(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	// read request for validation
	// TODO: add feature fetch using request body
	reqBytes, err := validateRequest(r)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	fmt.Printf("got /predict request for model: %s version: %s\n", model, version)
	requestURL := fmt.Sprintf("http://localhost:8001/predict/%s/%s", model, version)
	req, err := http.NewRequest(http.MethodPost, requestURL, bytes.NewReader(reqBytes))
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	client := &http.Client{}
	res, err := client.Do(req)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
	resBodyBytes, _ := io.ReadAll(res.Body)
	resBody := string(resBodyBytes)
	resStatus := res.StatusCode
	if resStatus != 200 {
		http.Error(w, resBody, http.StatusInternalServerError)
		return
	}
	w.WriteHeader(resStatus)
	w.Write(resBodyBytes)
}

func StartServer() {
	http.HandleFunc("/", getRoot)
	http.HandleFunc("/predict/", predict)

	err := http.ListenAndServe(":3333", nil)
	if errors.Is(err, http.ErrServerClosed) {
		fmt.Printf("server closed\n")
	} else if err != nil {
		fmt.Printf("error starting server: %s\n", err)
		os.Exit(1)
	}
}
