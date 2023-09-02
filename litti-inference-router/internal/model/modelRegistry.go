package model

import (
	"com/litti/ml/litti-inference-router/internal/config"
	"encoding/json"
	"io"
	"net/http"
)

var modelMap map[string]ModelDeploymentMetadata = make(map[string]ModelDeploymentMetadata)

func LoadModelRegistry() {
	resp, err := http.Get(config.RouterConfig.MGMT_SERVER_URL + "/models")
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
	// fmt.Printf("%+v", deployedModels)
	for _, deployedModel := range deployedModels {
		modelKey := deployedModel.Name + "#" + deployedModel.Version
		modelMap[modelKey] = deployedModel
	}
}
