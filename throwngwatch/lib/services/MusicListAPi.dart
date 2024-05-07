import 'package:dio/dio.dart';
import 'package:throwngwatch/services/commons.dart';

final api = ApiClient();

Future<Response> getMusicList(lat, lon) async {
  try {
    final res = await api.get('/watch/throw-items?lat=${lat}&lon=${lon}');
    return res;
  } catch (e) {
    print(e);
    throw (e);
  }
}

Future<Response> getMyPlayList() async {
  try {
    final res = await api.get('/watch/playlists');
    return res;
  } catch (e) {
    print(e);
    throw (e);
  }
}

Future<Response?> postThrowng(data) async {
  try {
    final res = await api.post('/watch/throw-items', data: data);
    return res;
  } on DioError catch (e) {
    if (e.response != null) {
      print(e.response!.data);
      if (e.response!.data['code'] == 'Song_400_2') {
        return Response(
          requestOptions: RequestOptions(path: ''),
          data: {'error': 'Song_400_2', 'message': 'The number of songs assigned per day is over'},
          statusCode: 400,
        );
      }
    }
    return null;
  } catch (e) {
    print(e);
    return null;
  }
}
