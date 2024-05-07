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
          SizedBox(height: 5),
          Text(trimText('${widget.musicData['title']} - ${widget.musicData['artist']}', 10)),
          SizedBox(height: 5),
          Row(
            children: [
              IconButton(
                icon: Icon(Icons.arrow_back, color: Colors.white),
                onPressed: () => Navigator.of(context).pop(),
              ),
              Container(
                width: 130,
                height: 85,
                padding: EdgeInsets.all(5),
                decoration: BoxDecoration(
                  color: Color(0xff1F2127),
                  borderRadius: BorderRadius.circular(5),
                ),
                child: Text(
                  widget.musicData['comment'],
                  style: TextStyle(
                    fontSize: 10,
                  ),
                  maxLines: 4,
                  overflow: TextOverflow.ellipsis,
                ),
              ),
            ],
          ),
          SizedBox(height: 5),
          CustomElevatedButton(
            text: '바로 듣기',
            onPressed: () async {
              goMusic();
            },
            backgroundColor: BTN_COLOR,
            textColor: Colors.white,
            icon: Image.asset(
              'assets/images/youtubeIcon.png',
              width: 15,
              height: 15,
            ),
          )
        ],
      ),
    );
  }
}
