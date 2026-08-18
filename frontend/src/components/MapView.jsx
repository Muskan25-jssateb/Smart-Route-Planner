import { useEffect, useState } from "react";

import {
    MapContainer,
    TileLayer,
    Marker,
    Polyline,
    Popup,
    useMap
} from "react-leaflet";

import "leaflet/dist/leaflet.css";

import L from "leaflet";

import markerIcon from "leaflet/dist/images/marker-icon.png";
import markerIcon2x from "leaflet/dist/images/marker-icon-2x.png";
import markerShadow from "leaflet/dist/images/marker-shadow.png";


/* =========================================
   Leaflet marker configuration
========================================= */

const defaultIcon = L.icon({
    iconUrl: markerIcon,
    iconRetinaUrl: markerIcon2x,
    shadowUrl: markerShadow,

    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41]
});

L.Marker.prototype.options.icon = defaultIcon;


/* =========================================
   Automatically fit map to route
========================================= */

function FitRoute({ positions }) {

    const map = useMap();

    useEffect(() => {

        if (!positions || positions.length === 0) {
            return;
        }

        const bounds = L.latLngBounds(positions);

        map.fitBounds(
            bounds,
            {
                padding: [40, 40]
            }
        );

    }, [map, positions]);

    return null;
}


/* =========================================
   MapView
========================================= */

function MapView({ route }) {

    const [locations, setLocations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");


    /* =====================================
       Fetch locations from Spring Boot
    ===================================== */

    useEffect(() => {

        const fetchLocations = async () => {

            try {

                setLoading(true);
                setError("");

                const response = await fetch(
                    `${import.meta.env.VITE_API_URL}/api/locations`
                );

                if (!response.ok) {
                    throw new Error(
                        "Unable to load map locations."
                    );
                }

                const data = await response.json();

                /*
                 * Support both:
                 *
                 * [
                 *   {...},
                 *   {...}
                 * ]
                 *
                 * and:
                 *
                 * {
                 *   locations: [...]
                 * }
                 */

                if (Array.isArray(data)) {

                    setLocations(data);

                } else if (
                    data &&
                    Array.isArray(data.locations)
                ) {

                    setLocations(data.locations);

                } else {

                    throw new Error(
                        "Invalid location data received."
                    );
                }

            } catch (err) {

                console.error(
                    "Map location error:",
                    err
                );

                setError(
                    err.message ||
                    "Unable to load map."
                );

            } finally {

                setLoading(false);
            }
        };


        fetchLocations();

    }, []);


    /* =====================================
       Loading state
    ===================================== */

    if (loading) {

        return (
            <div className="map-container map-state">

                <div className="map-message">

                    <div className="map-spinner"></div>

                    <p>
                        Loading route map...
                    </p>

                </div>

            </div>
        );
    }


    /* =====================================
       Error state
    ===================================== */

    if (error) {

        return (
            <div className="map-container map-state">

                <div className="map-message">

                    <div className="map-error-icon">
                        ⚠️
                    </div>

                    <p>
                        {error}
                    </p>

                </div>

            </div>
        );
    }


    /* =====================================
       No route
    ===================================== */

    if (!route || route.length === 0) {

        return null;
    }


    /* =====================================
       Create location lookup
    ===================================== */

    const locationMap = {};

    locations.forEach((location) => {

        locationMap[location.name] = [
            Number(location.latitude),
            Number(location.longitude)
        ];

    });


    /* =====================================
       Convert route names to coordinates
    ===================================== */

    const positions = route
        .map((location) => locationMap[location])
        .filter(Boolean);


    /* =====================================
       Invalid route coordinates
    ===================================== */

    if (positions.length === 0) {

        return (
            <div className="map-container map-state">

                <div className="map-message">

                    <div className="map-error-icon">
                        📍
                    </div>

                    <p>
                        Location coordinates are unavailable
                        for this route.
                    </p>

                </div>

            </div>
        );
    }


    /* =====================================
       Render map
    ===================================== */

    return (
        <div className="map-container">

            <MapContainer
                center={positions[0]}
                zoom={13}
                scrollWheelZoom={true}
                style={{
                    height: "500px",
                    width: "100%"
                }}
            >

                <TileLayer
                    attribution="&copy; OpenStreetMap contributors"
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />


                <FitRoute
                    positions={positions}
                />


                {/* =========================
                    Route markers
                ========================= */}

                {positions.map(
                    (position, index) => (

                        <Marker
                            key={`${route[index]}-${index}`}
                            position={position}
                        >

                            <Popup>

                                <strong>
                                    {index === 0
                                        ? "Starting Point"
                                        : index === route.length - 1
                                            ? "Destination"
                                            : "Route Stop"}
                                </strong>

                                <br />

                                Location:{" "}
                                {route[index]}

                            </Popup>

                        </Marker>

                    )
                )}


                {/* =========================
                    Route line
                ========================= */}

                <Polyline
                    positions={positions}
                    pathOptions={{
                        weight: 6
                    }}
                />

            </MapContainer>

        </div>
    );
}

export default MapView;