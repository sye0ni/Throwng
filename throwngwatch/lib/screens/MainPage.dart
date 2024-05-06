import 'package:flutter/material.dart';
import 'package:throwngwatch/const/color.dart';
import 'package:wear/wear.dart';
import '../widgets/ClockTime.dart';
import 'MusicList.dart';
import 'MyPlayList.dart';

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<MainPage> createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  PageController _pageController = PageController();
  int _currentPageIndex = 0;

  @override
  void initState() {
    super.initState();
    _pageController = PageController(initialPage: _currentPageIndex);
  }

  @override
  void dispose() {
    _pageController.dispose();
    super.dispose();
  }

  void _onPageChanged(int pageIndex) {
    setState(() {
      _currentPageIndex = pageIndex;
    });
  }

  @override
  Widget build(BuildContext context) {
    return WatchShape(
      builder: (BuildContext context, WearShape shape, Widget? child) {
        return Scaffold(
          body: Column(
            children: [
              ClockTime(),
              SizedBox(height: 5),
              Expanded(
                child: PageView(
                  controller: _pageController,
                  onPageChanged: _onPageChanged,
                  children: <Widget>[
                    MusicList(),
                    MyPlayList(),
                  ],
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(2, (index) {
                  return Container(
                    margin: const EdgeInsets.all(4),
                    width: 7,
                    height: 7,
                    decoration: BoxDecoration(
                      shape: BoxShape.circle,
                      color: _currentPageIndex == index ? MAIN_COLOR : Colors.grey,
                    ),
                  );
                }),
              ),
            ],
          ),
        );
      },
    );
  }
}
