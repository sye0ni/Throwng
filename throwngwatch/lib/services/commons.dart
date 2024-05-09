import 'package:dio/dio.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import '../store/store.dart';

class ApiClient {
  late Dio dio;
  var userManager = UserManager();

  ApiClient() {
    dio = Dio();
    userManager.loadUserInfo();
    dio.options.baseUrl = "https://throwng.store/api";
    dio.options.headers['Content-Type'] = 'application/json';
    dio.interceptors.add(InterceptorsWrapper(onRequest: (options, handler) async {
      final accessToken = await userManager.accessToken;

      if (accessToken != null) {
        options.headers["Authorization"] = "$accessToken";
      }
      return handler.next(options);
    }));
  }

// GET 요청
  Future<Response> get(String path, {dynamic queryParameters}) async {
    return dio.get(path, queryParameters: queryParameters);
  }

  // POST 요청
  Future<Response> post(String path, {dynamic data}) async {
    return dio.post(path, data: data);
  }

  // PUT 요청
  Future<Response> put(String path, {dynamic data, Options? options}) async {
    return dio.put(path, data: data, options: options);
  }

  // DELETE 요청
  Future<Response> delete(String path, {dynamic data}) async {
    return dio.delete(path, data: data);
  }
}
