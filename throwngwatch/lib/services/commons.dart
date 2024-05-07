import 'package:dio/dio.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import '../store/store.dart';

// 기본
class ApiClient {
  late Dio dio;
  var userManager = UserManager();

  ApiClient() {
    dio = Dio();
    userManager.loadUserInfo();
    // dio.options.baseUrl = dotenv.env['BASE_URL']!;
    dio.options.baseUrl = "http://10.0.2.2:8080/api";
    dio.options.headers['Content-Type'] = 'application/json';
    dio.interceptors.add(InterceptorsWrapper(onRequest: (options, handler) async {
      final jwtToken = await userManager.jwtToken;

      if (jwtToken != null) {
        options.headers["Authorization"] = "$jwtToken";
      }
      return handler.next(options);
    }));
  }

  Future<Response> get(String path, {dynamic queryParameters}) async {
    return dio.get(path, queryParameters: queryParameters);
  }

  Future<Response> post(String path, {dynamic data}) async {
    return dio.post(path, data: data);
  }

  Future<Response> put(String path, {dynamic data, Options? options}) async {
    return dio.put(path, data: data, options: options);
  }

  Future<Response> delete(String path, {dynamic data}) async {
    return dio.delete(path, data: data);
  }
}
