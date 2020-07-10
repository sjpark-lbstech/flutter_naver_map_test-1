package kr.co.lbstech.flutter_naver_map_test;

import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.CircleOverlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircleOverlayController {
    private final NaverMap naverMap;
    private final NaverMapListeners listener;
    private final CircleOverlayBuilder builder = new CircleOverlayBuilder();

    private HashMap<String, CircleOverlay> idToCircleOverlay = new HashMap<>();

    CircleOverlayController(NaverMap naverMap, NaverMapListeners listener){
        this.naverMap = naverMap;
        this.listener = listener;
    };

    void setInitialCircleOverlays(List circles){
        if (circles == null) return;
        builder.add(circles);
    }

    void add(List circlesToAdd){
        if (circlesToAdd == null) return;
        builder.add(circlesToAdd);
    }

    void remove(List circleIdsToRemove){
        if (circleIdsToRemove == null) return;
        builder.removeById(circleIdsToRemove);
    }

    void modify(List circlesToChange){
        if (circlesToChange == null) return;
        builder.modify(circlesToChange);
    }


    // ===================================== inner class ==========================================

    @SuppressWarnings("unchecked")
    private class CircleOverlayBuilder{

        void add(List circles){
            for (Object obj : circles){
                Map<String, Object> json = (Map<String, Object>) obj;
                CircleOverlay circleOverlay = build(json);
                circleOverlay.setMap(naverMap);

                String circleId = (String)circleOverlay.getTag();
                idToCircleOverlay.put(circleId, circleOverlay);
            }
        }

        void removeById(List circleIdsToRemove){
            for(Object obj : circleIdsToRemove){
                String id = (String) obj;
                if (idToCircleOverlay.containsKey(id)){
                    CircleOverlay circle = idToCircleOverlay.get(id);
                    if (circle != null) {
                        circle.setOnClickListener(null);
                        circle.setMap(null);
                    }
                    idToCircleOverlay.remove(id);
                }
            }
        }

        void modify(List circlesToChange){
            for(Object obj : circlesToChange){
                Map<String, Object> json = (Map<String, Object>) obj;
                CircleOverlay current = build(json);
                current.setMap(naverMap);

                String id = (String) json.get("overlayId");
                if (idToCircleOverlay.containsKey(id)){
                    CircleOverlay prev = idToCircleOverlay.get(id);
                    if (prev != null) prev.setMap(null);
                }

                idToCircleOverlay.put(id, current);
            }
        }

        CircleOverlay build(Map<String, Object> json){
            if (!json.containsKey("overlayId") ||
                    !json.containsKey("center") || !json.containsKey("radius"))
                throw new AssertionError();

            CircleOverlay circleOverlay = new CircleOverlay();
            circleOverlay.setOnClickListener(listener);

            circleOverlay.setTag(json.get("overlayId"));
            circleOverlay.setCenter(Convert.toLatLng(json.get("center")));
            circleOverlay.setRadius((double) json.get("radius"));

            if(json.containsKey("color"))
                circleOverlay.setColor((int)json.get("color"));
            if(json.containsKey("outlineColor"))
                circleOverlay.setOutlineColor((int)json.get("outlineColor"));
            if(json.containsKey("outlineWidth"))
                circleOverlay.setOutlineWidth((int)json.get("outlineWidth"));
            if(json.containsKey("zIndex"))
                circleOverlay.setZIndex((int)json.get("zIndex"));
            if(json.containsKey("globalZIndex"))
                circleOverlay.setGlobalZIndex(((int)json.get("globalZIndex")) + 1);
            if(json.containsKey("isVisible"))
                circleOverlay.setVisible((boolean)json.get("isVisible"));
            if(json.containsKey("minZoom"))
                circleOverlay.setMinZoom((int)json.get("minZoom"));
            if(json.containsKey("maxZoom"))
                circleOverlay.setMaxZoom((int)json.get("maxZoom"));

            return  circleOverlay;
        }
    }
}
