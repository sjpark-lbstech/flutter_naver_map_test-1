package kr.co.lbstech.flutter_naver_map_test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterNaverMapTestPlugin */
public class FlutterNaverMapTestPlugin implements
        FlutterPlugin,
        Application.ActivityLifecycleCallbacks,
        ActivityAware{
  static final int CREATED = 1;
  static final int STARTED = 2;
  static final int RESUMED = 3;
  static final int PAUSED = 4;
  static final int STOPPED = 5;
  static final int SAVE_INSTANCE_STATE = 6;
  static final int DESTROYED = 7;

  private final AtomicInteger state = new AtomicInteger(0);
  private final int registrarActivityHashCode;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityPluginBinding;

  private FlutterNaverMapTestPlugin(Registrar registrar) {
    this.registrarActivityHashCode = registrar.activity().hashCode();
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    if(registrar.activity() == null){
      // 백그라운드에서 플러그인을 등록하려고 시도할때 엑티비티는 존재하지 않습니다.
      // 이 플러그인이 포어그라운드에서만 돌아가기 때문에 백그라운드에서 등록하는 것을 막습니다.
      return;
    }
    FlutterNaverMapTestPlugin plugin = new FlutterNaverMapTestPlugin(registrar);
    registrar.activity().getApplication().registerActivityLifecycleCallbacks(plugin);
    registrar
          .platformViewRegistry()
          .registerViewFactory(
                  "flutter_naver_map_test",
                  new NaverMapFactory(
                          plugin.state,
                          registrar.messenger(),
                          registrar.activity()
                  ));
  }

  // ===================== FlutterPlugin =========================

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    pluginBinding = binding;
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    pluginBinding = null;
  }


  // =================== ActivityLifeCycleCallBack ===================
  @Override
  public void onActivityCreated(Activity activity, Bundle bundle) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(CREATED);
  }

  @Override
  public void onActivityStarted(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(STARTED);
  }

  @Override
  public void onActivityResumed(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(RESUMED);
  }

  @Override
  public void onActivityPaused(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(PAUSED);
  }

  @Override
  public void onActivityStopped(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(STOPPED);
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(SAVE_INSTANCE_STATE);
  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    activity.getApplication().unregisterActivityLifecycleCallbacks(this);
    state.set(DESTROYED);
  }


  // ========================== ActivityAware =================================


  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    activityPluginBinding = binding;
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);
    pluginBinding
            .getPlatformViewRegistry()
            .registerViewFactory(
                    "flutter_naver_map_test",
                    new NaverMapFactory(
                            state,
                            pluginBinding.getBinaryMessenger(),
                            binding.getActivity()
                    ));
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activityPluginBinding.getActivity().getApplication().unregisterActivityLifecycleCallbacks(this);
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    activityPluginBinding = binding;
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onDetachedFromActivity() {
    activityPluginBinding.getActivity().getApplication().unregisterActivityLifecycleCallbacks(this);
  }
}
