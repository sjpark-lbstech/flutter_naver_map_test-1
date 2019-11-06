package kr.co.lbstech.flutter_naver_map_test;

import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.CREATED;
import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.STARTED;
import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.RESUMED;
import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.PAUSED;
import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.STOPPED;
import static kr.co.lbstech.flutter_naver_map_test.FlutterNaverMapTestPlugin.DESTROYED;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.platform.PlatformView;

public class NaverMapController implements
        PlatformView,
        OnMapReadyCallback,
        MethodChannel.MethodCallHandler,
        Application.ActivityLifecycleCallbacks,
        NaverMapOptionSink,
        Overlay.OnClickListener {

    int id;
    private final AtomicInteger activityState;
    private final PluginRegistry.Registrar registrar;
    private final MapView mapView;
    private NaverMap naverMap;
    private final MethodChannel methodChannel;
    private boolean disposed = false;
    private final int registrarActivityHashCode;
    private MethodChannel.Result mapReadyResult;
    private List initialMarkers;
    private HashMap<String, Object> markers = new HashMap<>();
    private NaverMapListeners listeners;

    NaverMapController(
            int id,
            Context context,
            AtomicInteger activityState,
            PluginRegistry.Registrar registrar,
            NaverMapOptions options,
            List initialMarkers){
        this.id = id;
        this.registrar = registrar;
        this.mapView = new MapView(context, options);
        this.activityState = activityState;
        registrarActivityHashCode = registrar.activity().hashCode();
        methodChannel = new MethodChannel(registrar.messenger(), "flutter_naver_map_test_"+ id);
        methodChannel.setMethodCallHandler(this);
        listeners = new NaverMapListeners(methodChannel);
        this.initialMarkers = initialMarkers;
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        if(mapReadyResult != null){
            mapReadyResult.success(null);
            mapReadyResult = null;
        }
        // 맵 완전히 만들어진 이후에 마커 추가.
        setInitialMarkers();
        naverMap.setOnMapClickListener(listeners);
        naverMap.setOnMapLongClickListener(listeners);
        naverMap.setOnMapDoubleTapListener(listeners);
        naverMap.setOnMapTwoFingerTapListener(listeners);
        naverMap.setOnSymbolClickListener(listeners);
    }

    @Override
    public View getView() {
        return mapView;
    }

    void init(){
        switch (activityState.get()) {
            case STOPPED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                mapView.onStop();
                break;
            case PAUSED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                mapView.onPause();
                break;
            case RESUMED:
                mapView.onCreate(null);
                mapView.onStart();
                mapView.onResume();
                break;
            case STARTED:
                mapView.onCreate(null);
                mapView.onStart();
                break;
            case CREATED:
                mapView.onCreate(null);
                break;
            case DESTROYED:
                // Nothing to do, the activity has been completely destroyed.
                break;
            default:
                throw new IllegalArgumentException(
                        "Cannot interpret " + activityState.get() + " as an activity state");
        }
        registrar.activity().getApplication().registerActivityLifecycleCallbacks(this);
        mapView.getMapAsync(this);
    }

    @Override
    public void dispose() {
        if(disposed) return;
        disposed = true;
        methodChannel.setMethodCallHandler(null);
        mapView.onDestroy();
        registrar.activity().getApplication().unregisterActivityLifecycleCallbacks(this);
    }

    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method){
            case "map#waitFoeMap":
                {
                    if(naverMap != null){
                        result.success(null);
                        return;
                    }
                    mapReadyResult = result;
                }
                break;
            case "map#update" :
                {
                    Convert.carveMapOptions(this, (Map<String, Object>) methodCall.argument("options"));
                    result.success(true);
                }
                break;
            case "map#getVisibleRegion":
                {
                    if (naverMap != null) {
                        LatLngBounds latLngBounds = naverMap.getContentBounds();
                        result.success(Convert.latlngBoundsToJson(latLngBounds));
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "map#getPosition":
                {
                    if (naverMap != null) {
                        CameraPosition position = naverMap.getCameraPosition();
                        result.success(Convert.cameraPositionToJson(position));
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "camera#move" :
                {
                    if (naverMap != null) {
                        CameraUpdate update = Convert.toCameraUpdate(methodCall.argument("cameraUpdate"));
                        naverMap.moveCamera(update);
                        result.success(null);
                    } else result.error("네이버맵 초기화 안됨.",
                            "네이버 지도가 생성되기 전에 이 메서드를 사용할 수 없습니다.",
                            null);
                }
                break;
            case "markers#update":
                {
                    List markersToAdd = methodCall.argument("markersToAdd");
                    for (Object data : markersToAdd) {
                        Map<String, Object> markerData = (Map<String, Object>) data;
                        Marker marker = MarkerBuilder.build(markerData);
                        if(markerData.containsKey("onMarkerTab"))
                            marker.setOnClickListener(this);
                        markers.put(MarkerBuilder.getMarkerId(marker), marker);
                        marker.setMap(naverMap);
                    }
                    List markersToChange = methodCall.argument("markersToChange");
                    for (Object data : markersToChange) {
                        Map<String, Object> markerData = (Map<String, Object>) data;
                        Marker currentMarker = MarkerBuilder.build(markerData);
                        String markerId = MarkerBuilder.getMarkerId(currentMarker);
                        if (markers.containsKey(markerId)) {
                            Marker previousMarker = (Marker) markers.get(markerId);
                            previousMarker.setMap(null);
                            if(markerData.containsKey("onMarkerTab"))
                                currentMarker.setOnClickListener(this);
                            currentMarker.setMap(naverMap);
                            markers.put(markerId, currentMarker);
                        }
                    }
                    List markerIdsToRemove = methodCall.argument("markerIdsToRemove");
                    for (Object data : markerIdsToRemove) {
                        String markerId = (String) data;
                        if (markers.containsKey(markerId)) {
                            Marker markerToRemove = (Marker) markers.get(markerId);
                            markerToRemove.setMap(null);
                            markers.remove(markerId);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onCreate(bundle);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onStart();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onResume();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onPause();
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onStop();
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (disposed || activity.hashCode() != registrarActivityHashCode) {
            return;
        }
        mapView.onDestroy();
    }

    @Override
    public void setNightModeEnable(boolean nightModeEnable) {
        naverMap.setNightModeEnabled(nightModeEnable);
    }

    @Override
    public void setLiteModeEnable(boolean liteModeEnable) {
        naverMap.setLiteModeEnabled(liteModeEnable);
    }

    @Override
    public void setIndoorEnable(boolean indoorEnable) {
        naverMap.setIndoorEnabled(indoorEnable);
    }

    @Override
    public void setMapType(int typeIndex) {
        NaverMap.MapType type;
        switch(typeIndex){
            case 1:
                type = NaverMap.MapType.Navi;
                break;
            case 2:
                type = NaverMap.MapType.Satellite;
                break;
            case 3:
                type = NaverMap.MapType.Hybrid;
                break;
            case 4:
                type = NaverMap.MapType.Terrain;
                break;
            default:
                type = NaverMap.MapType.Basic;
                break;
        }
        naverMap.setMapType(type);
    }

    @Override
    public void setBuildingHeight(double buildingHeight) {
        naverMap.setBuildingHeight((float) buildingHeight);
    }

    @Override
    public void setSymbolScale(double symbolScale) {
        naverMap.setSymbolScale((float) symbolScale);
    }

    @Override
    public void setSymbolPerspectiveRatio(double symbolPerspectiveRatio) {
        naverMap.setSymbolPerspectiveRatio((float) symbolPerspectiveRatio);
    }

    @Override
    public void setActiveLayers(List activeLayers) {
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, false);
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, false);
        for(int i = 0; i < activeLayers.size(); i ++){
            int layerIndex = (int) activeLayers.get(i);
            switch (layerIndex){
                case 0:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true);
                    break;
                case 1:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRAFFIC, true);
                    break;
                case 2:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true);
                    break;
                case 3:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);
                    break;
                case 4:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_MOUNTAIN, true);
                    break;
                case 5:
                    naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, true);
                    break;
            }
        }
    }

    @Override
    public void setRotationGestureEnable(boolean rotationGestureEnable) {}
    @Override
    public void setScrollGestureEnable(boolean scrollGestureEnable) {}
    @Override
    public void setTiltGestureEnable(boolean tiltGestureEnable) {}
    @Override
    public void setZoomGestureEnable(boolean zoomGestureEnable) {}
    @Override
    public void setLocationButtonEnable(boolean locationButtonEnable) {}

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if(overlay instanceof Marker){
            String markerId = MarkerBuilder.getMarkerId((Marker) overlay);
            methodChannel.invokeMethod("marker#onTap", markerId);
            return MarkerBuilder.getConsumeTabEvent((Marker) overlay);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void setInitialMarkers() {
        if(naverMap != null){
            for(Object data : initialMarkers){
                Map<String, Object> markerJson = (Map<String, Object>) data;
                Marker marker = MarkerBuilder.build(markerJson);
                if(markerJson.containsKey("onMarkerTab"))
                    marker.setOnClickListener(this);
                String markerId = (String) markerJson.get("markerId");
                markers.put(markerId, marker);
            }
            Set<String> markerIds = markers.keySet();
            for(String key : markerIds){
                Marker marker = (Marker) markers.get(key);
                if(marker != null) marker.setMap(naverMap);
            }
        }
    }

}
