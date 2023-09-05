from typing import Any, Dict, List


class PredictionRequest:
    id: str
    inputs:Dict[str,Any]
    def __init__(self, req_json:Dict[str,Any]):
        self.id = req_json["id"]
        self.inputs = req_json["inputs"]
    def encode(self):
        return self.__dict__


class BatchPredictionRequest:
    batchPredictionId: str
    predictionRequests:List[PredictionRequest]
    def __init__(self, req_json: Dict[str,Any]):
        self.batchPredictionId = req_json["batchPredictionId"]
        self.predictionRequests = [PredictionRequest(j) for j in req_json["predictionRequests"]]
    def encode(self):
        return self.__dict__

class PredictionResponse:
    id: str
    outputs:Dict[str,Any]
    def __init__(self, id:str,outputs:Dict[str,Any]):
        self.id = id
        self.outputs = outputs
    def encode(self):
        return self.__dict__

class BatchPredictionResponse:
    batchPredictionId: str
    predictionResponses:List[PredictionResponse]
    def __init__(self, batchPredictionId:str,predictionResponses:List[PredictionResponse]):
        self.batchPredictionId = batchPredictionId
        self.predictionResponses = predictionResponses
    def encode(self):
        return self.__dict__
