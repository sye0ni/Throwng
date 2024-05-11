import 'package:cached_network_image/cached_network_image.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import '../const/color.dart';
import '../services/MusicListAPi.dart';
import '../store/store.dart';
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
  final FlutterSecureStorage storage = FlutterSecureStorage();

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
    loadUserInfo(context);
  }

  Future<void> loadUserInfo(BuildContext context) async {
    var userManager = UserManager();
    userManager.loadUserInfo();
    double? latitude = await userManager.latitude;
    double? longitude = await userManager.longitude;

    if (latitude != null && longitude != null) {
      final data = {
        'latitude': latitude,
        'longitude': longitude,
        'songId': widget.musicData['songId'],
        'comment': userComment,
      };
      await fetchThrowng(context, data);
    } else {
      print("위도없다~~ 경도없다~~");
    }
  }

  Future<void> fetchThrowng(BuildContext context, data) async {
    var res = await postThrowng(data);
    if (res?.statusCode == 204) {
      Navigator.of(context).pushNamedAndRemoveUntil('/', (Route<dynamic> route) => false);
    } else if (res?.statusCode == 400 && res?.data['error'] == 'Song_400_2') {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            title: Text("알림"),
            content: Text("하루 쓰롱개수를 초과하였습니다."),
            actions: <Widget>[
              TextButton(
                child: Text("확인"),
                onPressed: () {
                  Navigator.of(context).pop();
                  Navigator.of(context).pushNamedAndRemoveUntil('/', (Route<dynamic> route) => false);
                },
              ),
            ],
          );
        },
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          image: DecorationImage(
            image: CachedNetworkImageProvider(widget.musicData['albumImage']),
            fit: BoxFit.cover,
          ),
        ),
        child: Container(
          decoration: BoxDecoration(
            color: Colors.black.withOpacity(0.7),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              ClockTime(),
              SizedBox(height: 5),
              Text(trimText('${widget.musicData['title']} - ${widget.musicData['artist']}', 6)),
              SizedBox(height: 5),
              Row(
                children: [
                  IconButton(
                    icon: Icon(Icons.arrow_back, color: Colors.white),
                    onPressed: () => Navigator.of(context).pop(),
                  ),
                  SizedBox(
                    width: 130,
                    height: 85,
                    child: TextField(
                      controller: commentController,
                      maxLength: 50,
                      minLines: 1,
                      maxLines: 5,
                      onChanged: (value) {
                        setState(() {
                          userComment = value.trim();
                        });
                      },
                      style: TextStyle(
                        fontSize: 15,
                        color: Colors.white,
                      ),
                      decoration: InputDecoration(
                        // border: InputBorder,
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
                ],
              ),
              CustomElevatedButton(
                text: '쓰롱하기',
                onPressed: userComment.trim().isEmpty
                    ? null
                    : () async {
                        handlePress();
                      },
                backgroundColor: BTN_COLOR,
                textColor: Colors.white,
              )
            ],
          ),
        ),
      ),
    );
  }
}
