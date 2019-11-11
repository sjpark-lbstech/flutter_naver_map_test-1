package kr.co.lbstech.flutter_naver_map_test;

import android.graphics.PointF;
import android.location.Location;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.Symbol;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;

public class NaverMapListeners implements
        NaverMap.OnMapClickListener,
        NaverMap.OnMapLongClickListener,
        NaverMap.OnMapDoubleTapListener,
        NaverMap.OnMapTwoFingerTapListener,
        NaverMap.OnSymbolClickListener,
        NaverMap.OnCameraChangeListener,
        NaverMap.OnCameraIdleListener {

    // member variable
    private final MethodChannel channel;
    private static boolean originalBehaviorDisable = false;


    NaverMapListeners(MethodChannel channel){
        this.channel = channel;
    }

    static void setOriginalBehaviorDisable(boolean originalBehaviorDisable){
        NaverMapListeners.originalBehaviorDisable = originalBehaviorDisable;
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onTap", arguments);
    }

    @Override
    public boolean onMapDoubleTap(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onDoubleTap", arguments);
        return originalBehaviorDisable;
    }

    @Override
    public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onLongTap", arguments);
    }

    @Override
    public boolean onMapTwoFingerTap(@NonNull PointF pointF, @NonNull LatLng latLng) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(latLng));
        channel.invokeMethod("map#onTwoFingerTap", arguments);
        return originalBehaviorDisable;
    }

    @Override
    public boolean onSymbolClick(@NonNull Symbol symbol) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("position", Convert.latLngToJson(symbol.getPosition()));
        arguments.put("caption", symbol.getCaption());
        channel.invokeMethod("map#onSymbolClick", arguments);
        return false;
    }


    @Override
    public void onCameraChange(int i, boolean b) {
        channel.invokeMethod("camera#move", null);
    }

    @Override
    public void onCameraIdle() {
        channel.invokeMethod("camera#idle", null);
    }
}
