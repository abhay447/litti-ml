package dto

type FeatureStoreRecord struct {
	FeatureName    string
	FeatureVersion string
	Value          string
	ValidFrom      int
	ValidTo        int
}
