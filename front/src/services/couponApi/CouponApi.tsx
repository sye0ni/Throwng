import axios from "axios";
import { axiosApi } from "@/utils/common";
import { quizCoupon } from "../../types/couponType";

const api = axiosApi();

const getContentCoupon = async (route: string): Promise<quizCoupon> => {
  try {
    const response = await api.get<quizCoupon>(`/quizzes/coupons/${route}`);
    return response.data;
  } catch (e) {
    if (axios.isAxiosError(e)) {
      console.error("Axios error:", e.response?.data || e.message);
    } else {
      console.error("Unexpected error:", e);
    }
    throw e;
  }
};

export { getContentCoupon };
