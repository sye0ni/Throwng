import 'package:flutter/material.dart';
import '../const/color.dart';
import 'MusicDrop.dart';

class MyPlayList extends StatefulWidget {
  const MyPlayList({super.key});

  @override
  State<MyPlayList> createState() => _MyPlayListState();
}

class _MyPlayListState extends State<MyPlayList> {
  final List<Map<String, dynamic>> dummyData = [
    {
      'id': '1',
      'title': '사랑하긴 했었나요 스쳐가는 인연이었나요 짧지않은 우리 함께했던 시간들이 자꾸 내 마음을 가둬두네',
      'artist': '잔나비',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'previewUrl': "미리듣기 url (없으면 null)"
    },
    {
      'id': '2',
      'title': '홀씨홀씨홀씨홀씨홀씨홀씨홀씨홀씨',
      'artist': '아이유아이유아이유아이유아이유아이유',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'previewUrl': "미리듣기 url (없으면 null)"
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
              '플레이리스트',
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
                          builder: (context) => MusicDrop(
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
          ],
        ),
      ),
    );
  }
}
