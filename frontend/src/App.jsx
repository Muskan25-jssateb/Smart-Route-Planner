import { useEffect, useState } from "react";
import "./index.css";
import MapView from "./components/MapView";

function App() {
    const [source, setSource] = useState("");
    const [destination, setDestination] = useState("");
    const [weather, setWeather] = useState("Sunny");
    const [hour, setHour] = useState(12);
    const [dayOfWeek, setDayOfWeek] = useState(1);

    const [locations, setLocations] = useState([]);
    const [route, setRoute] = useState(null);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchLocations = async () => {
            try {
                const response = await fetch(
                    `${import.meta.env.VITE_API_URL}/api/locations`
                );

                if (!response.ok) {
                    throw new Error("Unable to load locations.");
                }

                const data = await response.json();

                setLocations(data);
            } catch (err) {
                setError(err.message);
            }
        };

        fetchLocations();
    }, []);

    const findRoute = async () => {
        if (!source || !destination) {
            setError("Please select both source and destination.");
            return;
        }

        if (source === destination) {
            setError("Source and destination must be different.");
            return;
        }

        setLoading(true);
        setError("");
        setRoute(null);

        try {
            const response = await fetch(
                `${import.meta.env.VITE_API_URL}/api/routes`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        source,
                        destination,
                        weather,
                        hour: Number(hour),
                        dayOfWeek: Number(dayOfWeek)
                    })
                }
            );

            if (!response.ok) {
                throw new Error(
                    "Unable to calculate the route."
                );
            }

            const data = await response.json();

            setRoute(data);

        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const getTrafficClass = () => {
        if (!route) {
            return "";
        }

        if (route.trafficLevel === "LOW") {
            return "traffic-low";
        }

        if (route.trafficLevel === "MEDIUM") {
            return "traffic-medium";
        }

        return "traffic-high";
    };

    const getTimeSaved = () => {
        if (
            !route ||
            !route.alternatives ||
            route.alternatives.length === 0
        ) {
            return 0;
        }

        const fastestAlternative =
            route.alternatives[0];

        return (
            Number(
                fastestAlternative.estimatedTravelTime
            ) -
            Number(
                route.estimatedTravelTime
            )
        );
    };

    return (
        <div className="app">

            {/* HEADER */}

            <header className="header">

                <div className="header-content">

                    <div>

                        <h1>
                            Smart Route Planner
                        </h1>

                        <p>
                            AI-powered traffic-aware
                            route optimization
                        </p>

                    </div>

                    <div className="header-badge">
                        AI + Dijkstra
                    </div>

                </div>

            </header>


            <main className="container">

                {/* ROUTE INPUT */}

                <section className="route-card">

                    <div className="section-heading">

                        <div>

                            <h2>
                                Plan Your Route
                            </h2>

                            <p>
                                Choose your locations and
                                travel conditions.
                            </p>

                        </div>

                    </div>


                    {/* SOURCE + DESTINATION */}

                    <div className="input-row">

                        <div className="input-group">

                            <label>
                                Source
                            </label>

                            <select
                                value={source}
                                onChange={(e) =>
                                    setSource(
                                        e.target.value
                                    )
                                }
                            >

                                <option value="">
                                    Select source
                                </option>

                                {locations.map(
                                    (location) => (

                                        <option
                                            key={location.id}
                                            value={
                                                location.name
                                            }
                                        >
                                            {location.name}
                                        </option>

                                    )
                                )}

                            </select>

                        </div>


                        <div className="input-group">

                            <label>
                                Destination
                            </label>

                            <select
                                value={destination}
                                onChange={(e) =>
                                    setDestination(
                                        e.target.value
                                    )
                                }
                            >

                                <option value="">
                                    Select destination
                                </option>

                                {locations.map(
                                    (location) => (

                                        <option
                                            key={location.id}
                                            value={
                                                location.name
                                            }
                                        >
                                            {location.name}
                                        </option>

                                    )
                                )}

                            </select>

                        </div>

                    </div>


                    {/* CONDITIONS */}

                    <div className="input-row">

                        <div className="input-group">

                            <label>
                                Weather
                            </label>

                            <select
                                value={weather}
                                onChange={(e) =>
                                    setWeather(
                                        e.target.value
                                    )
                                }
                            >

                                <option value="Sunny">
                                    ☀️ Sunny
                                </option>

                                <option value="Cloudy">
                                    ☁️ Cloudy
                                </option>

                                <option value="Rain">
                                    🌧️ Rain
                                </option>

                            </select>

                        </div>


                        <div className="input-group">

                            <label>
                                Travel Hour
                            </label>

                            <input
                                type="number"
                                min="0"
                                max="23"
                                value={hour}
                                onChange={(e) =>
                                    setHour(
                                        e.target.value
                                    )
                                }
                            />

                        </div>


                        <div className="input-group">

                            <label>
                                Day
                            </label>

                            <select
                                value={dayOfWeek}
                                onChange={(e) =>
                                    setDayOfWeek(
                                        e.target.value
                                    )
                                }
                            >

                                <option value="0">
                                    Monday
                                </option>

                                <option value="1">
                                    Tuesday
                                </option>

                                <option value="2">
                                    Wednesday
                                </option>

                                <option value="3">
                                    Thursday
                                </option>

                                <option value="4">
                                    Friday
                                </option>

                                <option value="5">
                                    Saturday
                                </option>

                                <option value="6">
                                    Sunday
                                </option>

                            </select>

                        </div>

                    </div>


                    <button
                        className="route-button"
                        onClick={findRoute}
                        disabled={loading}
                    >

                        {loading
                            ? "Finding Best Route..."
                            : "Find Best Route"}

                    </button>


                    {error && (

                        <div className="error">
                            {error}
                        </div>

                    )}

                </section>


                {/* RESULTS */}

                {route && (

                    <section className="result-card">

                        {/* RESULT HEADER */}

                        <div className="result-header">

                            <div>

                                <h2>
                                    Recommended Route
                                </h2>

                                <p>
                                    AI-optimized for current
                                    travel conditions
                                </p>

                            </div>

                            <div
                                className={`traffic-badge ${getTrafficClass()}`}
                            >

                                {route.trafficLevel}
                                {" "}TRAFFIC

                            </div>

                        </div>


                        {/* BEST ROUTE */}

                        <div className="route-path">

                            {route.path.map(
                                (location, index) => (

                                    <span
                                        key={index}
                                        className="route-location"
                                    >

                                        {location}

                                        {index <
                                            route.path.length - 1 && (

                                            <span className="arrow">
                                                →
                                            </span>

                                        )}

                                    </span>

                                )
                            )}

                        </div>


                        {/* STATS */}

                        <div className="stats">

                            <div className="stat">

                                <span>
                                    Distance
                                </span>

                                <strong>
                                    {Number(
                                        route.totalDistance
                                    ).toFixed(1)} km
                                </strong>

                            </div>


                            <div className="stat">

                                <span>
                                    Normal Time
                                </span>

                                <strong>
                                    {Number(
                                        route.baseTravelTime
                                    ).toFixed(1)} min
                                </strong>

                            </div>


                            <div className="stat">

                                <span>
                                    Predicted Time
                                </span>

                                <strong>
                                    {Number(
                                        route.estimatedTravelTime
                                    ).toFixed(1)} min
                                </strong>

                            </div>


                            <div className="stat">

                                <span>
                                    Traffic Delay
                                </span>

                                <strong>
                                    +
                                    {Number(
                                        route.trafficIncreasePercentage
                                    ).toFixed(1)}
                                    %
                                </strong>

                            </div>

                        </div>


                        {/* SAVINGS */}

                        {route.alternatives &&
                            route.alternatives.length > 0 && (

                            <div className="savings-card">

                                <strong>
                                    ⚡ Faster than alternatives
                                </strong>

                                <span>
                                    You save{" "}
                                    {getTimeSaved().toFixed(1)}
                                    {" "}minutes with the
                                    recommended route.
                                </span>

                            </div>

                        )}


                        {/* ALTERNATIVE ROUTES */}

                        {route.alternatives &&
                            route.alternatives.length > 0 && (

                            <div className="alternatives">

                                <div className="alternatives-heading">

                                    <h3>
                                        Alternative Routes
                                    </h3>

                                    <span>
                                        Compared by predicted
                                        travel time
                                    </span>

                                </div>


                                {route.alternatives.map(
                                    (alternative, index) => (

                                        <div
                                            className="alternative-route"
                                            key={index}
                                        >

                                            <div className="alternative-main">

                                                <div className="alternative-number">
                                                    {index + 1}
                                                </div>

                                                <div>

                                                    <div className="alternative-path">

                                                        {alternative.path.join(
                                                            " → "
                                                        )}

                                                    </div>

                                                    <div className="alternative-details">

                                                        {Number(
                                                            alternative.totalDistance
                                                        ).toFixed(1)}
                                                        {" "}km

                                                        <span>
                                                            •
                                                        </span>

                                                        {Number(
                                                            alternative.estimatedTravelTime
                                                        ).toFixed(1)}
                                                        {" "}min

                                                    </div>

                                                </div>

                                            </div>


                                            <div className="alternative-time">

                                                {Number(
                                                    alternative.estimatedTravelTime
                                                ).toFixed(1)}
                                                {" "}min

                                            </div>

                                        </div>

                                    )
                                )}

                            </div>

                        )}


                        {/* WEATHER */}

                        <div className="condition">

                            <span>
                                Weather
                            </span>

                            <strong>
                                {route.weather}
                            </strong>

                        </div>


                        {/* MAP */}

                        <div className="map-section">

                            <h3>
                                Route Map
                            </h3>

                            <MapView
                                path={route.path}
                                locations={locations}
                            />

                        </div>

                    </section>

                )}

            </main>

        </div>
    );
}

export default App;