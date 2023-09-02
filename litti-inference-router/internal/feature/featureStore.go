package feature

import (
	"context"
	"encoding/json"
	"errors"

	"com/litti/ml/litti-inference-router/internal/dto"

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
