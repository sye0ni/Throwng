import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

class UserManager with ChangeNotifier {
  final FlutterSecureStorage storage = FlutterSecureStorage();

  String? jwtToken;

  static final UserManager _instance = UserManager._internal();

  factory UserManager() {
    return _instance;
  }
  UserManager._internal();

  Future<void> saveUserInfo({
    String? newJwtToken,
  }) async {
    jwtToken = newJwtToken ?? jwtToken;
    await storage.write(key: 'jwtToken', value: jwtToken);
    notifyListeners();
  }

  Future<void> loadUserInfo() async {
    jwtToken = await storage.read(key: 'jwtToken');
    notifyListeners();
  }

  Future<void> clearUserInfo() async {
    jwtToken = null;
    await storage.deleteAll();
    notifyListeners();
  }
}
