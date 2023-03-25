from typing import Dict, Optional
import requests
import json

base_url = "http://localhost:8081"
headers = {'Content-type': 'application/json', 'Accept': 'application/json'}

# add feature store
feature_store_entity = {
    "name" : "test_redis_store",
    "storageEngine" : "redis"
}

response = requests.post(base_url + "/feature-stores", json=feature_store_entity, headers=headers)

feature_store_id = response.json()['id']

# add feature groups
feature_group_ids = {}
dream11_fg_player = {
    "name" : "dream11_fg_player",
    "dimensions" : "player_id",
    "featureStoreId" : feature_store_id
}
response = requests.post(base_url + "/feature-groups", json=dream11_fg_player, headers=headers).json()
feature_group_ids[response["name"]] = response["id"]

dream11_fg_player_date = {
    "name" : "dream11_fg_player_date",
    "dimensions" : "player_id,dt",
    "featureStoreId" : feature_store_id
}
response = requests.post(base_url + "/feature-groups", json=dream11_fg_player_date, headers=headers).json()
feature_group_ids[response["name"]] = response["id"]

dream11_fg_player_date_venue = {
    "name" : "dream11_fg_player_date_venue",
    "dimensions" : "player_id,dt,venue_name",
    "featureStoreId" : feature_store_id
}
response = requests.post(base_url + "/feature-groups", json=dream11_fg_player_date_venue, headers=headers).json()
feature_group_ids[response["name"]] = response["id"]

# add features
feature_id_map: Dict[str,str] = {}
model_meta = json.loads(open("dream11-model-meta.json").read())

for feature in model_meta["features"]:
    feature_entity: Dict[str,Optional[str]] = {
        "name": feature["name"],
        "version": feature["version"],
        "dataType": feature["dataType"],
        "defaultValue": feature["defaultValue"],
        "ttlSeconds": feature["ttlSeconds"],
        "featureGroupId": feature_group_ids[feature["featureGroup"]] if "featureGroup" in feature and len(feature["featureGroup"])>0 else None
    }
    response = requests.post(base_url + "/features", json=feature_entity, headers=headers).json()
    feature_id_map[response["name"]+"#"+response["version"]] = response["id"]

feature_ids = list(feature_id_map.values())

# add model

create_model_request = {
    "modelEntity": {
        "name" : model_meta["name"],
        "version": model_meta["version"],
        "modelLocation" : model_meta["modelLocation"],
        "modelFramework" : model_meta["modelFramework"],
        "outputs": json.dumps(model_meta["outputs"]),
    },
    "featureIds": feature_ids
}
response = requests.post(base_url + "/models", json=create_model_request, headers=headers).json()
model_id = response["id"]
print(response)

# fetch deployed model meta
db_model_deployment_metadata = requests.get(base_url + "/models-deployment-meta/"+model_id,headers=headers).json()
print(json.dumps(db_model_deployment_metadata))

# assert feature match
assert [i for i in model_meta["features"] if i not in db_model_deployment_metadata["features"]] == []

