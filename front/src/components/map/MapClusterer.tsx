import { memo, useMemo } from "react";
import { useRecoilValue } from "recoil";
import { markersState } from "@store/map/atoms";
import MusicMarkerItem from "./MusicMarkerItem";

const MapClusterer = () => {
  const markers = useRecoilValue(markersState);

  const renderedMarkers = useMemo(
    () =>
      markers.map((marker) => (
        <MusicMarkerItem key={marker.itemId} marker={marker} />
      )),
    [markers]
  );

  return renderedMarkers;

  // return markers.map((marker) => (
  //   <MusicMarkerItem key={marker.itemId} marker={marker} />
  // ));
};

export default memo(MapClusterer);
