# 🚗 Smart Route Planner

An AI-powered traffic-aware route optimization system built with React, Spring Boot, Machine Learning, FastAPI, and MySQL.

The application recommends efficient routes by combining a road-network graph with Machine Learning-based travel-time prediction. Instead of selecting routes only by physical distance, the system considers traffic conditions influenced by time, day, weather, road type, and base travel time.

The system uses a Random Forest Regression model to predict travel time for individual roads and Dijkstra's shortest-path algorithm to determine the route with the lowest predicted travel time.

The application also provides alternative route comparison, traffic classification, route savings, and route visualization through a React-based interface.

---

## Features

### Traffic-Aware Route Planning

- Source and destination selection
- Traffic-aware route calculation
- Dijkstra-based shortest-path optimization
- Travel-time-based route selection
- Distance calculation
- Base travel-time calculation
- Predicted travel time
- Traffic-level classification
- Weather-aware prediction
- Time-aware prediction
- Day-of-week traffic consideration

### Alternative Route Comparison

- Alternative route generation
- Multiple route discovery
- Route ranking
- Distance comparison
- Predicted travel-time comparison
- Recommended route identification
- Estimated time savings
- Alternative route visualization

### Machine Learning Traffic Prediction

- Random Forest Regression
- Travel-time prediction
- Weather-based prediction
- Road-type-based prediction
- Hour-based prediction
- Day-of-week prediction
- Base travel-time consideration
- One-Hot Encoding
- Model evaluation using MAE and R²
- Saved trained model using Joblib
- FastAPI ML microservice

### Route Analytics

- Total route distance
- Base travel time
- Estimated travel time
- Traffic increase percentage
- Traffic-level classification
- Weather information
- Route savings
- Alternative route comparison

### Frontend

- React + Vite interface
- Route planning form
- Weather selection
- Hour selection
- Day selection
- Recommended route display
- Alternative route cards
- Traffic indicators
- Route statistics
- Route visualization
- Responsive design
- Loading states
- Error handling

---

## Tech Stack

### Backend

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- REST APIs
- Maven

### Frontend

- React
- JavaScript
- Vite
- CSS
- Fetch API

### Machine Learning

- Python
- FastAPI
- Pandas
- NumPy
- Scikit-learn
- Random Forest Regression
- Joblib

### Database

- MySQL

### Algorithms

- Dijkstra's Shortest Path
- DFS-based Alternative Route Discovery
- Graph-based Route Optimization

### Tools & Development

- Git & GitHub
- IntelliJ IDEA
- VS Code
- Postman
- npm
- Uvicorn

---

## System Architecture

React Frontend
        |
        | REST API
        v
Spring Boot Backend
        |
        +-----------------------+
        |                       |
        v                       v
MySQL Database          Route Optimization
                                |
                                v
                         Graph Construction
                                |
                                v
                       Traffic Prediction
                                |
                                v
                        Dijkstra Algorithm
                                |
                                v
                       Recommended Route
                                |
                                v
                       Alternative Routes
                                |
                                v
                         React Frontend

Spring Boot Backend
        |
        | Prediction Request
        v
FastAPI ML Service
        |
        v
Random Forest Model
        |
        v
Predicted Travel Time
        |
        v
Spring Boot Backend

---

## Route Optimization Flow

User Input

Source
Destination
Weather
Hour
Day

        |
        v

React Frontend

        |
        v

Spring Boot Backend

        |
        v

Road Network

        |
        v

For every road:

Base Travel Time
Weather
Hour
Day
Road Type

        |
        v

FastAPI ML Service

        |
        v

Predicted Travel Time

        |
        v

Traffic-Aware Graph

        |
        v

Dijkstra Algorithm

        |
        v

Recommended Route

        |
        v

Alternative Routes

        |
        v

Route Comparison

        |
        v

React Dashboard

---

## How It Works

### Machine Learning Traffic Prediction

The ML model predicts the expected travel time of individual roads using:

- Hour
- Day of week
- Weather
- Road type
- Base travel time

The model uses a Random Forest Regressor.

The prediction process is:

Road Information
        |
        +---- Hour
        +---- Day of Week
        +---- Weather
        +---- Road Type
        +---- Base Travel Time
        |
        v
One-Hot Encoding
        |
        v
Random Forest Model
        |
        v
Predicted Travel Time

### Traffic-Aware Graph

Each road is represented as an edge in the graph.

The predicted travel time is used as the traffic-aware edge weight.

Road
        |
        +---- Distance
        +---- Base Travel Time
        +---- Predicted Travel Time
        |
        v
Graph Edge Weight

### Dijkstra Route Optimization

Dijkstra's algorithm uses the traffic-aware edge weights to find the route with the lowest predicted travel time.

Traditional routing:

Shortest Distance
        |
        v
Shortest Route

Smart Route Planner:

Road Information
        |
        v
ML Traffic Prediction
        |
        v
Predicted Travel Time
        |
        v
Dijkstra Algorithm
        |
        v
Fastest Traffic-Aware Route

### Alternative Routes

The system also discovers alternative routes and compares them using predicted travel time.

