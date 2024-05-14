import { memo, useEffect, useState } from "react";
import ToasterMsg from "@components/ToasterMsg";
import MapHeader from "./MapHeader";
import LoadMap from "@components/map/LoadMap";
import Loading from "@components/Loading";
import { useRecoilValue } from "recoil";
import { loadStatusState } from "@store/map/atoms";
import "@styles/map/Map.scss";

const Map = () => {
  const [map, setMap] = useState<google.maps.Map | null>(null);
  const [loadStatus, setLoadStatus] = useState("UNINITIALIZED");
  const loadStatuss = useRecoilValue(loadStatusState);

  useEffect(() => {
    if (map) {
      const status = map.getRenderingType();
      if (status !== "UNINITIALIZED") {
        setLoadStatus(status);
      }
    }
  }, [loadStatuss]);

  return (
    <div className="Map">
      {loadStatus !== "UNINITIALIZED" && <MapHeader map={map} />}
      <LoadMap map={map} setMap={setMap} />
      <ToasterMsg />
      {loadStatus === "UNINITIALIZED" && <Loading />}
    </div>
  );
};

export default memo(Map);
