package io.adaptant.labs.flutter_windowmanager;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** FlutterWindowManagerPlugin */
public class FlutterWindowManagerPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;
  private Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_windowmanager");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
  }

  @Override
public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
  if (activity == null) {
    result.error("NO_ACTIVITY", "Plugin not attached to an activity", null);
    return;
  }

  Integer flag = call.argument("flag");
  if (flag == null) {
    result.error("INVALID_ARGUMENT", "'flag' argument is required and must be an integer.", null);
    return;
  }

  Window window = activity.getWindow();

  if (call.method.equals("addFlags")) {
    window.addFlags(flag);
    result.success(null);
  } else if (call.method.equals("clearFlags")) {
    window.clearFlags(flag);
    result.success(null);
  } else {
    result.notImplemented();
  }
}


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
  }
}
