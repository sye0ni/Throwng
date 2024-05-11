import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/widgets.dart';
import 'package:throwngwatch/const/color.dart';
import 'package:wear/wear.dart';
import '../widgets/ClockTime.dart';
import 'LocationService.dart';
import 'MusicListPage.dart';
import 'MyPlayListPage.dart';

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
    LocationService.initialize();
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
              Expanded(
                child: Stack(
                  alignment: AlignmentDirectional.centerStart,
                  children: [
                    PageView(
                      controller: _pageController,
                      onPageChanged: _onPageChanged,
                      scrollDirection: Axis.vertical,
                      children: <Widget>[
                        MusicListPage(),
                        MyPlayListPage(),
                      ],
                    ),
                    Padding(
                      padding: const EdgeInsets.all(8.0),
                      child: Column(
                        mainAxisSize: MainAxisSize.min,
                        children: List.generate(2, (index) => buildDot(index: index)),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  Widget buildDot({required int index}) {
    return Container(
      height: 8,
      width: 8,
      margin: EdgeInsets.symmetric(vertical: 4),
      decoration: BoxDecoration(
        color: _currentPageIndex == index ? MAIN_COLOR : Colors.grey,
        shape: BoxShape.circle,
      ),
    );
  }
}
