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
      console.log(data);
      setMarkers(data);
    }

    // const newItems = data.filter(
    //   (item) => !markers.some((items) => items.itemId === item.itemId)
    // );

    // console.log(newItems);

    // if (newItems.length > 0) {
    //   setMarkers([...markers, ...newItems]);
    // }

    // setMarkers((prev) => {
    //   const prevIds = new Set(prev.map((item) => item.itemId));
    //   const newItems = data.filter((item) => !prevIds.has(item.itemId));
    //   if (newItems.length === 0) {
    //     return prev;
    //   }
    //   return [...prev, ...newItems];
    // });
  } catch (err) {
    console.error(err);
  }
};

export default fetchMusic;
