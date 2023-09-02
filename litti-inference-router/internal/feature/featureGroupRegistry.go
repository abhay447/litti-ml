package feature

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strings"

	"golang.org/x/exp/slices"
	"k8s.io/utils/strings/slices"
)

var featureGroupMap = make(map[string]FeatureGroupMetadata)

func LoadModelRegistry() {
	resp, err := http.Get("http://localhost:8081/feature-groups")
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	respBody, err := io.ReadAll(resp.Body)
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	var featureGroups []FeatureGroupMetadata
	err = json.Unmarshal(respBody, &featureGroups)
	if err != nil {
		panic("Error occured in loading model metadata: " + err.Error())
	}
	fmt.Printf("%+v", featureGroups)
	for _, featureGroup := range featureGroups {
		slices.Sort(featureGroup.Dimensions)
		featureGroupMap[featureGroup.Name] = featureGroup
	}
}

func FetchFeatureGroupRows(featureGroupSet map[string]bool, req dto.BatchPredictionRequest) {
	featureGroupKeys := make(map[string]bool)
	for _, predictionReq := range req.PredictionRequests {
		for featureGroupName, _ := range featureGroupSet {
			featureGroup := featureGroupMap[featureGroupName]
			prefix := make([]string, len(featureGroup.Dimensions))
			suffix := make([]string, len(featureGroup.Dimensions))
			for _, dim := range featureGroup.Dimensions {
				prefix = append(prefix, dim)
				suffix = append(suffix, fmt.Sprint(predictionReq.Inputs[dim]))
			}
			featureGroupKey := strings.Join(prefix, "#") + "-" + strings.Join(suffix, "#")
			featureGroupKeys[featureGroupKey] = true
		}
	}
}
