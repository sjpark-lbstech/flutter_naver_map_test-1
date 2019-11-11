package kr.co.lbstech.flutter_naver_map_test_example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
      int result = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
      if(result != PackageManager.PERMISSION_GRANTED){
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0xaa);
      }
    }
  }
}
