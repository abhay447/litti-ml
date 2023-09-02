package dto

type FeatureStoreRecord struct {
	FeatureName    string
	FeatureVersion string
	RawValue       string
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
