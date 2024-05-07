import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class UserManager with ChangeNotifier {
  final FlutterSecureStorage storage = FlutterSecureStorage();

  String? jwtToken;
  double? latitude;
  double? longitude;

  static final UserManager _instance = UserManager._internal();

  factory UserManager() {
    return _instance;
  }
  UserManager._internal();

  Future<void> saveUserInfo({
    String? newJwtToken,
    double? newLatitude,
    double? newLongitude,
  }) async {
    jwtToken = newJwtToken ?? jwtToken;
    latitude = newLatitude ?? latitude;
    longitude = newLongitude ?? longitude;

    await storage.write(key: 'jwtToken', value: jwtToken);
    if (latitude != null) await storage.write(key: 'latitude', value: latitude.toString());
    if (longitude != null) await storage.write(key: 'longitude', value: longitude.toString());

    notifyListeners();
  }

  Future<void> loadUserInfo() async {
    jwtToken = await storage.read(key: 'jwtToken');
    String? latStr = await storage.read(key: 'latitude');
    String? lonStr = await storage.read(key: 'longitude');

    latitude = latStr != null ? double.tryParse(latStr) : null;
    longitude = lonStr != null ? double.tryParse(lonStr) : null;

    notifyListeners();
  }

  Future<void> clearUserInfo() async {
    jwtToken = null;
    latitude = null;
    longitude = null;
    await storage.deleteAll();
    notifyListeners();
  }
}
