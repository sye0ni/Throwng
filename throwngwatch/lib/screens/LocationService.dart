import 'package:flutter/services.dart';
import '../store/store.dart';

class LocationService {
  static const MethodChannel _channel = MethodChannel('com.example.throwngwatch/location');

  static void initialize() {
    _channel.setMethodCallHandler(_handleMethod);
  }

  static Future<void> _handleMethod(MethodCall call) async {
    print('latitude: ${call.arguments['latitude']}');
    print('longitude: ${call.arguments['longitude']}');
    switch (call.method) {
      case 'sendLocation':
        double? latitude = call.arguments['latitude'];
        double? longitude = call.arguments['longitude'];
        await UserManager().saveUserInfo(newLatitude: latitude, newLongitude: longitude);
        break;
      default:
        print('Unknown method ${call.method}');
    }
  }
}
