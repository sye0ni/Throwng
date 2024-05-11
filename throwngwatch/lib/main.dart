import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:throwngwatch/screens/LoginPage.dart';
import 'package:throwngwatch/screens/MainPage.dart';
import 'package:throwngwatch/store/store.dart';
import 'package:wear/wear.dart';
import 'const/color.dart';

Future<void> main() async {
  await dotenv.load(fileName: ".env");
  WidgetsFlutterBinding.ensureInitialized();
  await UserManager().loadUserInfo();
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  static const platform = MethodChannel('com.example.throwngwatch/location');

  @override
  void initState() {
    super.initState();
    platform.setMethodCallHandler(_handleMethod);
  }

  Future<void> _handleMethod(MethodCall call) async {
    final UserManager userManager = UserManager();
    switch (call.method) {
      case 'sendLocation':
        double latitude = call.arguments['latitude'];
        double longitude = call.arguments['longitude'];
        await userManager.saveUserInfo(newLatitude: latitude, newLongitude: longitude);
        print('00000: ${userManager.latitude}');
        print('00000: ${userManager.longitude}');
        break;
      default:
        print('Method not implemented.');
    }
  }

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
