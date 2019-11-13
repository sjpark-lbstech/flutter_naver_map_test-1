package kr.co.lbstech.flutter_naver_map_test;

import android.annotation.SuppressLint;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PolylineOverlay;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.view.FlutterMain;

public class Convert {

    @SuppressWarnings("ConstantConditions")
    static void carveMapOptions(NaverMapOptionSink sink, Map<String, Object> options) {
        if (options.containsKey("mapType"))
            sink.setMapType((Integer) options.get("mapType"));
        if (options.containsKey("liteModeEnable"))
            sink.setLiteModeEnable((Boolean) options.get("liteModeEnable"));
        if (options.containsKey("nightModeEnable"))
            sink.setNightModeEnable((Boolean) options.get("nightModeEnable"));
        if (options.containsKey("indoorEnable"))
            sink.setIndoorEnable((Boolean) options.get("indoorEnable"));
        if (options.containsKey("buildingHeight"))
            sink.setBuildingHeight((Double) options.get("buildingHeight"));
        if (options.containsKey("symbolScale"))
            sink.setSymbolScale((Double) options.get("symbolScale"));
        if (options.containsKey("symbolPerspectiveRatio"))
            sink.setSymbolPerspectiveRatio((Double) options.get("symbolPerspectiveRatio"));
        if (options.containsKey("activeLayers"))
            sink.setActiveLayers((List) options.get("activeLayers"));
        if (options.containsKey("scrollGestureEnable"))
            sink.setScrollGestureEnable((Boolean) options.get("scrollGestureEnable"));
        if (options.containsKey("zoomGestureEnable"))
            sink.setZoomGestureEnable((Boolean) options.get("zoomGestureEnable"));
        if (options.containsKey("rotationGestureEnable"))
            sink.setRotationGestureEnable((Boolean) options.get("rotationGestureEnable"));
        if (options.containsKey("tiltGestureEnable"))
            sink.setTiltGestureEnable((Boolean) options.get("tiltGestureEnable"));
        if (options.containsKey("locationButtonEnable"))
            sink.setLocationButtonEnable((Boolean) options.get("locationButtonEnable"));
        if (options.containsKey("originalBehaviorDisable"))
            NaverMapListeners.setOriginalBehaviorDisable((Boolean) options.get("originalBehaviorDisable"));
    }

    @SuppressWarnings("unchecked")
    static LatLng toLatLng(Object o) {
        final List<Double> data = (List<Double>) o;
        return new LatLng(data.get(0), data.get(1));
    }

    static LatLngBounds toLatLngBounds(Object o) {
        if (o == null) {
            return null;
        }
        final List data = (List) o;
        return new LatLngBounds(toLatLng(data.get(0)), toLatLng(data.get(1)));
    }

    @SuppressLint("Assert")
    static CameraPosition toCameraPosition(Map<String, Object> cameraPositionMap) {
        double bearing, tilt, zoom;
        bearing = (double) cameraPositionMap.get("bearing");
        tilt = (double) cameraPositionMap.get("tilt");
        zoom = (double) cameraPositionMap.get("zoom");

        List target = (List) cameraPositionMap.get("target");
        assert target != null && target.size() == 2;
        double lat = (double) target.get(0);
        double lng = (double) target.get(1);
        return new CameraPosition(new LatLng(lat, lng), zoom, tilt, bearing);
    }

    @SuppressWarnings("unchecked")
    static CameraUpdate toCameraUpdate(Object o) {
        final List data = (List) o;
        switch ((String) data.get(0)) {
            case "newCameraPosition":
                Map<String, Object> positionMap = (Map<String, Object>) data.get(1);
                return CameraUpdate.toCameraPosition(toCameraPosition(positionMap));
            case "scrollTo":
                return CameraUpdate.scrollTo(toLatLng(data.get(1)));
            case "zoomIn":
                return CameraUpdate.zoomIn();
            case "zoomOut":
                return CameraUpdate.zoomOut();
            case "zoomBy":
                double amount = (double) data.get(1);
                return CameraUpdate.zoomBy(amount);
            case "zoomTo":
                double level = (double) data.get(1);
                return CameraUpdate.zoomTo(level);
            case "fitBounds":
                return CameraUpdate.fitBounds(toLatLngBounds(data.get(1)));
            default:
                throw new IllegalArgumentException("Cannot interpret " + o + " as CameraUpdate");
        }
    }

    static Object cameraPositionToJson(CameraPosition position) {
        if (position == null) {
            return null;
        }
        final Map<String, Object> data = new HashMap<>();
        data.put("bearing", position.bearing);
        data.put("target", latLngToJson(position.target));
        data.put("tilt", position.tilt);
        data.put("zoom", position.zoom);
        return data;
    }

    static Object latlngBoundsToJson(LatLngBounds latLngBounds) {
        final Map<String, Object> arguments = new HashMap<>(2);
        arguments.put("southwest", latLngToJson(latLngBounds.getSouthWest()));
        arguments.put("northeast", latLngToJson(latLngBounds.getNorthEast()));
        return arguments;
    }

