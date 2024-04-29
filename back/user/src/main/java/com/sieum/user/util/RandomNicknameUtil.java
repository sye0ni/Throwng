package com.sieum.user.util;

import java.util.Arrays;
import java.util.List;

public class RandomNicknameUtil {
    public static String getRamdomNinkname() {
        return adjective.get((int) (Math.random() * adjective.size()))
                + nickname.get((int) (Math.random() * nickname.size()));
    }

    static List<String> adjective =
            Arrays.asList(
                    "아름다운", "귀여운", "활기찬", "뛰어난", "창조적인", "반짝이는", "친절한", "충실한", "유능한", "매력적인", "차분한",
                    "자신있는", "명랑한", "적극적인", "성실한", "참신한", "감동적인", "훌륭한", "놀라운", "화려한", "기뻐하는", "독특한",
                    "재치있는", "존경받는", "기분좋은", "흥미로운", "즐거운", "진실한", "따뜻한", "다정한", "자상한", "멋진", "열정적인",
                    "고요한", "밝은", "신나는", "행복한", "대단한", "훈훈한");
    static List<String> nickname =
            Arrays.asList(
                    "멜로디", "하모니", "리듬", "음표", "박자", "독주", "합주", "헤드폰", "마이크", "스피커", "클래식", "댄스",
                    "포크", "힙합", "재즈", "레게", "작곡가", "지휘자", "디제이", "바흐", "베토벤", "모차르트", "슈베르트", "쇼팽",
                    "브람스", "차이콥스키", "증폭기", "악보대", "블루스", "일렉트로닉", "라틴", "오페라", "락", "밴드", "합창단",
                    "오케스트라", "음악가", "연주자", "드보르작", "바그너", "버스킹", "탱고");
}
