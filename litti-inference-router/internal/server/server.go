package server

import (
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
func getHello(w http.ResponseWriter, r *http.Request) {
	fmt.Printf("got /hello request\n")
	io.WriteString(w, "Hello, HTTP!\n")
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
	fmt.Fprintf(w, "Request: %+v", p)
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
