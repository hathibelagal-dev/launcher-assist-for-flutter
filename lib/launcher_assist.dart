import 'package:flutter/services.dart';

class LauncherAssist {
  static const MethodChannel _channel = const MethodChannel('launcher_assist');

  /// Returns a list of apps installed on the user's device
  static getAllApps() async {
    var data = await _channel.invokeMethod('getAllApps');
    return data;
  }

  /// Launches an app using its package name
  static launchApp(String packageName) {
    _channel.invokeMethod("launchApp", {"packageName": packageName});
  }

  /// Gets you the current wallpaper on the user's device. This method
  /// needs the READ_EXTERNAL_STORAGE permission on Android Oreo.
  static getWallpaper() async {
    var data = await _channel.invokeMethod('getWallpaper');
    return data;
  }
}
