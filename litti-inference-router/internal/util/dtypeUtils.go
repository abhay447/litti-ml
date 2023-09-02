package util

import (
	"com/litti/ml/litti-inference-router/internal/dto"
	"strconv"
	"time"
)

func ParseFeatureRecordValue(featureStoreRecord dto.FeatureStoreRecord, featureFound bool, featureEntry dto.FeatureEntry) interface{} {
	rawValue := featureEntry.DefaultValue
	if featureFound && featureStoreRecord.ValidTo >= int(time.Now().Unix()) {
		rawValue = featureStoreRecord.RawValue
	}
	switch featureEntry.DataType {
	case "PRIMITIVE#DOUBLE":
		val, _ := strconv.ParseFloat(rawValue, 64)
		return val
	case "PRIMITIVE#BOOL":
		val, _ := strconv.ParseBool(rawValue)
		return val
	case "PRIMITIVE#LONG":
		val, _ := strconv.ParseInt(rawValue, 10, 64)
		return val
	default:
		return rawValue
	}
}
