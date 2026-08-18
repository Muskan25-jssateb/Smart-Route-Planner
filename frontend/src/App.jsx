import { useState } from "react";
import "./index.css";
import MapView from "./components/MapView";

function App() {
    const [source, setSource] = useState("");
    const [destination, setDestination] = useState("");
    const [weather, setWeather] = useState("Sunny");
    const [hour, setHour] = useState(12);
    const [dayOfWeek, setDayOfWeek] = useState(1);

    const [route, setRoute] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const findRoute = async () => {
        if (!source || !destination) {
            setError("Please enter both source and destination.");
            return;
        }

        if (source === destination) {
            setError("Source and destination cannot be the same.");
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
                throw new Error("Unable to calculate route.");
            }

            const data = await response.json();

            setRoute(data);

        } catch (err) {
            setError(
                err.message ||
                "Something went wrong while calculating the route."
            );
        } finally {
            setLoading(false);
        }
    };

    const getTrafficClass = (trafficLevel) => {
        if (!trafficLevel) return "";

        return trafficLevel.toLowerCase();
    };

    return (
        <div className="app">

            {/* ================= HEADER ================= */}

            <header className="header">
                <div className="header-content">

                    <div>
                        <div className="brand">
                            <span className="brand-icon">🚗</span>
                            <span>Smart Route Planner</span>
                        </div>

                        <p>
                            AI-powered traffic-aware route optimization
                        </p>
                    </div>

                    <div className="status-badge">
                        <span className="status-dot"></span>
                        System Online
                    </div>

                </div>
            </header>

            {/* ================= MAIN ================= */}

            <main className="container">

                {/* ================= ROUTE INPUT ================= */}

                <section className="route-card">

                    <div className="section-heading">
                        <div>
                            <h2>Plan Your Route</h2>

                            <p>
                                Find the fastest route based on traffic,
                                weather and travel conditions.
                            </p>
                        </div>
                    </div>

                    <div className="route-inputs">

                        <div className="input-group">
                            <label>Source</label>

                            <div className="input-wrapper">
                                <span className="input-icon source-icon">
                                    ●
                                </span>

                                <input
                                    value={source}
                                    onChange={(e) =>
                                        setSource(
                                            e.target.value.toUpperCase()
                                        )
                                    }
                                    placeholder="Example: A"
                                    maxLength={1}
                                />
                            </div>
                        </div>

                        <div className="route-arrow">
                            ↓
                        </div>

                        <div className="input-group">
                            <label>Destination</label>

                            <div className="input-wrapper">
                                <span className="input-icon destination-icon">
                                    ●
                                </span>

                                <input
                                    value={destination}
                                    onChange={(e) =>
                                        setDestination(
                                            e.target.value.toUpperCase()
                                        )
                                    }
                                    placeholder="Example: H"
                                    maxLength={1}
                                />
                            </div>
                        </div>

                    </div>

                    {/* ================= CONDITIONS ================= */}

                    <div className="conditions">

                        <div className="input-group">
                            <label>Weather</label>

                            <select
                                value={weather}
                                onChange={(e) =>
                                    setWeather(e.target.value)
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
                            <label>Hour</label>

                            <input
                                type="number"
                                min="0"
                                max="23"
                                value={hour}
                                onChange={(e) =>
                                    setHour(e.target.value)
                                }
                            />
                        </div>

                        <div className="input-group">
                            <label>Day</label>

                            <select
                                value={dayOfWeek}
                                onChange={(e) =>
                                    setDayOfWeek(e.target.value)
                                }
                            >
                                <option value="0">Monday</option>
                                <option value="1">Tuesday</option>
                                <option value="2">Wednesday</option>
                                <option value="3">Thursday</option>
                                <option value="4">Friday</option>
                                <option value="5">Saturday</option>
                                <option value="6">Sunday</option>
                            </select>
                        </div>

                    </div>

                    {/* ================= BUTTON ================= */}

                    <button
                        className="find-route-button"
                        onClick={findRoute}
                        disabled={loading}
                    >
                        {loading ? (
                            <>
                                <span className="spinner"></span>
                                Finding Best Route...
                            </>
                        ) : (
                            <>
                                Find Best Route
                                <span>→</span>
                            </>
                        )}
                    </button>

                    {error && (
                        <div className="error">
                            ⚠️ {error}
                        </div>
                    )}

                </section>

                {/* ================= RESULT ================= */}

                {route && (
                    <section className="result-card">

                        <div className="result-header">

                            <div>
                                <h2>Recommended Route</h2>

                                <p>
                                    Fastest traffic-aware route found
                                </p>
                            </div>

                            <div
                                className={`traffic-badge ${getTrafficClass(
                                    route.trafficLevel
                                )}`}
                            >
                                <span className="traffic-dot"></span>
                                {route.trafficLevel}
                            </div>

                        </div>

                        {/* ================= ROUTE PATH ================= */}

                        <div className="route-display">

                            <div className="route-endpoint">
                                <span className="endpoint-dot start"></span>

                                <strong>
                                    {route.source}
                                </strong>
                            </div>

                            <div className="route-line">
                                <span></span>
                            </div>

                            <div className="route-stops">

                                {route.path &&
                                    route.path
                                        .slice(1, -1)
                                        .map((location, index) => (
                                            <div
                                                className="route-stop"
                                                key={`${location}-${index}`}
                                            >
                                                <span>
                                                    {location}
                                                </span>
                                            </div>
                                        ))}

                            </div>

                            <div className="route-line">
                                <span></span>
                            </div>

                            <div className="route-endpoint">
                                <span className="endpoint-dot end"></span>

                                <strong>
                                    {route.destination}
                                </strong>
                            </div>

                        </div>

                        {/* ================= MAP ================= */}

                        <div className="map-container">
                            <MapView route={route.path} />
                        </div>

                        {/* ================= STATISTICS ================= */}

                        <div className="stats">

                            <div className="stat">
                                <div className="stat-icon">
                                    📍
                                </div>

                                <div>
                                    <span>
                                        Distance
                                    </span>

                                    <strong>
                                        {route.totalDistance} km
                                    </strong>
                                </div>
                            </div>

                            <div className="stat">
                                <div className="stat-icon">
                                    ⏱️
                                </div>

                                <div>
                                    <span>
                                        Estimated Time
                                    </span>

                                    <strong>
                                        {Number(
                                            route.estimatedTravelTime
                                        ).toFixed(2)}{" "}
                                        min
                                    </strong>
                                </div>
                            </div>

                            <div className="stat">
                                <div className="stat-icon">
                                    🚦
                                </div>

                                <div>
                                    <span>
                                        Traffic
                                    </span>

                                    <strong>
                                        {route.trafficLevel}
                                    </strong>
                                </div>
                            </div>

                            <div className="stat">
                                <div className="stat-icon">
                                    🌤️
                                </div>

                                <div>
                                    <span>
                                        Weather
                                    </span>

                                    <strong>
                                        {route.weather}
                                    </strong>
                                </div>
                            </div>

                        </div>

                    </section>
                )}

            </main>

            {/* ================= FOOTER ================= */}

            <footer className="footer">
                <p>
                    Smart Route Planner · AI-powered traffic optimization
                </p>
            </footer>

        </div>
    );
}

export default App;