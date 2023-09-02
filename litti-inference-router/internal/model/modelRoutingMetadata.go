package model

type ModelDeploymentMetadata struct {
	Id       string
	Name     string
	Version  string
	Domain   string
	Outputs  []ModelOutputEntry
	Features []FeatureEntry
}

type ModelOutputEntry struct {
	Name     string
	DataType string
}

type FeatureEntry struct {
	Name         string
	Version      string
	DataType     string
	DefaultValue string
	FeatureGroup string
	TtlSeconds   int
}
