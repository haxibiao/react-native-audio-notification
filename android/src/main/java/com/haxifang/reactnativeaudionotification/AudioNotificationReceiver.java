package com.haxifang.reactnativeaudionotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AudioNotificationReceiver extends BroadcastReceiver {
  Context mContext;
  String TAG = "tzmax";

  @Override
  public void onReceive(Context context, Intent intent) {
    mContext = context;

    // 获取广播意图判断是否和音频通知栏操作意图相同
    int action_id = intent.getIntExtra("action", 0);
    // Log.d(TAG, "onReceive: 点击了通知栏啦");

    if (action_id == AudioNotifyHelper.TO_ACTION_LIKE_ID) {
      // 点击的是喜欢图标
      // Log.d(TAG, "onReceive: 点击了喜欢");
      sendEvent("onClickLike", getConfig("onClickLike"));
    } else if (action_id == AudioNotifyHelper.TO_ACTION_BACK_ID) {
      // 点击的是上一首图标
      // Log.d(TAG, "onReceive: 点击了上一首");
      sendEvent("onClickLast", getConfig("onClickLast"));
    } else if (action_id == AudioNotifyHelper.TO_ACTION_PLAY_ID) {
      // 点击的是播放图标
      // Log.d(TAG, "onReceive: 点击了播放");
      sendEvent("onClickPlay", getConfig("onClickPlay"));
    } else if (action_id == AudioNotifyHelper.TO_ACTION_NEXT_ID) {
      // 点击的是下一首图标
      // Log.d(TAG, "onReceive: 点击了下一首");
      sendEvent("onClickNext", getConfig("onClickNext"));

    } else if (action_id == AudioNotifyHelper.TO_ACTION_CLOSE_ID) {
      // 点击的是关闭图标
      // Log.d(TAG, "onReceive: 点击了关闭");
      sendEvent("onClickClose", getConfig("onClickClose"));
    }


  }

  // 注册回调监听方法，将点击事件抛回 ReactNative 中
  private void sendEvent(String eventName, WritableMap params) {
    if (AudioNotifyHelper.mReactContext == null) {
      return;
    }
    AudioNotifyHelper.mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
  }

  // 获取数据，随回调函数返回
  private WritableMap getConfig() {
    return getConfig(null);
  }

  private WritableMap getConfig(String action) {
    if (AudioNotifyHelper.mReactContext == null) {
      return null;
    }
    AudioNotifyHelper.AudioConfigData configData = AudioNotifyHelper.getInstance(mContext).getConfigData();
    WritableMap params = Arguments.createMap();

    if (action != null && !action.equals("")) {
      params.putString("action", action);
    }
    params.putString("title", configData.getTitle());
    params.putString("cover", configData.getCover());
    params.putString("description", configData.getDescription());
    params.putBoolean("is_like", configData.isLike());
    params.putBoolean("is_play", configData.isPlay());

    return params;
  }
}
