import 'package:flutter/material.dart';
import '../const/color.dart';
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

  final ScrollController _scrollController = ScrollController();

  String trimText(String text, int limit) {
    return text.length > limit ? '${text.substring(0, limit)}...' : text;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              '내 주변 인기 음악',
              style: TextStyle(fontSize: 13, fontWeight: FontWeight.bold),
            ),
            SizedBox(height: 10),
            Expanded(
              child: ListView.builder(
                controller: _scrollController,
                itemCount: dummyData.length,
                itemBuilder: (context, index) {
                  return GestureDetector(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => MusicPickDetail(
                            musicData: dummyData[index],
                          ),
                        ),
                      );
                    },
                    child: Center(
                      child: Container(
                        margin: EdgeInsets.only(bottom: 8),
                        decoration: BoxDecoration(
                          color: Color(0xff1F2127),
                          borderRadius: BorderRadius.circular(5),
                        ),
                        height: 45,
                        child: Row(
                          children: <Widget>[
                            Padding(
                              padding: const EdgeInsets.all(5),
                              child: Image(
                                image: AssetImage(dummyData[index]['albumImage']),
                                width: 35,
                                height: 35,
                              ),
                            ),
                            Expanded(
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    trimText(dummyData[index]['title'], 14),
                                    style: TextStyle(fontSize: 12),
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                  Text(
                                    trimText(dummyData[index]['artist'], 15),
                                    style: TextStyle(
                                      color: PLACEHOLDER,
                                      fontSize: 10,
                                    ),
                                    overflow: TextOverflow.ellipsis,
                                  ),
                                ],
                              ),
                            )
                          ],
                        ),
                      ),
                    ),
                  );
                },
              ),
            ),
            SizedBox(height: 10),
          ],
        ),
      ),
    );
  }
}
