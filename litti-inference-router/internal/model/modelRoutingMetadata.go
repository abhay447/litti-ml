package model

import "com/litti/ml/litti-inference-router/internal/dto"

type ModelDeploymentMetadata struct {
	Id       string
	Name     string
	Version  string
	Domain   string
	Outputs  []ModelOutputEntry
	Features []dto.FeatureEntry
}

type ModelOutputEntry struct {
	Name     string
	DataType string
}