Example:

Recommended Route

A → E → D → H

Predicted Time: 20.4 minutes

Alternative Route

A → B → D → H

Predicted Time: 22.9 minutes

Alternative Route

A → C → D → H

Predicted Time: 23.7 minutes

The frontend displays the alternative routes along with their distance and predicted travel time.

---

## Machine Learning Model

The traffic prediction component uses a Random Forest Regression model to estimate travel time for individual roads.

### Model Features

The model receives five input features:

hour
day_of_week
weather
road_type
base_travel_time

### Target Variable

travel_time

### Preprocessing

Categorical features are processed using One-Hot Encoding.

The trained model is saved as:

ml-service/traffic_model.pkl

### Training Dataset

The current model is trained using a generated traffic dataset containing:

5,000 records

The generated dataset simulates traffic behavior under different:

- Time conditions
- Weekday and weekend conditions
- Weather conditions
- Road types
- Base travel times

---

## Model Evaluation

The current trained model achieved:

| Metric | Result |
|---|---:|
| Mean Absolute Error (MAE) | 1.50 minutes |
| R² Score | 0.9669 |

### Mean Absolute Error

The model has an average prediction error of approximately 1.50 minutes on the generated test set.

### R² Score

The model achieved an R² score of 0.9669 on the generated test set.

This indicates a strong fit on the synthetic evaluation dataset.

> Note: The current dataset is synthetically generated for project development and demonstration. These metrics should not be interpreted as real-world traffic prediction accuracy.

---

## Traffic Classification

Traffic level is determined by comparing predicted travel time with base travel time.

Traffic Increase < 20%

        |
        v

LOW

Traffic Increase 20% - 50%

        |
        v

MEDIUM

Traffic Increase > 50%

        |
        v

HIGH

Example:

Base Travel Time: 14 minutes
Predicted Travel Time: 20.4 minutes

Traffic Increase:

(20.4 - 14) / 14 × 100

≈ 45.7%

Result:

MEDIUM TRAFFIC

---

## REST APIs

### Route API

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/routes | Calculate the optimal route |

### Route Request

    {
      "source": "A",
      "destination": "H",
      "weather": "Rain",
      "hour": 18,
      "dayOfWeek": 1
    }

### Route Response

    {
      "source": "A",
      "destination": "H",
      "path": [
        "A",
        "E",
        "D",
        "H"
      ],
      "totalDistance": 13.0,
      "estimatedTravelTime": 27.17,
      "trafficLevel": "HIGH",
      "weather": "Rain"
    }

The route response can also include additional route analytics such as:

- Base travel time
- Traffic increase percentage
- Alternative routes
- Alternative route travel time
- Route savings

---

## Machine Learning API

The Machine Learning model is exposed through a FastAPI microservice.

### Health Check

    GET /

### Traffic Prediction

    POST /predict

### Prediction Request

    {
      "hour": 18,
      "day_of_week": 1,
      "weather": "Rain",
      "road_type": "Main Road",
      "base_travel_time": 5
    }

### Prediction Response

    {
      "predicted_travel_time": 7.62
    }

---

## Screenshots

### Route Planning Dashboard

![Route Planning Dashboard](docs/screenshots/home.png)

### Recommended Route

![Recommended Route](docs/screenshots/recommended-route.png)

### Alternative Routes

![Alternative Routes](docs/screenshots/alternative-routes.png)

### Route Map

![Route Map](docs/screenshots/map.png)

---

## Project Structure

Smart-Route-Planner/

├── backend/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/
│           │       └── smartroute/
│           │           └── backend/
│           │               ├── algorithm/
│           │               │   └── DijkstraAlgorithm.java
│           │               ├── controller/
│           │               │   └── RouteController.java
│           │               ├── model/
│           │               │   ├── Location.java
│           │               │   ├── Road.java
│           │               │   ├── Graph.java
│           │               │   ├── RouteRequest.java
│           │               │   ├── RouteResponse.java
│           │               │   ├── RouteResult.java
│           │               │   └── AlternativeRoute.java
│           │               ├── repository/
│           │               └── service/
│           │                   ├── GraphService.java
│           │                   ├── RouteService.java
│           │                   └── TrafficPredictionService.java
│           └── resources/
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   └── MapView.jsx
│   │   ├── App.jsx
│   │   └── index.css
│   ├── package.json
│   └── vite.config.js
│
├── ml-service/
│   ├── main.py
│   ├── train_model.py
│   ├── traffic_model.pkl
│   ├── traffic_data.csv
│   └── requirements.txt
│
├── .gitignore
├── requirements.txt
└── README.md

---

## Running the Project

The application consists of three locally running services:

React Frontend      → Port 5173
Spring Boot Backend → Port 8080
FastAPI ML Service  → Port 8000

### 1. Database

Create the required MySQL database and configure the Spring Boot database connection.

The backend stores:

- Locations
- Roads
- Road distances
- Base travel times
- Road types

### 2. Machine Learning Service

Navigate to the ML service:

    cd ml-service

Create a virtual environment:

    python -m venv venv

