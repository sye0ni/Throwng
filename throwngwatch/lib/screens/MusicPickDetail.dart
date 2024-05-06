import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../const/color.dart';
import '../widgets/ClockTime.dart';
import '../widgets/ElevateBTN.dart';

class MusicPickDetail extends StatefulWidget {
  final Map<String, dynamic> musicData;

  const MusicPickDetail({super.key, required this.musicData});

  @override
  State<MusicPickDetail> createState() => _MusicPickDetailState();
}

String trimText(String text, int limit) {
  return text.length > limit ? '${text.substring(0, limit)}...' : text;
}

class _MusicPickDetailState extends State<MusicPickDetail> {
  void goMusic() async {
    final url = 'https://music.youtube.com/watch?v=${widget.musicData['youtubeId']}';
    if (await canLaunch(url)) {
      await launch(url);
    } else {
      print('Could not launch $url');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          ClockTime(),
          SizedBox(height: 10),
          Container(
            child: Row(
              children: [
                SizedBox(width: 40),
                Image(
                  image: AssetImage(widget.musicData['albumImage']),
                  width: 35,
                  height: 35,
                ),
                SizedBox(width: 5),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      trimText(widget.musicData['title'], 10),
                      style: TextStyle(fontSize: 12),
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      trimText(widget.musicData['artist'], 10),
                      style: TextStyle(
                        color: PLACEHOLDER,
                        fontSize: 10,
                      ),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ],
                )
              ],
            ),
          ),
          SizedBox(height: 10),
          Row(
            children: [
              IconButton(
                icon: Icon(Icons.arrow_back, color: Colors.white),
                onPressed: () => Navigator.of(context).pop(),
              ),
              Container(
                width: 157,
                padding: EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Color(0xff1F2127),
                  borderRadius: BorderRadius.circular(5),
                ),
                child: Text(
                  widget.musicData['comment'],
                  style: TextStyle(
                    fontSize: 13,
                  ),
                  maxLines: 2,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          SizedBox(height: 10),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              CustomElevatedButton(
                text: '바로 듣기',
                onPressed: () async {
                  goMusic();
                },
                backgroundColor: BTN_COLOR,
                textColor: Colors.white,
                icon: Image.asset(
                  'assets/images/youtubeIcon.png',
                  width: 20,
                  height: 20,
                ),
              ),
              SizedBox(width: 5),
              CustomElevatedButton(
                text: '줍기',
                onPressed: () async {
                  print(widget.musicData);
                },
                backgroundColor: BTN_COLOR,
                textColor: Colors.white,
              ),
            ],
          )
        ],
      ),
    );
  }
}
