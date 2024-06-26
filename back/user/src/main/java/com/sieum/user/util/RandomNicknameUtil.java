package com.sieum.user.util;

import java.util.Arrays;
import java.util.List;

public class RandomNicknameUtil {
    public static String getRamdomNinkname() {
        return area.get((int) (Math.random() * area.size()))
                + nickname.get((int) (Math.random() * nickname.size()));
    }

    static List<String> area =
            Arrays.asList(
                    "마포", "잠실", "이태원", "홍대", "관악", "명동", "상암", "뚝섬", "선릉", "압구정", "역삼", "노량진",
                    "여의도", "신도림", "방이동", "북촌", "종로", "판교", "인천", "남양주", "망원", "서면", "광안리", "충장로",
                    "동성로", "혜화", "대학로", "용인", "수원", "광안리", "해운대", "강릉", "송도", "분당", "논현", "반포",
                    "신촌", "광화문", "서귀포");
    static List<String> nickname =
            Arrays.asList(
                    "불주먹", "불가사리", "독수리", "호랑이", "둘리", "짱구", "맹구", "허스키", "계란", "두더지", "코난", "알라딘",
                    "훈이", "포켓몬", "루피", "나루토", "손오공", "도라에몽", "스폰지밥", "뚱이", "가오나시", "흰둥이", "노진구",
                    "퉁퉁이", "비실이", "아이언맨", "포로리", "보노보노", "토르", "헐크", "헤르미온느", "말포이", "뱁새", "토토로",
                    "양상추", "신형만", "봉미선", "물주먹", "물뿌리개", "짱아", "핑구", "핑가");

    //    static List<String> adjective =
    //            Arrays.asList(
    //                    "아름다운", "귀여운", "활기찬", "뛰어난", "창조적인", "반짝이는", "친절한", "충실한", "유능한", "매력적인",
    // "차분한",
    //                    "자신있는", "명랑한", "적극적인", "성실한", "참신한", "감동적인", "훌륭한", "놀라운", "화려한", "기뻐하는",
    // "독특한",
    //                    "재치있는", "존경받는", "기분좋은", "흥미로운", "즐거운", "진실한", "따뜻한", "다정한", "자상한", "멋진",
    // "열정적인",
    //                    "고요한", "밝은", "신나는", "행복한", "대단한", "훈훈한");
    //    static List<String> nickname =
    //            Arrays.asList(
    //                    "멜로디", "하모니", "리듬", "음표", "박자", "독주", "합주", "헤드폰", "마이크", "스피커", "클래식",
    // "댄스",
    //                    "포크", "힙합", "재즈", "레게", "작곡가", "지휘자", "디제이", "바흐", "베토벤", "모차르트", "슈베르트",
    // "쇼팽",
    //                    "브람스", "차이콥스키", "증폭기", "악보대", "블루스", "일렉트로닉", "라틴", "오페라", "락", "밴드",
    // "합창단",
    //                    "오케스트라", "음악가", "연주자", "드보르작", "바그너", "버스킹", "탱고");
}
