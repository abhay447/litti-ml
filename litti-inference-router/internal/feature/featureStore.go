package feature

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"strings"

	"com/litti/ml/litti-inference-router/internal/dto"
	"com/litti/ml/litti-inference-router/internal/util"

	"github.com/redis/go-redis/v9"
)

var redisClient = redis.NewClient(&redis.Options{
	Addr:     "localhost:6379",
	Password: "", // no password set
	DB:       0,  // use default DB
})

func FetchFeatureGroupRow(featureGroupKey string) (map[string]dto.FeatureStoreRecord, error) {
	return fetchRedisFeatureGroup(featureGroupKey)
}

func fetchRedisFeatureGroup(featureGroupKey string) (map[string]dto.FeatureStoreRecord, error) {
	ctx := context.Background()
	val, err := redisClient.Get(ctx, featureGroupKey).Result()
	if err != nil {
		if errors.Is(err, redis.Nil) {
			return nil, nil
		}
		return nil, err
	}
	var records map[string]dto.FeatureStoreRecord
	err = json.Unmarshal([]byte(val), &records)
	if err != nil {
		return nil, err
	}
	return records, nil
}

/*
For each prediction request in batch request fetch the Feature Store records using feature store.
*/
func FetchRawFeatureRows(featureGroupSet map[string]bool, req dto.BatchPredictionRequest) (map[string]map[string]dto.FeatureStoreRecord, error) {
	featureGroupKeys, reqFeatureGroupKeysMap := mapFeatureGroupKeys(featureGroupSet, req)

	featureGroupRecordsMap, err := fetchFeatureGroupRecordsMap(featureGroupKeys)
	if err != nil {
		return nil, err
	}
	reqFeatureRowsMap := make(map[string]map[string]dto.FeatureStoreRecord)
	for reqId, fgKeys := range reqFeatureGroupKeysMap {
		if reqFeatureRowsMap[reqId] == nil {
			reqFeatureRowsMap[reqId] = map[string]dto.FeatureStoreRecord{}
		}
		for _, fgKey := range fgKeys {
			reqFeatureRowsMap[reqId] = util.MergeMaps(reqFeatureRowsMap[reqId], featureGroupRecordsMap[fgKey])
		}
	}
	return reqFeatureRowsMap, nil
}

/*
Iterates over the prediction request and featureGrooup set to create:
1. Set of feature group keys to be sent to feature store for querying
2. List of feature group keys required for each prediction request in batch request.
*/
func mapFeatureGroupKeys(featureGroupSet map[string]bool, req dto.BatchPredictionRequest) (map[string]bool, map[string][]string) {
	featureGroupKeys := make(map[string]bool)
	reqFeatureGroupKeysMap := make(map[string][]string)
	for _, predictionReq := range req.PredictionRequests {
		if reqFeatureGroupKeysMap[predictionReq.Id] == nil {
			reqFeatureGroupKeysMap[predictionReq.Id] = []string{}
		}
		for featureGroupName, _ := range featureGroupSet {
			featureGroup := featureGroupMap[featureGroupName]
			var prefix = []string{}
			var suffix = []string{}
			for _, dim := range featureGroup.Dimensions {
				prefix = append(prefix, dim)
				suffix = append(suffix, fmt.Sprint(predictionReq.Inputs[dim]))
			}
			featureGroupKey := strings.Join(prefix, "#") + "|" + strings.Join(suffix, "#")
			featureGroupKeys[featureGroupKey] = true
			reqFeatureGroupKeysMap[predictionReq.Id] = append(reqFeatureGroupKeysMap[predictionReq.Id], featureGroupKey)
		}
	}
	return featureGroupKeys, reqFeatureGroupKeysMap
}

/*
For each Featuregroupkey fetch the relevant map[string] -> FeatureStoreRecords by querying the feature store.
*/
func fetchFeatureGroupRecordsMap(featureGroupKeys map[string]bool) (map[string]map[string]dto.FeatureStoreRecord, error) {
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
	return featureGroupsMap, nil
}
