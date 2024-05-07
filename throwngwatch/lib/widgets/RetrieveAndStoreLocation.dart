import 'package:location/location.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';

Future<void> retrieveAndStoreLocation() async {
  Location location = new Location();
  final FlutterSecureStorage secureStorage = FlutterSecureStorage();

  bool _serviceEnabled;
  PermissionStatus _permissionGranted;
  LocationData _locationData;

  _serviceEnabled = await location.serviceEnabled();
  if (!_serviceEnabled) {
    _serviceEnabled = await location.requestService();
    if (!_serviceEnabled) {
      return;
    }
  }

  _permissionGranted = await location.hasPermission();
  if (_permissionGranted == PermissionStatus.denied) {
    _permissionGranted = await location.requestPermission();
    if (_permissionGranted != PermissionStatus.granted) {
      return;
    }
  }

  _locationData = await location.getLocation();

  if (_locationData.latitude != null && _locationData.longitude != null) {
    await secureStorage.write(key: 'latitude', value: _locationData.latitude.toString());
    print('11111111111: ${_locationData.latitude}');
    await secureStorage.write(key: 'longitude', value: _locationData.longitude.toString());
    print('222222222222: ${_locationData.longitude}');
  }
}
