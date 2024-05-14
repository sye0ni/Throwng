import {
  Dispatch,
  SetStateAction,
  memo,
  useCallback,
  useEffect,
  useState,
} from "react";
import { useRecoilState, useSetRecoilState } from "recoil";
import { centerState, loadStatusState, zoomLevelState } from "@store/map/atoms";
import { GoogleMap } from "@react-google-maps/api";
import MapClusterer from "@components/map/MapClusterer";
import MyLocation from "@components/map/MyLocation";
import { CONTAINER_STYLE, MAP_OPTIONS } from "@constants/map";
import useLocationWatcher from "@hooks/map/useLocationWatcher";
import useChangeCenter from "@hooks/map/useChangeCenter";

interface Props {
  map: google.maps.Map | null;
  setMap: Dispatch<SetStateAction<google.maps.Map | null>>;
}

const MapContainer = ({ map, setMap }: Props) => {
  const [initialLoad, setInitialLoad] = useState(true);
  const [center, setCenter] = useRecoilState(centerState);
  const [loadStatuss, setLoadStatuss] = useRecoilState(loadStatusState);
  const setZoomLevel = useSetRecoilState(zoomLevelState);
  const [loadStatus, setLoadStatus] = useState("UNINITIALIZED");

  const { changeCenter } = useChangeCenter();
  useLocationWatcher(map, initialLoad, setInitialLoad);

  const onLoad = useCallback((map: google.maps.Map) => setMap(map), []);
  const onUnmount = useCallback(() => setMap(null), []);

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

  useEffect(() => {
    if (map) {
      const status = map.getRenderingType();
      if (status !== "UNINITIALIZED") {
        setLoadStatus(status);
      }
    }
  }, [loadStatuss]);

  return (
    <GoogleMap
      mapContainerStyle={CONTAINER_STYLE}
      onTilesLoaded={() => {
        setLoadStatuss((prev) => prev + 1);
      }}
      options={MAP_OPTIONS}
      onLoad={onLoad}
      onUnmount={onUnmount}
      onDragStart={onChanged}
      onZoomChanged={onZoomChanged}
      onIdle={() => changeCenter(map)}
    >
      {loadStatus !== "UNINITIALIZED" && (
        <>
          <MyLocation />
          <MapClusterer />
        </>
      )}
    </GoogleMap>
  );
};

export default memo(MapContainer);
