package com.progur.launcherassist;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * LauncherAssistPlugin
 */
public class LauncherAssistPlugin implements MethodCallHandler {

  private byte[] wallpaperData = null;
  private PluginRegistry.Registrar registrar;

  public LauncherAssistPlugin(Registrar registrar) {
        this.registrar = registrar;
  }

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "launcher_assist");
    channel.setMethodCallHandler(new LauncherAssistPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall methodCall, Result result) {
      if(methodCall.method.equals("getAllApps")) {
          getAllApps(result);
      } else if(methodCall.method.equals("launchApp")) {
          launchApp(methodCall.argument("packageName").toString());
      } else if(methodCall.method.equals("getWallpaper")) {
          getWallpaper(result);
      }
  }

  private void getWallpaper(MethodChannel.Result result) {
      if(wallpaperData != null) {
          result.success(wallpaperData);
          return;
      }

      WallpaperManager wallpaperManager = WallpaperManager.getInstance(registrar.context());
      Drawable wallpaperDrawable = wallpaperManager.getDrawable();
      if(wallpaperDrawable instanceof BitmapDrawable) {
          wallpaperData = convertToBytes(((BitmapDrawable)wallpaperDrawable).getBitmap(),
                                                Bitmap.CompressFormat.JPEG, 100);
          result.success(wallpaperData);
      }
  }

  private void launchApp(String packageName) {
      Intent i = registrar.context().getPackageManager().getLaunchIntentForPackage(packageName);
      if(i != null)
        registrar.context().startActivity(i);
  }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

  private void getAllApps(MethodChannel.Result result) {

      Intent intent = new Intent(Intent.ACTION_MAIN, null);
      intent.addCategory(Intent.CATEGORY_LAUNCHER);

      PackageManager manager = registrar.context().getPackageManager();
      List<ResolveInfo> resList = manager.queryIntentActivities(intent, 0);

      List<Map<String, Object>> _output = new ArrayList<>();

      for (ResolveInfo resInfo : resList) {
          try {
              ApplicationInfo app = manager.getApplicationInfo(
                      resInfo.activityInfo.packageName, PackageManager.GET_META_DATA);
              if (manager.getLaunchIntentForPackage(app.packageName) != null) {

                  byte[] iconData = convertToBytes(getBitmapFromDrawable(app.loadIcon(manager)),
                          Bitmap.CompressFormat.PNG, 100);

                  Map<String, Object> current = new HashMap<>();
                  current.put("label", app.loadLabel(manager).toString());
                  current.put("icon", iconData);
                  current.put("package", app.packageName);
                  _output.add(current);
              }
          } catch(Exception e) {
              e.printStackTrace();
          }
      }

      result.success(_output);
  }

  public static byte[] convertToBytes(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
    image.compress(compressFormat, quality, byteArrayOS);
    return byteArrayOS.toByteArray();
  }
}
