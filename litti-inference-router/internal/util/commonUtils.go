package util

import "com/litti/ml/litti-inference-router/internal/dto"

func MergeMaps[V dto.FeatureStoreRecord | interface{}](ms ...map[string]V) map[string]V {
	res := map[string]V{}
	for _, m := range ms {
		for k, v := range m {
			res[k] = v
		}
	}
	return res
}
