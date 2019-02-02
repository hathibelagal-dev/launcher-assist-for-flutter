# Launcher Assist

This is a Flutter plugin that gives you basic access to Android's `PackageManager` and `WallpaperManager` classes. As such, it is designed to help you build launchers for Android. Currently, it offers the following methods:

- `getAllApps()` - This method returns a map containing the labels, package names, and icons of all the launchable apps installed on a user's device. The icons are available as byte arrays.

- `launchApp()` - Takes a package name as its only argument. As its name suggests, it lets you launch apps.

- `getWallpaper()` - Returns the current wallpaper of the user, as a byte array that you can directly pass to the `Image.memory()` method. Note that on devices running Android Oreo or higher, this method will work only if your app has the `READ_EXTERNAL_STORAGE` permission.

### Usage

To use this plugin, add `launcher_assist` as a dependency in your pubspec.yaml file.

### Sample Code

```
import 'package:launcher_assist/launcher_assist.dart';

.
.
.

// Get all apps
LauncherAssist.getAllApps().then((apps) {
    setState(() {
        numberOfInstalledApps = apps.length;
        installedApps = apps;
    });
});
```
