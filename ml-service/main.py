from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
import joblib


app = FastAPI(
    title="Smart Route Traffic Prediction API",
    version="1.0.0"
)


model = joblib.load("traffic_model.pkl")


class TrafficRequest(BaseModel):

    hour: int
    day_of_week: int
    weather: str
    road_type: str
    base_travel_time: float


@app.get("/")
def home():

    return {
        "message": "Traffic Prediction API is running"
    }


@app.post("/predict")
def predict_traffic(request: TrafficRequest):

    data = pd.DataFrame([
        {
            "hour": request.hour,
            "day_of_week": request.day_of_week,
            "weather": request.weather,
            "road_type": request.road_type,
            "base_travel_time": request.base_travel_time
        }
    ])

    prediction = model.predict(data)[0]

    return {
        "predicted_travel_time": round(
            float(prediction),
            2
        )
    }