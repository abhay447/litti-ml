package main

import (
	"com/litti/ml/litti-inference-router/internal/feature"
	"com/litti/ml/litti-inference-router/internal/model"
	"com/litti/ml/litti-inference-router/internal/server"
	"fmt"
)

func main() {
	fmt.Println("Hello, world.")
	feature.LoadModelRegistry()
	model.LoadModelRegistry()
	server.StartServer()
}
