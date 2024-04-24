package com.sieum.user.util;

import java.util.Arrays;
import java.util.List;

public class RandomNicknameUtil {
    public static String getRamdomNinkname() {
        return adjective.get((int) (Math.random() * adjective.size()))
                + fishname.get((int) (Math.random() * fishname.size()));
    }

    static List<String> adjective =
            Arrays.asList(
                    "아름다운", "귀여운", "활기찬", "뛰어난", "창조적인", "반짝이는", "친절한", "충실한", "유능한", "매력적인", "차분한",
                    "자신있는", "명랑한", "적극적인", "성실한", "참신한", "감동적인", "훌륭한", "놀라운", "화려한", "기뻐하는", "독특한",
                    "재치있는", "존경받는", "기분좋은", "흥미로운", "즐거운", "진실한", "따뜻한", "다정한", "자상한", "멋진", "열정적인",
                    "고요한", "밝은", "신나는", "행복한", "대단한", "훈훈한");
    static List<String> fishname =
            Arrays.asList(
                    "가물치", "송어", "꺽지", "끄리", "누치", "메기", "배스", "붕어", "송어", "쏘가리", "은어", "잉어", "향어",
                    "다랑어", "갈치", "감성돔", "오징어", "상어", "넙치", "노래미", "농어", "달고기", "대구", "돌돔", "만새기",
                    "방어", "뱅에돔", "보리멸", "뽈락", "붉바리", "삼치", "성대", "쏨뱅이", "옥돔", "놀래기", "자리돔", "전갱이",
                    "전어", "주꾸미", "쥐치", "호박돔", "황어");
}
