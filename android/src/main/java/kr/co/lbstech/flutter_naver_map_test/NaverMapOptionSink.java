package kr.co.lbstech.flutter_naver_map_test;

import java.util.List;

public interface NaverMapOptionSink {
    void setNightModeEnable(boolean nightModeEnable);

    void setLiteModeEnable(boolean liteModeEnable);

    void setIndoorEnable(boolean indoorEnable);

    void setMapType(int typeIndex);

    void setBuildingHeight(double buildingHeight);

    void setSymbolScale(double symbolScale);

    void setSymbolPerspectiveRatio (double symbolPerspectiveRatio);

    void setActiveLayers(List activeLayers);

    void setRotationGestureEnable(boolean rotationGestureEnable);

    void setScrollGestureEnable(boolean scrollGestureEnable);

    void setTiltGestureEnable(boolean tiltGestureEnable);

    void setZoomGestureEnable(boolean zoomGestureEnable);

    void setLocationButtonEnable(boolean locationButtonEnable);

    void setLocationTrackingMode(int locationTrackingMode);

}
