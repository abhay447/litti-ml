package dto

type FeatureStoreRecord struct {
	FeatureName    string
	FeatureVersion string
	Value          string
	ValidFrom      int
	ValidTo        int
}

type FeatureEntry struct {
	Name         string
	Version      string
	DataType     string
	DefaultValue string
	FeatureGroup string
	TtlSeconds   int
}
