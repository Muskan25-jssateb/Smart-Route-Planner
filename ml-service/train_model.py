import pandas as pd
import random
import joblib

from sklearn.model_selection import train_test_split
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import OneHotEncoder
from sklearn.pipeline import Pipeline
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error, r2_score


random.seed(42)


# ==================================================
# 1. Generate realistic synthetic traffic dataset
# ==================================================

rows = []

weather_options = [
    "Sunny",
    "Cloudy",
    "Rain"
]

road_types = [
    "Highway",
    "Main Road",
    "Residential"
]


for _ in range(5000):

    hour = random.randint(0, 23)

    day_of_week = random.randint(0, 6)

    weather = random.choice(
        weather_options
    )

    road_type = random.choice(
        road_types
    )

    base_travel_time = random.uniform(
        5,
        30
    )


    # ----------------------------------------------
    # Base traffic factor
    # ----------------------------------------------

    traffic_factor = 1.0


    # ----------------------------------------------
    # Time of day
    # ----------------------------------------------

    if 7 <= hour <= 10:

        # Morning rush
        traffic_factor += random.uniform(
            0.30,
            0.70
        )

    elif 17 <= hour <= 20:

        # Evening rush
        traffic_factor += random.uniform(
            0.40,
            0.80
        )

    elif 0 <= hour <= 5:

        # Very low traffic
        traffic_factor += random.uniform(
            -0.10,
            0.02
        )

    else:

        # Normal traffic
        traffic_factor += random.uniform(
            0.10,
            0.25
        )


    # ----------------------------------------------
    # Weekend effect
    # ----------------------------------------------

    if day_of_week >= 5:

        traffic_factor -= random.uniform(
            0.05,
            0.15
        )


    # ----------------------------------------------
    # Weather effect
    # ----------------------------------------------

    if weather == "Rain":

        traffic_factor += random.uniform(
            0.10,
            0.30
        )

    elif weather == "Cloudy":

        traffic_factor += random.uniform(
            0.02,
            0.08
        )


    # ----------------------------------------------
    # Road type effect
    # ----------------------------------------------

    if road_type == "Highway":

        traffic_factor -= random.uniform(
            0.05,
            0.10
        )

    elif road_type == "Main Road":

        traffic_factor += random.uniform(
            0.05,
            0.15
        )

    elif road_type == "Residential":

        traffic_factor += random.uniform(
            0.10,
            0.20
        )


    # ----------------------------------------------
    # Prevent unrealistic values
    # ----------------------------------------------

    traffic_factor = max(
        0.85,
        min(
            traffic_factor,
            2.0
        )
    )


    # ----------------------------------------------
    # Small random noise
    # ----------------------------------------------

    noise = random.uniform(
        -0.05,
        0.05
    )


    travel_time = (
        base_travel_time
        * traffic_factor
        * (1 + noise)
    )


    # ----------------------------------------------
    # Traffic level label
    # ----------------------------------------------

    increase_percentage = (
        (travel_time - base_travel_time)
        / base_travel_time
    )


    if increase_percentage < 0.20:

        traffic_level = "LOW"

    elif increase_percentage < 0.50:

        traffic_level = "MEDIUM"

    else:

        traffic_level = "HIGH"


    rows.append({

        "hour": hour,

        "day_of_week": day_of_week,

        "weather": weather,

        "road_type": road_type,

        "base_travel_time":
            base_travel_time,

        "traffic_level":
            traffic_level,

        "travel_time":
            travel_time
    })


df = pd.DataFrame(rows)


# Save dataset

df.to_csv(
    "traffic_data.csv",
    index=False
)


print("Dataset created.")
print(
    f"Rows: {len(df)}"
)


# ==================================================
# 2. Features and target
# ==================================================

X = df[
    [
        "hour",
        "day_of_week",
        "weather",
        "road_type",
        "base_travel_time"
    ]
]

y = df[
    "travel_time"
]


# ==================================================
# 3. Train / test split
# ==================================================

X_train, X_test, y_train, y_test = train_test_split(

    X,
    y,

    test_size=0.20,

    random_state=42
)


# ==================================================
# 4. Preprocessing
# ==================================================

categorical_features = [

    "weather",

    "road_type"
]


numeric_features = [

    "hour",

    "day_of_week",

    "base_travel_time"
]


preprocessor = ColumnTransformer(

    transformers=[

        (
            "categorical",

            OneHotEncoder(
                handle_unknown="ignore"
            ),

            categorical_features
        ),

        (
            "numeric",

            "passthrough",

            numeric_features
        )
    ]
)


# ==================================================
# 5. Random Forest
# ==================================================

model = RandomForestRegressor(

    n_estimators=200,

    max_depth=18,

    min_samples_leaf=2,

    random_state=42,

    n_jobs=-1
)


pipeline = Pipeline(

    steps=[

        (
            "preprocessor",
            preprocessor
        ),

        (
            "model",
            model
        )
    ]
)


# ==================================================
# 6. Train
# ==================================================

print()
print("Training model...")

pipeline.fit(
    X_train,
    y_train
)


# ==================================================
# 7. Evaluate
# ==================================================

predictions = pipeline.predict(
    X_test
)


mae = mean_absolute_error(
    y_test,
    predictions
)


r2 = r2_score(
    y_test,
    predictions
)


print()
print("Model Evaluation")
print("-----------------")

print(
    f"MAE: {mae:.2f} minutes"
)

print(
    f"R2 Score: {r2:.4f}"
)


# ==================================================
# 8. Save model
# ==================================================

joblib.dump(

    pipeline,

    "traffic_model.pkl"
)


print()
print(
    "Model saved as traffic_model.pkl"
)


# ==================================================
# 9. Test sample predictions
# ==================================================

test_cases = pd.DataFrame([

    {
        "hour": 2,
        "day_of_week": 1,
        "weather": "Sunny",
        "road_type": "Highway",
        "base_travel_time": 10
    },

    {
        "hour": 9,
        "day_of_week": 1,
        "weather": "Sunny",
        "road_type": "Main Road",
        "base_travel_time": 10
    },

    {
        "hour": 18,
        "day_of_week": 1,
        "weather": "Rain",
        "road_type": "Main Road",
        "base_travel_time": 10
    }

])


test_predictions = pipeline.predict(
    test_cases
)


print()
print("Sample Predictions")
print("-------------------")


for i, prediction in enumerate(
    test_predictions
):

    print(
        f"Case {i + 1}: "
        f"{prediction:.2f} minutes"
    )