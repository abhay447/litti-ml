package feature

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"com/litti/ml/litti-inference-router/internal/util"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"strings"

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

func FetchRawFeatureRows(featureGroupSet map[string]bool, req dto.BatchPredictionRequest) (map[string]map[string]dto.FeatureStoreRecord, error) {
	featureGroupKeys := make(map[string]bool)
	reqFeatureGroupKeysMap := make(map[string][]string)
	for _, predictionReq := range req.PredictionRequests {
		if reqFeatureGroupKeysMap[predictionReq.Id] == nil {
			reqFeatureGroupKeysMap[predictionReq.Id] = []string{}
		}
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
			reqFeatureGroupKeysMap[predictionReq.Id] = append(reqFeatureGroupKeysMap[predictionReq.Id], featureGroupKey)
		}
	}
	// key os feature group key
	featureGroupsMap := make(map[string]map[string]dto.FeatureStoreRecord)
	for featureGroupKey := range featureGroupKeys {
		featureStoreRecords, err := FetchFeatureGroupRow(featureGroupKey)
		if err != nil {
			return nil, err
		}
		if featureStoreRecords != nil {
			featureGroupsMap[featureGroupKey] = featureStoreRecords
		}
	}
	// key is reqId
	// value is map[string]FeatureStoreRecord -> each entry in map represents a feature
	reqFeatureRowsMap := make(map[string]map[string]dto.FeatureStoreRecord)
	for reqId, fgKeys := range reqFeatureGroupKeysMap {
		if reqFeatureRowsMap[reqId] == nil {
			reqFeatureRowsMap[reqId] = map[string]dto.FeatureStoreRecord{}
		}
		for _, fgKey := range fgKeys {
			reqFeatureRowsMap[reqId] = util.MergeMaps(reqFeatureRowsMap[reqId], featureGroupsMap[fgKey])
		}
	}
	return reqFeatureRowsMap, nil
}
