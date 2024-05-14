import {
  Dispatch,
  SetStateAction,
  memo,
  useCallback,
  useEffect,
  useRef,
  useState,
} from "react";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import {
  centerState,
  locationState,
  markersState,
  prevLocationState,
  zoomLevelState,
} from "@store/map/atoms";
import MapClusterer from "@components/map/MapClusterer";
import MyLocation from "@components/map/MyLocation";
import { CONTAINER_STYLE, MAP_OPTIONS } from "@constants/map";
import { GoogleMap } from "@react-google-maps/api";
import { Location } from "../../types/mapType";
import fetchDistance from "@/utils/map/fetchDistance";
import useFetchMusic from "@hooks/map/useFetchMusic";
import useFetchAddress from "@hooks/map/useFetchAddress";

interface Props {
  map: google.maps.Map | null;
  setMap: Dispatch<SetStateAction<google.maps.Map | null>>;
  tilesLoaded: boolean;
  setTilesLoaded: Dispatch<SetStateAction<boolean>>;
}

const MapContainer = ({ map, setMap, tilesLoaded, setTilesLoaded }: Props) => {
  const [initialLoad, setInitialLoad] = useState(true);
  const [prevLocation, setPrevLocation] = useRecoilState(prevLocationState);
  const setLocation = useSetRecoilState(locationState);
  const setZoomLevel = useSetRecoilState(zoomLevelState);
  const [center, setCenter] = useRecoilState(centerState);
  const markers = useRecoilValue(markersState);
  const location = useRecoilValue(locationState);

  const centerRef = useRef(center);
  const prevLocationRef = useRef(prevLocation);
  const initialLoadRef = useRef(initialLoad);

  const { fetchMusicc } = useFetchMusic();
  const { fetchAddress } = useFetchAddress();

  const onLoad = useCallback((map: google.maps.Map) => setMap(map), []);

  const onUnmount = useCallback(() => setMap(null), []);

  const onTilesLoaded = useCallback(() => {
    if (!tilesLoaded) {
      setTilesLoaded(true);
    }
  }, [tilesLoaded]);

  const onChanged = () => {
    if (!initialLoad && center) {
      setCenter(false);
    }
  };

  const onZoomChanged = () => {
    onChanged();
    if (map) {
      const zoom = map.getZoom()!;
      setZoomLevel(zoom);
    }
  };

  const changeCenter = useCallback(() => {
    if (map) {
      if (!center) {
        const mapCenter = map.getCenter();
        if (mapCenter) {
          const mapPosition = { lat: mapCenter.lat(), lng: mapCenter.lng() };
          fetchMusicc(false, mapPosition, markers);
          fetchAddress(mapPosition, "mapCenter");
        }
      } else {
        fetchMusicc(true, location, markers);
      }
    }
  }, [map, center, markers, location]);

  const updateMyLocation = useCallback(
    (location: Location) => {
      setLocation(location);
      fetchAddress(location, "myLocation");
    },
    [fetchAddress]
  );

  useEffect(() => {
    centerRef.current = center;
  }, [center]);

  useEffect(() => {
    prevLocationRef.current = prevLocation;
  }, [prevLocation]);

  useEffect(() => {
    initialLoadRef.current = initialLoad;
  }, [initialLoad]);

  useEffect(() => {
    if (map) {
      const watchId = navigator.geolocation.watchPosition(
        ({ coords }) => {
          const currentLocation = {
            lat: coords.latitude,
            lng: coords.longitude,
          };

          if (initialLoadRef.current) {
            map.setZoom(15);
            setZoomLevel(15);
            map.setCenter(currentLocation);
            // console.log("초반");
            fetchMusicc(true, currentLocation, markers);
            updateMyLocation(currentLocation);
            setPrevLocation(currentLocation);
            setInitialLoad(false);
          } else {
            const distance = fetchDistance(
              prevLocationRef.current,
              currentLocation
            );

            if (distance) {
              if (distance >= 50) {
                setPrevLocation(currentLocation);
                if (centerRef.current) {
                  // console.log("후반");
                  fetchMusicc(true, currentLocation, markers);
                }
              }

              updateMyLocation(currentLocation);

              if (centerRef.current) {
                map.panTo(currentLocation);
              }
            }
          }
        },
        (err) => {
          console.error("Error fetching location", err);
        },
        { enableHighAccuracy: true }
      );

      return () => {
        navigator.geolocation.clearWatch(watchId);
      };
    }
  }, [map]);

  // console.log("센터변경");

  return (
    <GoogleMap
      mapContainerStyle={CONTAINER_STYLE}
      onTilesLoaded={onTilesLoaded}
      options={MAP_OPTIONS}
      onLoad={onLoad}
      onUnmount={onUnmount}
      onDragStart={onChanged}
      onZoomChanged={onZoomChanged}
      onIdle={changeCenter}
    >
      {tilesLoaded && (
        <>
          <MyLocation />
          <MapClusterer />
        </>
      )}
    </GoogleMap>
  );
};

export default memo(MapContainer);
