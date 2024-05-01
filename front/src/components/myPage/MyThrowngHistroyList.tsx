import { useEffect, useState } from "react";
import { MyHistory } from "../../types/songType";
import "@styles/myPage/MyThrowngHistroyList.scss";
import { TiLocation } from "react-icons/ti";
import { useRecoilValue } from "recoil";
import {
  myPickHistoryList,
  myThrowHistoryList,
  throwngFilter,
} from "@store/myPage/atoms";
import { useNavigate } from "react-router-dom";
import dayjs from "dayjs";
import isBetween from "dayjs/plugin/isBetween";

interface Props {
  pageIdx: boolean;
  setHistoryCnt: React.Dispatch<React.SetStateAction<number>>;
}

const MyThrowngHistroyList = ({ pageIdx, setHistoryCnt }: Props) => {
  const [songHistoryList, setSongHistoryList] = useState<Array<MyHistory>>([]);
  const filter = useRecoilValue(throwngFilter);
  const filterThrownList = useRecoilValue(myThrowHistoryList);
  const filterPickList = useRecoilValue(myPickHistoryList);
  const now = dayjs();
  const sevenDaysAgo = dayjs().subtract(7, "days");
  const navigate = useNavigate();
  dayjs.extend(isBetween);

  const fetchAndFilterHistory = () => {
    const dataList = !pageIdx ? filterThrownList : filterPickList;
    const filteredData = dataList
      .filter((item: MyHistory) => {
        const dateToUse = item.dropDate ? item.dropDate : item.pickDate;
        const itemDate = dayjs(dateToUse);
        switch (filter) {
          case "오늘":
            return now.isSame(itemDate, "day");
          case "이번 주":
            return dayjs(now).isBetween(sevenDaysAgo, now, "day", "[]");
          case "이번 달":
            return now.isSame(itemDate, "month");
          case "전체":
            return true;
          default:
            return false;
        }
      })
      .sort((a, b) => {
        const dateA = a.dropDate ? a.dropDate : a.pickDate;
        const dateB = b.dropDate ? b.dropDate : b.pickDate;
        return dayjs(dateB).diff(dayjs(dateA));
      });

    setSongHistoryList(filteredData);
    setHistoryCnt(filteredData.length);
  };

  useEffect(() => {
    fetchAndFilterHistory();
  }, [filter, pageIdx, filterThrownList, filterPickList]);

  console.log(songHistoryList);

  const handleGoNavigation = (song: MyHistory) => {
    if ("myThrowId" in song) {
      navigate(`/music/pick/${song.myThrowId}`);
    } else if ("myPickId" in song) {
      navigate(`/music/pick/${song.throwId}`);
    }
  };

  return (
    <div className="MyThrowngHistroyList">
      <div className="list-body">
        {songHistoryList.length > 0 ? (
          songHistoryList.map((song, index) => (
            <div
              key={index}
              className="result-item"
              onClick={() => handleGoNavigation(song)}
            >
              <div className="item-header">
                {!pageIdx ? (
                  <div className="item-date">
                    {dayjs(song.dropDate).format("YYYY-MM-DD")}
                  </div>
                ) : (
                  <div className="item-date">
                    {dayjs(song.pickDate).format("YYYY-MM-DD")}
                  </div>
                )}

                <div className="item-location">
                  <TiLocation /> {song.location}
                </div>
              </div>
              <div className="item">
                <div className="img-container">
                  <img src={song.albumImage} alt="" />
                </div>
                <div className="item-detail">
                  <div className="item-title">{song.title}</div>
                  <div className="item-artist">{song.artist}</div>
                  <div className="item-comment">{song.comment}</div>
                </div>
              </div>
            </div>
          ))
        ) : (
          <div className="no-result">내역이 없습니다.</div>
        )}
      </div>
    </div>
  );
};

export default MyThrowngHistroyList;
