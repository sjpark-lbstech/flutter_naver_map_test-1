package kr.co.lbstech.flutter_naver_map_test;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class NaverMapFactory extends PlatformViewFactory {
    private final PluginRegistry.Registrar pluginRegistrar;
    private final AtomicInteger activityState;

    public NaverMapFactory(AtomicInteger state, PluginRegistry.Registrar registrar) {
        super(StandardMessageCodec.INSTANCE);
        pluginRegistrar = registrar;
        activityState = state;
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public PlatformView create(Context context, int i, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        NaverMapBuilder builder = new NaverMapBuilder();

        if(params.containsKey("initialCameraPosition")) {
            Map<String, Object> initPosition = (Map<String, Object>) params.get("initialCameraPosition");
            builder.setInitialCameraPosition(initPosition);
        }
        if(params.containsKey("options")){
            Map<String, Object> options = (Map<String, Object>) params.get("options");
            if(options.containsKey("isDevMode")){
                boolean isDevMode = (boolean) options.get("isDevMode");
                builder.setDevMode(isDevMode);
            }
            Convert.carveMapOptions(builder, options);
        }
        if(params.containsKey("markersToAdd")){
            builder.setInitialMarkers((List) params.get("markersToAdd"));
        }
        return builder.build(i, context, activityState, pluginRegistrar);
    }
}
