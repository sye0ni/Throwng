import 'package:flutter/material.dart';
import 'package:wear/wear.dart';
import '../const/color.dart';
import '../services/MusicListAPi.dart';
import '../widgets/ClockTime.dart';
import 'MusicDrop.dart';

class MyPlayList extends StatefulWidget {
  const MyPlayList({super.key});

  @override
  State<MyPlayList> createState() => _MyPlayListState();
}

class _MyPlayListState extends State<MyPlayList> {
  final List<Map<String, dynamic>> myPlayList = [
    {
      "playlistId": 11,
      "title": "Magnetic",
      "artist": "아일릿",
      "albumImage": "https://popcon-s3-bucket.s3.ap-southeast-2.amazonaws.com/throwng/superrealme.jpg",
      "youtubeId": "69q7FmDh1JXekNKOOxDmdM",
      "previewUrl": "String"
    },
    {
      "playlistId": 11,
      "title": "Magnetic",
      "artist": "아일릿",
      "albumImage": "https://popcon-s3-bucket.s3.ap-southeast-2.amazonaws.com/throwng/superrealme.jpg",
      "youtubeId": "69q7FmDh1JXekNKOOxDmdM",
      "previewUrl": "String"
    },
  ];

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
    // fetchMyPlayList();
    _pageController = PageController(initialPage: _currentPageIndex);
    Map<String, dynamic> initialMusic = myPlayList[0];
    _currentMusicTitleAndArtist = "${initialMusic['title']}-${initialMusic['artist']}";
  }

  Future<void> fetchMyPlayList() async {
    var res = await getMyPlayList();
    setState(() {
      myPlayList.addAll(res.data);
      Map<String, dynamic> initialMusic = myPlayList[0];
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
      Map<String, dynamic> currentMusic = myPlayList[pageIndex];
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
                  '내 플레이리스트',
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
                        child: myPlayList.isEmpty
                            ? Center(child: Text('플레이리스트가 비어있습니다.'))
                            : PageView.builder(
                                itemCount: myPlayList.length,
                                scrollDirection: Axis.vertical,
                                controller: _pageController,
                                onPageChanged: _onPageChanged,
                                itemBuilder: (context, playlistId) {
                                  return GestureDetector(
                                    onTap: () {
                                      Navigator.of(context).push(
                                        MaterialPageRoute(
                                          builder: (context) => MusicDrop(
                                            musicData: myPlayList[playlistId],
                                          ),
                                        ),
                                      );
                                    },
                                    child: Container(
                                      height: 110,
                                      width: 110,
                                      child: ClipRRect(
                                        borderRadius: BorderRadius.circular(100),
                                        child: Image.network(
                                          myPlayList[playlistId]['albumImage'],
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
                        children: List.generate(myPlayList.length, (index) => buildDot(index: index, total: myPlayList.length)),
                      ),
                    ),
                  ],
                ),
                SizedBox(height: 5),
                Text(
                  trimText(_currentMusicTitleAndArtist, 12),
                  style: TextStyle(fontSize: 12),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}
