from flask import Flask, request
import json
import dto
import tweetnlp # type: ignore
import os

app = Flask(__name__)
model:tweetnlp.TopicClassification = tweetnlp.load_model('topic_classification',multi_label=False) # type: ignore

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/predict/<modelName>/<modelVersion>',methods=['POST'])
def predict(modelName:str,modelVersion:str):
    req = request.get_json()
    batchPredictReq = dto.BatchPredictionRequest(req)
    predictionResponses = []
    for predReq in batchPredictReq.predictionRequests:
        predRes = dto.PredictionResponse(
            predReq.id,
            model.topic(predReq.inputs["tweet"]) # type: ignore
        ) 
        predictionResponses.append(predRes)
    batchPredictRes = dto.BatchPredictionResponse(
        batchPredictReq.batchPredictionId,
        predictionResponses
    )

    return json.dumps(batchPredictRes,default=lambda o: o.encode())

port = int(os.environ.get('PORT', 5000))
app.run(debug=True, host='0.0.0.0', port=port)