import 'package:flutter/material.dart';
import 'MusicList.dart';

class MusicListPage extends StatelessWidget {
  const MusicListPage({super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(
          context,
          MaterialPageRoute(builder: (context) => MusicList()),
        );
      },
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset(
              'assets/images/MusicList.png',
              width: 100,
              height: 100,
            ),
            SizedBox(
              height: 5,
            ),
            Text("내 주변 인기 음악")
          ],
        ),
      ),
    );
  }
}
