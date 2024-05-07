import 'package:flutter/material.dart';
import '../components/MusicListBody.dart';
import '../const/color.dart';
import '../widgets/ClockTime.dart';
import 'MusicPickDetail.dart';

class MusicList extends StatefulWidget {
  @override
  State<MusicList> createState() => _MusicListState();
}

class _MusicListState extends State<MusicList> {
  final List<Map<String, dynamic>> dummyData = [
    {
      'id': '1',
      'title': '노래 제목 1노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 1노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'rank': '1',
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 성수 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'id': '2',
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'rank': '2',
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'id': '2',
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'rank': '2',
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'id': '2',
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'rank': '2',
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'id': '2',
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'rank': '2',
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
  ];

  String trimText(String text, int limit) {
    return text.length > limit ? '${text.substring(0, limit)}...' : text;
  }

  final PageController _pageController = PageController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          ClockTime(),
          SizedBox(height: 5),
          Text(
            '내 주변 인기 음악',
            style: TextStyle(fontSize: 13),
          ),
          Row(
            children: [
              IconButton(
                icon: Icon(Icons.arrow_back, color: Colors.white),
                onPressed: () => Navigator.of(context).pop(),
              ),
              MusicPageView(
                pageController: _pageController,
                dummyData: dummyData,
              ),
            ],
          ),
        ],
      ),
    );
  }
}
