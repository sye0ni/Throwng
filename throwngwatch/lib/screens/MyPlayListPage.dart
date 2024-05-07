import 'package:flutter/material.dart';
import 'package:throwngwatch/screens/MyPlayList.dart';

class MyPlayListPage extends StatelessWidget {
  const MyPlayListPage({super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => MyPlayList()),
        );
      },
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset(
              'assets/images/MyPlayList.png',
              width: 50,
              height: 50,
            ),
            Text("내 플레이리스트")
          ],
        ),
      ),
    );
  }
}