    static Object latLngToJson(LatLng latLng) {
        return Arrays.asList(latLng.latitude, latLng.longitude);
    }

    static float toFloat(Object o) {
        return ((Number) o).floatValue();
    }

    static OverlayImage toOverlayImage(Object o) {
        List data = (List) o;
        String assetName = (String) data.get(0);
        String key = FlutterMain.getLookupKeyForAsset(assetName);
        return OverlayImage.fromAsset(key);
    }

    private static List<LatLng> toCoords(Object o) {
        final List<?> data = (List) o;
        final List<LatLng> points = new ArrayList<>(data.size());

        for (Object ob : data) {
            final List<?> point = (List) ob;
            points.add(new LatLng(toFloat(point.get(0)), toFloat(point.get(1))));
        }
        return points;
    }

    private static List<Integer> toPattern(Object o) {
        final List<?> data = (List) o;
        final List<Integer> pattern = new ArrayList<>(data.size());

        for (Object item : data) {
            pattern.add((int) item);
        }

        return pattern;
    }

    @NotNull
    public static String interpretPolyline(@NotNull Object o, @NotNull PolylineSink sink) {
        final Map<?, ?> data = (Map) o;
        final Object color = data.get("color");
        if (color != null) {
            sink.setColor((Number) color);
        }
        final Object endCap = data.get("endCap");
        if (endCap != null) {
            sink.setLineCap(PolylineOverlay.LineCap.values()[(int) endCap]);
        }
        final Object jointType = data.get("jointType");
        if (jointType != null) {
            sink.setJointType(PolylineOverlay.LineJoin.values()[(int) jointType]);
        }
        final Object visible = data.get("visible");
        if (visible != null) {
            sink.setVisible((boolean) visible);
        }
        final Object width = data.get("width");
        if (width != null) {
            sink.setWidth((int) width);
        }
        final Object globalZIndex = data.get("globalZIndex");
        if (globalZIndex != null) {
            sink.setGlobalZIndex((int) globalZIndex);
        }
        final Object coords = data.get("coords");
        if (coords != null) {
            sink.setCoords(toCoords(coords));
        }
        final Object pattern = data.get("pattern");
        if (pattern != null) {
            sink.setPattern(toPattern(pattern));
        }
        final String polylineId = (String) data.get("polylineOverlayId");
        if (polylineId == null) {
            throw new IllegalArgumentException("polylineOverlayId was null");
        } else {
            return polylineId;
        }
    }

    @NotNull
    public static String interpretPath(@NotNull Object o, @NotNull PathSink sink) {
        final Map<?, ?> data = (Map) o;

        final Object coords = data.get("coords");
        if (coords != null) {
            sink.setCoords(toCoords(coords));
        }

        final Object globalZIndex = data.get("globalZIndex");
        if (globalZIndex != null) {
            sink.setGlobalZIndex((int) globalZIndex);
        }

        final Object collidedCaptions = data.get("collidedCaptions");
        if (collidedCaptions != null) {
            sink.setHideCollidedCaptions((boolean) collidedCaptions);
        }

        final Object collidedMarkers = data.get("collidedMarkers");
        if (collidedMarkers != null) {
            sink.setHideCollidedMarkers((boolean) collidedMarkers);
        }

        final Object collidedSymbols = data.get("collidedSymbols");
        if (collidedSymbols != null) {
            sink.setHideCollidedSymbols((boolean) collidedSymbols);
        }

        final Object visible = data.get("visible");
        if (visible != null) {
            sink.setVisible((boolean) visible);
        }

        final Object color = data.get("color");
        if (color != null) {
            sink.setColor((Number) color);
        }

        final Object outlineColor = data.get("outlineColor");
        if (outlineColor != null) {
            sink.setOutlineColor((Number) outlineColor);
        }

        final Object passedColor = data.get("passedColor");
        if (passedColor != null) {
            sink.setPassedColor((Number) passedColor);
        }

        final Object passedOutlineColor = data.get("passedOutlineColor");
        if (passedOutlineColor != null) {
            sink.setPassedOutlineColor((Number) passedOutlineColor);
        }

        final Object patternImage = data.get("patternImage");
        if (patternImage != null) {
            sink.setPatternImage((byte[]) patternImage);
        }

        final Object patternInterval = data.get("patternInterval");
        if (patternInterval != null) {
            sink.setPatternInterval((int) patternInterval);
        }

        final Object progress = data.get("progress");
        if (progress != null) {
            sink.setProgress((double) progress);
        }

        final Object width = data.get("width");
        if (width != null) {
            sink.setWidth((int) width);
        }

        final Object outlineWidth = data.get("outlineWidth");
        if (outlineWidth != null) {
            sink.setOutlineWidth((int) outlineWidth);
        }

        final String pathId = (String) data.get("pathOverlayId");
        if (pathId == null) {
            throw new IllegalArgumentException("pathOverlayId was null");
        } else {
            return pathId;
        }
    }
}
