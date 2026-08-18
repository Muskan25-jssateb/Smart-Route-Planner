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

const defaultIcon = L.icon({
    iconUrl: markerIcon,
    iconRetinaUrl: markerIcon2x,
    shadowUrl: markerShadow,
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = defaultIcon;


function FitRoute({ positions }) {

    const map = useMap();

    if (positions.length > 0) {

        const bounds = L.latLngBounds(positions);

        map.fitBounds(
            bounds,
            {
                padding: [40, 40]
            }
        );
    }

    return null;
}


function MapView({ path, locations }) {

    if (!locations || locations.length === 0) {
        return null;
    }

    const locationMap = {};

    locations.forEach((location) => {

        locationMap[location.name] = [
            location.latitude,
            location.longitude
        ];

    });

    const positions = path
        .map((location) => locationMap[location])
        .filter(Boolean);

    if (positions.length === 0) {
        return null;
    }

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

                {positions.map((position, index) => (

                    <Marker
                        key={index}
                        position={position}
                    >

                        <Popup>
                            Location: {path[index]}
                        </Popup>

                    </Marker>

                ))}

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