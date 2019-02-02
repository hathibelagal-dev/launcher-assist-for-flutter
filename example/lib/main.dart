// Copyright 2017 Ashraff Hathibelagal.
// Use of this source code is governed by an Apache license that can be
// found in the LICENSE file.

import 'package:flutter/material.dart';
import 'package:launcher_assist/launcher_assist.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  var numberOfInstalledApps;
  var installedApps;
  var wallpaper;

  @override
  initState() {
    super.initState();

    // Get all apps
    LauncherAssist.getAllApps().then((apps) {
      setState(() {
        numberOfInstalledApps = apps.length;
        installedApps = apps;
      });
    });

    // Get wallpaper as binary data
    LauncherAssist.getWallpaper().then((imageData) {
      setState(() {
        wallpaper = imageData;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
          appBar: new AppBar(
            title: new Text('Launcher Assist'),
          ),
          body: new Column(children: <Widget>[
            new Text("Found $numberOfInstalledApps apps installed"),
            new RaisedButton(
                child: new Text("Launch Something"),
                onPressed: () {
                  // Launch the first app available
                  LauncherAssist.launchApp(installedApps[0]["package"]);
                }),
            wallpaper != null
                ? new Image.memory(wallpaper, fit: BoxFit.scaleDown)
                : new Center()
          ])),
    );
  }
}
