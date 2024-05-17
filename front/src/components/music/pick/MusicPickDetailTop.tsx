import { memo } from "react";
import { useRecoilValue } from "recoil";
import { musicPickHeaderState } from "@store/music/pick/selectors";
import Header from "@components/Header";
import MusicInfoContent from "@components/music/pick/MusicInfoContent";
import AudioPlayer from "@components/music/pick/AudioPlayer";
import "@styles/music/pick/MusicPickDetailTop.scss";

const MusicPickDetailTop = () => {
  const { address, albumImage, previewUrl } =
    useRecoilValue(musicPickHeaderState);

  return (
    <div className="MusicPickDetailTop">
      <img className="album-image" src={albumImage} alt="" />
      <div className="cover">
        <div className="black-cover" />
        <div className="black-gradient" />
        <div className="content">
          <Header centerText={address} type="address" />
          {previewUrl && <AudioPlayer previewUrl={previewUrl} />}
          <MusicInfoContent />
        </div>
      </div>
    </div>
  );
};

export default memo(MusicPickDetailTop);
