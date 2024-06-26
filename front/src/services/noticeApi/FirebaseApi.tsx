import { axiosApi } from "@/utils/common";

const api = axiosApi();

const postFcmToken = async (fcmToken: string) => {
  try {
    // console.log("Sending token payload:", { fcmToken });
    const response = await api.post("/users/user/fcm", { fcmToken });
    // console.log("토큰 보내기 성공: ", response);
    return response;
  } catch (e) {
    console.error("토큰 보내기 실패:", e);
  }
};

export { postFcmToken };
