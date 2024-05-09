import 'package:flutter/material.dart';
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
    final res = await postWatchAuth(otpInput);
    String accessToken = res.data['accessToken'];
    String refreshToken = res.data['refreshToken'];

    await UserManager().saveUserInfo(newAccessToken: accessToken, newRefreshToken: refreshToken);
    Navigator.push(context, MaterialPageRoute(builder: (context) => MainPage()));
  }

  @override
  Widget build(BuildContext context) {
    return WatchShape(
      builder: (BuildContext context, WearShape shape, Widget? child) {
        return Scaffold(
          body: Center(
            child: Column(
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
