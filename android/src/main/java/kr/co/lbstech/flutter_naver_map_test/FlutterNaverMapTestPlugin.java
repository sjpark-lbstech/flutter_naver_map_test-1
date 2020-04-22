package kr.co.lbstech.flutter_naver_map_test;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterNaverMapTestPlugin */
public class FlutterNaverMapTestPlugin implements
        FlutterPlugin,
        Application.ActivityLifecycleCallbacks,
        LifecycleObserver,
        ActivityAware{
  static final int CREATED = 1;
  static final int STARTED = 2;
  static final int RESUMED = 3;
  static final int PAUSED = 4;
  static final int STOPPED = 5;
  static final int SAVE_INSTANCE_STATE = 6;
  static final int DESTROYED = 7;

  private final AtomicInteger state = new AtomicInteger(0);
  private int registrarActivityHashCode;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityPluginBinding;
//  private Lifecycle lifecycle;

  // =============================== constructor =======================================

  private FlutterNaverMapTestPlugin(Activity activity){
    this.registrarActivityHashCode = activity.hashCode();
  }

  public FlutterNaverMapTestPlugin(){}

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    if(registrar.activity() == null){
      // 백그라운드에서 플러그인을 등록하려고 시도할때 엑티비티는 존재하지 않습니다.
      // 이 플러그인이 포어그라운드에서만 돌아가기 때문에 백그라운드에서 등록하는 것을 막습니다.
      return;
    }
    FlutterNaverMapTestPlugin plugin = new FlutterNaverMapTestPlugin(registrar.activity());
    registrar.activity().getApplication().registerActivityLifecycleCallbacks(plugin);
    // 라이프사이클 콜백
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

  // ========================== DefaultLifeCycleObserver =================================

//  @Override
//  public void onCreate(@NonNull LifecycleOwner owner) {
//    state.set(CREATED);
//  }
//
//  @Override
//  public void onStart(@NonNull LifecycleOwner owner) {
//    state.set(STARTED);
//  }
//
//  @Override
//  public void onResume(@NonNull LifecycleOwner owner) {
//    state.set(RESUMED);
//  }
//
//  @Override
//  public void onPause(@NonNull LifecycleOwner owner) {
//    state.set(PAUSED);
//  }
//
//  @Override
//  public void onStop(@NonNull LifecycleOwner owner) {
//    state.set(STOPPED);
//  }
//
//  @Override
//  public void onDestroy(@NonNull LifecycleOwner owner) {
//    state.set(DESTROYED);
//  }


  // ========================== ActivityAware =================================


  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    activityPluginBinding = binding;
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);

//    HiddenLifecycleReference hiddenLifecycleReference = (HiddenLifecycleReference) binding.getLifecycle();
//    Lifecycle lifecycle = hiddenLifecycleReference.getLifecycle();
//    lifecycle.addObserver(this);

    androidx.lifecycle.Lifecycle lifecycleX = (androidx.lifecycle.Lifecycle) binding.getLifecycle();
    lifecycleX.addObserver(this);

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




    // ======================== Lifecycle Observer ===========================

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void onCreate(){
      state.set(CREATED);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void onStart(){
        state.set(STARTED);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume(){
        state.set(RESUMED);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause(){
        state.set(PAUSED);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void onStop(){
        state.set(STOPPED);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy(){
        state.set(DESTROYED);
    }

}
