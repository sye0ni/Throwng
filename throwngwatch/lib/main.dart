import 'package:flutter/material.dart';
import 'package:throwngwatch/screens/MainPage.dart';
import 'package:wear/wear.dart';
import 'const/color.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Center(
          child: WatchShape(
            builder: (context, shape, child) {
              return AmbientMode(
                builder: (context, mode, child) {
                  return MainPage();
                },
              );
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
