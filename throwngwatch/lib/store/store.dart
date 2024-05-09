import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class UserManager with ChangeNotifier {
  final FlutterSecureStorage storage = FlutterSecureStorage();

  String? accessToken;
  String? refreshToken;
  double? latitude;
  double? longitude;

  static final UserManager _instance = UserManager._internal();

  factory UserManager() {
    return _instance;
  }

  UserManager._internal();

  Future<void> saveUserInfo({
    String? newAccessToken,
    String? newRefreshToken,
    double? newLatitude,
    double? newLongitude,
  }) async {
    accessToken = newAccessToken ?? accessToken;
    refreshToken = newRefreshToken ?? refreshToken;
    latitude = newLatitude ?? latitude;
    longitude = newLongitude ?? longitude;

    await storage.write(key: 'accessToken', value: accessToken);
    await storage.write(key: 'refreshToken', value: refreshToken);
    if (latitude != null) await storage.write(key: 'latitude', value: latitude.toString());
    if (longitude != null) await storage.write(key: 'longitude', value: longitude.toString());

    print('11111: ${latitude}');
    print('11111: ${longitude}');
    notifyListeners();
  }

  Future<void> loadUserInfo() async {
    accessToken = await storage.read(key: 'accessToken');
    refreshToken = await storage.read(key: 'refreshToken');
    String? latStr = await storage.read(key: 'latitude');
    String? lonStr = await storage.read(key: 'longitude');

    latitude = latStr != null ? double.tryParse(latStr) : null;
    longitude = lonStr != null ? double.tryParse(lonStr) : null;

    print('22222: ${latitude}');
    print('22222: ${longitude}');

    notifyListeners();
  }

  Future<void> clearUserInfo() async {
    accessToken = null;
    refreshToken = null;
    latitude = null;
    longitude = null;
    await storage.deleteAll();
    notifyListeners();
  }
}
