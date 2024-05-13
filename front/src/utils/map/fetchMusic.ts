import { postMusicRadius } from "@services/mapAPi";
import { Location, Marker } from "../../types/mapType";
import { SetterOrUpdater } from "recoil";
import arraysAreEqual from "./arraysAreEqual";

const fetchMusic = async (
  isUserLocation: boolean,
  position: Location,
  markers: Marker[],
  setMarkers: SetterOrUpdater<Marker[]>
) => {
  try {
    const data = await postMusicRadius(isUserLocation, position);
    if (!arraysAreEqual(data, markers)) {
      setMarkers(data);
    }

    // setMarkers((prev) => {
    //   const prevIds = new Set(prev.map((item) => item.itemId)); // prev의 id만 모은 Set

    //   // console.log(prevIds);

    //   // next에서 prev에 없는 아이템만 찾아 추가
    //   const newItems = data.filter((item) => !prevIds.has(item.itemId));

    //   // console.log(newItems);

    //   // 기존의 prev 배열에 새로운 아이템들을 추가하여 새 배열을 반환
    //   return [...prev, ...newItems];
    // });
  } catch (err) {
    console.error(err);
  }
};

export default fetchMusic;
