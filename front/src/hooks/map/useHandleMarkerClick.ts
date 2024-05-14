import { getCheckRadiusCoupon } from "@services/mapAPi";
import { useSetRecoilState } from "recoil";
import {
  couponUsageActiveIdState,
  radiusActiveIdState,
} from "@store/map/atoms";
import { toastMsg } from "@/utils/toastMsg";
import getDistance from "@/utils/map/fetchDistance";
import { Location, Marker } from "../../types/mapType";

const useHandleMarkerClick = () => {
  const setRadiusActiveId = useSetRecoilState(radiusActiveIdState);
  const setCouponUsageActiveId = useSetRecoilState(couponUsageActiveIdState);

  const handleMarkerClick = async (marker: Marker, location: Location) => {
    try {
      const distance = getDistance(
        { lat: marker.latitude, lng: marker.longitude },
        location
      );

      const data = await getCheckRadiusCoupon();

      if (distance <= 600) {
        setRadiusActiveId(marker.itemId);
      } else {
        if (data) {
          setCouponUsageActiveId(marker.itemId);
        } else {
          toastMsg("반경 밖 음악을 듣고 싶다면 위치를 이동해 보세요!");
          setRadiusActiveId(null);
        }
      }
    } catch (error) {
      console.error("Error checking coupon availability:", error);
      toastMsg("잠시 후 다시 이용해 주세요");
    }
  };

  return { handleMarkerClick };
};

export default useHandleMarkerClick;
