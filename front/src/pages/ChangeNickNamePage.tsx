import Header from "@components/Header"
import { changeNickNameCouponId, myNickName } from "@store/myPage/atoms";
import { useRef, useState } from "react"
import { useRecoilValue } from "recoil";
import "@styles/ChangeNickName/ChangeNickName.scss"
import { putNickName } from "@services/myCouponApi/MyCouponAPi";
import { useNavigate } from "react-router-dom";
import ToasterMsg from "@components/ToasterMsg";
import { toastMsg } from "@/utils/toastMsg";

const ChangeNickNamePage = () => {
  const inputEl = useRef<HTMLInputElement>(null);
  const myName = useRecoilValue(myNickName);
  const [nickName, setNickName] = useState('');
  const navigate = useNavigate();
  const changeNickNameCouponIdValue = useRecoilValue(changeNickNameCouponId)

  const nickNameOnChange = (e:React.ChangeEvent<HTMLInputElement>) => {
    setNickName(e.target.value);
  }

  const handleOnClick = async (e:React.MouseEvent<HTMLDivElement>) => {
    e.preventDefault();
    const regex = /^[가-힣]{1,15}$/;

    const requestBody = {
      'couponId':changeNickNameCouponIdValue,
      'nickName':nickName,
      'couponType':"NICKNAME",
    }

    if (regex.test(nickName)) {
      console.log(requestBody)
      await putNickName(requestBody)
      navigate('/user/mypage', {replace:true})
    } else {
      toastMsg("닉네임은 한글 단어로만 설정이 가능하며, 영어, 숫자, 공백, 특수문자는 사용할 수 없습니다. 최대 15자까지 가능합니다.");
      setNickName('');
      return false;
    }
  }

  return (
    <div className="ChangeNickNamePage">
      <div className="content">
        <Header centerText="프로필 수정" />
        <div
          className={nickName.trim() ? 'onSubmit' : 'onSubmitClose'}
          onClick={nickName.trim()? handleOnClick : undefined}>
            확인
        </div>
        <div className="body">
          <div className="title">닉네임</div>
          <div className="change-nickname-input-div">
            <input
              className="input"
              ref={inputEl}
              type="text"
              placeholder={myName}
              value={nickName}
              onChange={(e)=> nickNameOnChange(e)}
              maxLength={15}
              />
          </div>
          <div className="notice">
            <div>닉네임은 <span>한글 단어</span>로만 설정이 가능합니다.</div>
            <div>영어, 숫자, 공백, 특수문자의 사용은 <span>불가능</span>합니다.</div>
            <div>최대 <span>15자</span>까지 가능합니다.</div>
          </div>
        </div>
      </div>
      <ToasterMsg/>
    </div>
  )
}

export default ChangeNickNamePage