package feature

import (
	"encoding/json"
	"io"
	"net/http"

	"golang.org/x/exp/slices"
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
	// fmt.Printf("%+v", featureGroups)
	for _, featureGroup := range featureGroups {
		slices.Sort(featureGroup.Dimensions)
		featureGroupMap[featureGroup.Name] = featureGroup
	}
}
