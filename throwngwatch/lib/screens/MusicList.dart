import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:throwngwatch/services/MusicListAPi.dart';
import 'package:wear/wear.dart';
import '../const/color.dart';
import '../widgets/ClockTime.dart';
import 'MusicPickDetail.dart';

class MusicList extends StatefulWidget {
  @override
  State<MusicList> createState() => _MusicListState();
}

class _MusicListState extends State<MusicList> {
  final List<Map<String, dynamic>> popularMusic = [
    {
      'dropId': 1,
      'title': '노래 제목 1노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 1노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 성수 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
    {
      'dropId': 2,
      'title': '노래 제목 2노래 제목 1노래 제목 1노래 제목 1',
      'artist': '아티스트 2노래 제목 1노래 제목 1노래 제목 1',
      'albumImage': "assets/images/albumImage.png",
      'youtubeId': 'WuUQX7Fz8SM',
      'comment': '전여친이랑 한강 공원 벤치에 앉아서 같이 에어팟 끼고 노래 들었던 게 엊그제 같아요 ㅎ',
    },
  ];
  final FlutterSecureStorage storage = FlutterSecureStorage();

  String trimText(String text, int limit) {
    return text.length > limit ? '${text.substring(0, limit)}...' : text;
  }

  PageController _pageController = PageController();
  int _currentPageIndex = 0;

  int get currentPageGroup => (_currentPageIndex ~/ 5);
  String _currentMusicTitleAndArtist = "";

  @override
  void initState() {
    super.initState();
    // loadUserInfo();
    _pageController = PageController(initialPage: _currentPageIndex);
  }

  Future<void> loadUserInfo() async {
    String? latStr = await storage.read(key: 'latitude');
    String? lonStr = await storage.read(key: 'longitude');

    double? latitude = latStr != null ? double.tryParse(latStr) : null;
    double? longitude = lonStr != null ? double.tryParse(lonStr) : null;

    if (latitude != null && longitude != null) {
      await fetchMusicList(latitude, longitude);
    } else {
      print("위도없다~~ 경도없다~~");
    }
  }

  Future<void> fetchMusicList(double lat, double lon) async {
    var res = await getMusicList(lat, lon);
    setState(() {
      popularMusic.addAll(res.data);
      Map<String, dynamic> initialMusic = popularMusic[0];
      _currentMusicTitleAndArtist = "${initialMusic['title']}-${initialMusic['artist']}";
    });
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  void _onPageChanged(int pageIndex) {
    setState(() {
      _currentPageIndex = pageIndex;
      Map<String, dynamic> currentMusic = popularMusic[pageIndex];
      _currentMusicTitleAndArtist = "${currentMusic['title']}-${currentMusic['artist']}";
    });
  }

  Widget buildDot({required int index, required int total}) {
    bool isInCurrentGroup = (index ~/ 5) == currentPageGroup;
    int lastDotIndexInGroup = ((currentPageGroup + 1) * 5) - 1;

    if (index == lastDotIndexInGroup) {
      return Icon(
        Icons.expand_more,
        color: MAIN_COLOR,
        size: 10,
      );
    } else if (isInCurrentGroup && (index + 1) % 5 == 0) {
      return Icon(
        Icons.expand_more,
        color: Colors.white,
        size: 10,
      );
    }

    return isInCurrentGroup
        ? Container(
            height: 7,
            width: 7,
            margin: EdgeInsets.symmetric(vertical: 3),
            decoration: BoxDecoration(
              color: _currentPageIndex % 5 == index % 5 ? MAIN_COLOR : Colors.grey,
              shape: BoxShape.circle,
            ),
          )
        : Container();
  }

  @override
  Widget build(BuildContext context) {
    return WatchShape(
      builder: (BuildContext context, WearShape shape, Widget? child) {
        return Scaffold(
          body: Center(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                ClockTime(),
                Text(
                  '내 주변 인기 음악',
                  style: TextStyle(fontSize: 13),
                ),
                SizedBox(height: 5),
                Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    IconButton(
                      icon: Icon(Icons.arrow_back, color: Colors.white),
                      onPressed: () => Navigator.of(context).pop(),
                    ),
                    SizedBox(
                      child: Container(
                        height: 110,
                        width: 110,
                        child: PageView.builder(
                          itemCount: popularMusic.isEmpty ? 1 : popularMusic.length,
                          scrollDirection: Axis.vertical,
                          controller: _pageController,
                          onPageChanged: _onPageChanged,
                          itemBuilder: (context, dropId) {
                            if (popularMusic.isEmpty) {
                              return Center(
                                child: Text('비어있는 리스트입니다.', style: TextStyle(fontSize: 12, color: Colors.white)),
                              );
                            }
                            return GestureDetector(
                              onTap: () {
                                Navigator.of(context).push(MaterialPageRoute(
                                  builder: (context) => MusicPickDetail(
                                    musicData: popularMusic[dropId],
                                  ),
                                ));
                              },
                              child: Container(
                                height: 110,
                                width: 110,
                                child: ClipRRect(
                                  borderRadius: BorderRadius.circular(100),
                                  child: Image.asset(
                                    popularMusic[dropId]['albumImage'],
                                    fit: BoxFit.cover,
                                  ),
                                ),
                              ),
                            );
                          },
                        ),
                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: List.generate(popularMusic.length, (index) => buildDot(index: index, total: popularMusic.length)),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 5),
                Text(
                  trimText(_currentMusicTitleAndArtist, 14),
                  style: TextStyle(fontSize: 10),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
