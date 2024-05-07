import 'package:flutter/material.dart';

class MusicPageView extends StatelessWidget {
  final PageController pageController;
  final List<Map<String, dynamic>> dummyData;

  const MusicPageView({
    Key? key,
    required this.pageController,
    required this.dummyData,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Expanded(
      child: PageView.builder(
        controller: pageController,
        scrollDirection: Axis.vertical,
        itemCount: dummyData.length,
        itemBuilder: (context, index) {
          return GestureDetector(
            onVerticalDragUpdate: (details) {
              if (details.delta.dy < 0) {
                pageController.nextPage(duration: Duration(milliseconds: 300), curve: Curves.easeIn);
              } else if (details.delta.dy > 0) {
                pageController.previousPage(duration: Duration(milliseconds: 300), curve: Curves.easeOut);
              }
            },
            child: Container(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Image.asset(
                    dummyData[index]['albumImage']!,
                    width: 176,
                    height: 176,
                  ),
                  SizedBox(height: 8),
                  Text(
                    '${dummyData[index]['title']} - ${dummyData[index]['artist']}',
                    style: TextStyle(fontSize: 14),
                    overflow: TextOverflow.ellipsis,
                    maxLines: 1,
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
