import 'package:flutter/material.dart';
import '../const/color.dart';
import '../widgets/ClockTime.dart';
import '../widgets/ElevateBTN.dart';

class MusicDrop extends StatefulWidget {
  final Map<String, dynamic> musicData;

  const MusicDrop({super.key, required this.musicData});

  @override
  State<MusicDrop> createState() => _MusicDropState();
}

class _MusicDropState extends State<MusicDrop> {
  String trimText(String text, int limit) {
    return text.length > limit ? '${text.substring(0, limit)}...' : text;
  }

  String userComment = '';
  late TextEditingController commentController;

  @override
  void initState() {
    super.initState();
    commentController = TextEditingController(text: userComment);
  }

  @override
  void dispose() {
    commentController.dispose();
    super.dispose();
  }

  Future<void> handlePress() async {
    print(widget.musicData);
    print(userComment);
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
                      trimText(widget.musicData['title'], 8),
                      style: TextStyle(fontSize: 12),
                      overflow: TextOverflow.ellipsis,
                    ),
                    Text(
                      trimText(widget.musicData['artist'], 8),
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
              SizedBox(
                width: 157,
                height: 80,
                child: Container(
                  padding: EdgeInsets.symmetric(horizontal: 5),
                  decoration: BoxDecoration(
                    color: Color(0xff1F2127),
                    borderRadius: BorderRadius.circular(5),
                  ),
                  child: TextField(
                    controller: commentController,
                    maxLength: 50,
                    onChanged: (value) {
                      setState(() {
                        userComment = value.trim();
                      });
                    },
                    style: TextStyle(
                      fontSize: 13,
                      color: Colors.white,
                    ),
                    decoration: InputDecoration(
                      border: InputBorder.none,
                      hintStyle: TextStyle(color: PLACEHOLDER),
                      hintText: '떠오르는 말을 적어보세요',
                    ),
                    textInputAction: TextInputAction.done,
                    onSubmitted: (value) {
                      setState(() {
                        userComment = value;
                      });
                      FocusScope.of(context).unfocus();
                    },
                  ),
                ),
              ),
            ],
          ),
          SizedBox(height: 10),
          CustomElevatedButton(
            text: '쓰롱하기',
            onPressed: userComment.isNotEmpty ? () => handlePress() : null,
            backgroundColor: BTN_COLOR,
            textColor: Colors.white,
          ),
        ],
      ),
    );
  }
}
