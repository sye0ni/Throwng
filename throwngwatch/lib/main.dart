import 'package:flutter/material.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:throwngwatch/screens/LoginPage.dart';
import 'package:throwngwatch/screens/MainPage.dart';
import 'package:throwngwatch/store/store.dart';
import 'package:wear/wear.dart';
import 'const/color.dart';

void main() async {
  await dotenv.load(fileName: ".env");
  WidgetsFlutterBinding.ensureInitialized();
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: WatchShape(
            builder: (context, shape, child) {
              if (UserManager().accessToken == null) {
                return LoginPage();
              } else {
                return MainPage();
              }
            },
          ),
        ),
      ),
      theme: ThemeData(
        scaffoldBackgroundColor: BG_COLOR,
        textTheme: ThemeData.light().textTheme.apply(
              bodyColor: Colors.white,
              displayColor: Colors.white,
            ),
      ),
    );
  }
}
