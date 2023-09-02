package config

import "os"

type RouterConfigStruct struct {
	SERVER_HOST          string
	SERVER_PORT          string
	MGMT_SERVER_URL      string
	INFERENCE_SERVER_URL string
	REDIS_URL            string
}

var RouterConfig RouterConfigStruct = RouterConfigStruct{
	MGMT_SERVER_URL:      getenv("MGMT_SERVER_URL", "http://localhost:8081"),
	INFERENCE_SERVER_URL: getenv("INFERENCE_SERVER_URL", "http://localhost:8001"),
	REDIS_URL:            getenv("REDIS_URL", "localhost:6379"),
	SERVER_HOST:          getenv("SERVER_HOST", "0.0.0.0"),
	SERVER_PORT:          getenv("SERVER_PORT", "3333"),
}

func getenv(key, fallback string) string {
	value := os.Getenv(key)
	if len(value) == 0 {
		return fallback
	}
	return value
}