Activate the environment on Windows:

    .\venv\Scripts\Activate.ps1

Install dependencies:

    pip install -r requirements.txt

Train the model:

    python train_model.py

Start FastAPI:

    uvicorn main:app --reload --port 8000

The ML service runs at:

http://localhost:8000

### 3. Spring Boot Backend

Open the backend project in IntelliJ IDEA and run:

    BackendApplication

The backend runs at:

http://localhost:8080

The backend communicates with the ML service through:

http://localhost:8000/predict

### 4. React Frontend

Navigate to the frontend:

    cd frontend

Install dependencies:

    npm install

Start the development server:

    npm run dev

The frontend runs at:

http://localhost:5173

---

## Local Service Architecture

React Frontend
localhost:5173
        |
        v
Spring Boot Backend
localhost:8080
        |
        +--------------------+
        |                    |
        v                    v
MySQL Database       FastAPI ML Service
                     localhost:8000
                            |
                            v
                   Random Forest Model
                            |
                            v
                   Predicted Travel Time

---

## Testing

The application has been tested across different route and traffic conditions.

### Weather Conditions

- Sunny
- Cloudy
- Rain

### Time Conditions

- Normal hours
- Morning rush hours
- Evening rush hours
- Late night

### Route Combinations

- A → H
- A → G
- A → D
- B → H
- C → H

### ML Testing

The same route was tested under different:

- Weather conditions
- Hours
- Days of the week

The predicted travel time changes according to the supplied traffic conditions.

### Validation Testing

The application handles invalid selections such as:

Source = Destination

and displays an appropriate validation message.

### End-to-End Testing

The following components have been tested together:

- React frontend
- Spring Boot backend
- FastAPI ML service
- MySQL database
- Route calculation
- ML prediction
- Alternative route generation
- Route visualization

---

## Reliability & Error Handling

### ML Service

The Spring Boot backend communicates with the FastAPI ML service for travel-time prediction.

If the ML service is unavailable, the prediction request fails with an appropriate backend error rather than silently returning an incorrect prediction.

### Route Validation

Invalid route requests are handled by the backend and frontend.

### Frontend Error Handling

The React application displays user-friendly error messages when:

- Source is missing
- Destination is missing
- Backend is unavailable
- Route calculation fails

---

## Deployment

The application is designed as a multi-service architecture.

### Frontend

The React/Vite frontend can be deployed using:

Vercel

### Backend

The Spring Boot backend can be containerized using:

Docker

and deployed using:

Render

### Machine Learning Service

The FastAPI ML service can be deployed independently as a Python web service.

### Database

The MySQL database can be deployed using a managed MySQL provider.

### Production Architecture

User
 |
 v
React + Vite
 |
 v
Spring Boot Backend
 |
 +----------------------+
 |                      |
 v                      v
MySQL Database     FastAPI ML Service
                         |
                         v
                  Random Forest Model
                         |
                         v
                 Predicted Travel Time

---

## Environment Configuration

Production configuration should be provided through environment variables rather than hardcoded credentials.

Typical configuration includes:

DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
ML_SERVICE_URL
VITE_API_URL

Sensitive credentials should never be committed to GitHub.

---

## Security Notes

- Database credentials should be stored using environment variables.
- API URLs should be configurable through environment variables.
- `.env` files should not be committed.
- Virtual environments should not be committed.
- Generated build files should not be committed.
- API keys and passwords should never be hardcoded into source code.

---

## Current Intelligent Features

The application combines multiple layers of route intelligence.

### Graph Algorithms

Dijkstra's algorithm determines the route with the lowest traffic-aware travel time.

### Machine Learning

The Random Forest model predicts travel time based on:

- Time
- Day
- Weather
- Road Type
- Base Travel Time

### Route Comparison

Alternative routes are compared using:

- Distance
- Base travel time
- Predicted travel time
- Traffic increase
- Route savings

### Visualization

The React interface presents:

- Recommended route
- Alternative routes
- Traffic information
- Weather information
- Distance statistics
- Estimated travel time
- Route savings
- Route map

---

## Future Improvements

- Real-time traffic data integration
- Real-world road network data
- OpenStreetMap integration
- Live weather API
- GPS-based current location
- Dynamic rerouting
- Historical traffic datasets
- Real-time accident detection
- Deep Learning-based traffic prediction
- Traffic congestion forecasting
- User accounts
- Saved routes
- Route history
- Mobile application
- ETA notifications
- Automated model retraining
- Model performance monitoring
- CI/CD pipeline

---

## Future Goal

The goal of Smart Route Planner is to evolve a basic shortest-path system into an intelligent route optimization platform capable of making routing decisions using real-world traffic conditions.

The project demonstrates the integration of:

Full-Stack Development
        +
Graph Algorithms
        +
Machine Learning
        +
REST APIs
        +
Database Management
        +
Traffic-Aware Optimization
        +
Interactive Visualization

to provide users with smarter and more practical route recommendations.

---

## Author

**Muskan**

Computer Science & Engineering (AIML)

---

## License

This project is created for educational and portfolio purposes.