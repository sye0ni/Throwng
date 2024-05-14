import arraysAreEqual from "@/utils/map/arraysAreEqual";
import { postMusicRadius } from "@services/mapAPi";
import { markersState } from "@store/map/atoms";
import { Location, Marker } from "../../types/mapType";
import { useSetRecoilState } from "recoil";

const useFetchMusic = () => {
  const setMarkers = useSetRecoilState(markersState);

  const fetchMusicc = async (
    isUserLocation: boolean,
    position: Location,
    markers: Marker[]
  ) => {
    try {
      const data = await postMusicRadius(isUserLocation, position);
      if (!arraysAreEqual(data, markers)) {
        setMarkers(data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return { fetchMusicc };
};

export default useFetchMusic;
