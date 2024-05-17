import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:throwngwatch/screens/MainPage.dart';
import 'package:wear/wear.dart';
import '../const/color.dart';
import '../services/MusicListAPi.dart';
import '../store/store.dart';
import '../widgets/ElevateBTN.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  String otpInput = '';
  TextEditingController inputController = TextEditingController();
  bool isLoading = false;

  @override
  void initState() {
    super.initState();
    inputController.text = otpInput;
  }

  @override
  void dispose() {
    inputController.dispose();
    super.dispose();
  }

  void handlePress() async {
    setState(() {
      isLoading = true;
    });
    try {
      final res = await postWatchAuth(otpInput);
      await UserManager().saveUserInfo(newAccessToken: res.data['accessToken'], newRefreshToken: res.data['refreshToken']);
      Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
    } on DioError catch (e) {
      String errorMessage = "오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
      if (e.response != null) {
        switch (e.response!.statusCode) {
          case 400:
            errorMessage = "잘못된 OTP입니다. 입력을 확인해주세요.";
            break;
          case 429:
            errorMessage = "잠시 후 다시 시도해주세요.";
            break;
          case 500:
            errorMessage = "서버 오류가 발생했습니다.";
            break;
        }
      } else {
        errorMessage = "네트워크 연결을 확인할 수 없습니다.";
      }
      Fluttertoast.showToast(
        msg: errorMessage,
        gravity: ToastGravity.CENTER,
        timeInSecForIosWeb: 1,
        backgroundColor: Color(0xff363636),
        textColor: Colors.white,
        fontSize: 13,
      );
    } catch (e) {
      Fluttertoast.showToast(
        msg: "알 수 없는 오류가 발생했습니다.",
        gravity: ToastGravity.CENTER,
        timeInSecForIosWeb: 1,
        backgroundColor: Color(0xff363636),
        textColor: Colors.white,
        fontSize: 13,
      );
    } finally {
      setState(() {
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return WatchShape(
      builder: (BuildContext context, WearShape shape, Widget? child) {
        return Scaffold(
          body: Center(
            child: isLoading
                ? CircularProgressIndicator()
                : Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text(
                        '휴대폰 화면에 뜬\nOTP 번호를 입력하세요',
                        textAlign: TextAlign.center,
                      ),
                      TextField(
                        controller: inputController,
                        keyboardType: TextInputType.number,
                        textAlign: TextAlign.center,
                        maxLength: 6,
                        minLines: 1,
                        onChanged: (value) {
                          setState(() {
                            otpInput = value.trim();
                          });
                        },
                        style: TextStyle(
                          fontSize: 13,
                          color: Colors.white,
                        ),
                        decoration: InputDecoration(
                          border: InputBorder.none,
                          hintStyle: TextStyle(color: PLACEHOLDER),
                          hintText: '숫자 6자리를 입력하세요',
                          counterText: "",
                          contentPadding: EdgeInsets.symmetric(vertical: 10),
                        ),
                        textInputAction: TextInputAction.done,
                        onSubmitted: (value) {
                          setState(() {
                            otpInput = value;
                          });
                          FocusScope.of(context).unfocus();
                        },
                      ),
                      CustomElevatedButton(
                        text: '확인',
                        onPressed: otpInput.trim().isEmpty
                            ? null
                            : () async {
                                handlePress();
                              },
                        backgroundColor: BTN_COLOR,
                        textColor: Colors.white,
                      )
                    ],
                  ),
          ),
        );
      },
    );
  }
}
