package server

import (
	"bytes"
	"com/litti/ml/litti-inference-router/internal/dto"
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"os"
	"strings"

	"github.com/go-playground/validator/v10"
)

func getRoot(w http.ResponseWriter, r *http.Request) {
	fmt.Printf("got / request\n")
	io.WriteString(w, "This is my website!\n")
}

func predict(w http.ResponseWriter, r *http.Request) {
	var tokens []string = strings.Split(r.URL.Path, "/")[1:]
	fmt.Printf("Tokens: %+v", tokens)
	if len(tokens) != 3 {
		http.Error(w, "invalid url spec, use <base_url>/predict/<model_name>/<model_version>", http.StatusBadRequest)
		return
	}
	var model = tokens[1]
	var version = tokens[2]
	// read request for validation
	// TODO: add feature fetch using request body
	var p dto.BatchPredictionRequest
	err := json.NewDecoder(r.Body).Decode(&p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	// validate
	validate := validator.New()
	err = validate.Struct(p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	fmt.Printf("got /predict request for model: %s version: %s\n", model, version)
	var reqBytes []byte
	r.Body.Read(reqBytes)
	var reqBodyReader = bytes.NewReader(reqBytes)
	requestURL := fmt.Sprintf("http://localhost:8001/predict/%s/%s", model, version)
	req, err := http.NewRequest(http.MethodPost, requestURL, reqBodyReader)
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
	if res.StatusCode != 200 {
		bytedata, _ := ioutil.ReadAll(res.Body)
		http.Error(w, string(bytedata), http.StatusInternalServerError)
		return
	}
	fmt.Fprintf(w, "Response: %+v", res)
	// io.WriteString(w, "Hello, HTTP!\n")
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
