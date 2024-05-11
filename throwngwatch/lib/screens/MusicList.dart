import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:throwngwatch/services/MusicListAPi.dart';
import 'package:wear/wear.dart';
import '../const/color.dart';
import '../store/store.dart';
import '../widgets/ClockTime.dart';
import 'MusicPickDetail.dart';

class MusicList extends StatefulWidget {
  @override
  State<MusicList> createState() => _MusicListState();
}

class _MusicListState extends State<MusicList> {
  final List<Map<String, dynamic>> popularMusic = [];
  final FlutterSecureStorage storage = FlutterSecureStorage();
  bool isLoading = true;

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
    loadUserInfo();
    _pageController = PageController(initialPage: _currentPageIndex);
  }

  Future<void> loadUserInfo() async {
    setState(() {
      isLoading = true;
    });
    var userManager = UserManager();
    await userManager.loadUserInfo();
    double? latitude = await userManager.latitude;
    double? longitude = await userManager.longitude;
    print('33333: ${latitude}');
    print('33333: ${longitude}');
    setState(() {
      isLoading = false;
    });

    if (latitude != null && longitude != null) {
      await fetchMusicList(latitude, longitude);
    } else {
      print("위도없다~~ 경도없다~~");
    }
  }

  Future<void> fetchMusicList(double lat, double lon) async {
    try {
      final res = await getMusicList(lat, lon);
      print(res);
      if (mounted) {
        setState(() {
          final List<Map<String, dynamic>> tempList = List<Map<String, dynamic>>.from(res.data.map((item) => Map<String, dynamic>.from(item)));
          popularMusic.addAll(tempList);
          if (popularMusic.isNotEmpty) {
            Map<String, dynamic> initialMusic = popularMusic[0];
            _currentMusicTitleAndArtist = "${initialMusic['title']}-${initialMusic['name']}";
          }
        });
      }
    } catch (e) {
      print(e);
    }
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
      _currentMusicTitleAndArtist = "${currentMusic['title']}-${currentMusic['name']}";
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
            child: isLoading
                ? CircularProgressIndicator()
                : Column(
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
                                itemBuilder: (context, index) {
                                  if (popularMusic.isEmpty) {
                                    return Center(
                                      child: Text('비어있는 리스트입니다.', style: TextStyle(fontSize: 12, color: Colors.white)),
                                    );
                                  }
                                  return GestureDetector(
                                    onTap: () {
                                      Navigator.of(context).push(MaterialPageRoute(
                                        builder: (context) => MusicPickDetail(
                                          musicData: popularMusic[index],
                                        ),
                                      ));
                                    },
                                    child: Container(
                                      height: 110,
                                      width: 110,
                                      child: ClipRRect(
                                        borderRadius: BorderRadius.circular(100),
                                        child: CachedNetworkImage(
                                          imageUrl: popularMusic[index]['albumImage'],
                                          placeholder: (context, url) => CircularProgressIndicator(),
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
