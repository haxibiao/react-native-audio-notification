package com.haxifang.reactnativeaudionotification;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.module.annotations.ReactModule;

@ReactModule(name = AudioNotificationModule.REACT_CLASS)
public class AudioNotificationModule extends ReactContextBaseJavaModule {
  public static final String REACT_CLASS = "RNAudioNotification";
  public static final String TAG = REACT_CLASS;

  ReactApplicationContext mContext;

  public AudioNotificationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    mContext = reactContext;
  }

  @Override
  @NonNull
  public String getName() {
    return REACT_CLASS;
  }

  @ReactMethod
  public void initAudioNotification(ReadableMap options) {
    String cover = options.hasKey("cover") ? options.getString("cover") : null;
    String title = options.hasKey("title") ? options.getString("title") : null;
    String description = options.hasKey("description") ? options.getString("description") : null;
    boolean isLike = options.hasKey("is_like") && options.getBoolean("is_like");
    boolean isPlay = options.hasKey("is_play") && options.getBoolean("is_play");

    if (cover == null || title == null || description == null) {
      // 异常，配置错误
      throw new RuntimeException("AudioNotification init Configs parameter Missing.");
    }

    AudioNotifyHelper audioNotify = AudioNotifyHelper.getInstance(mContext);
    audioNotify.setReactContext(mContext);

    // 更新数据
    AudioNotifyHelper.AudioConfigData configData = new AudioNotifyHelper.AudioConfigData(title, cover, description,
        isLike, isPlay);
    audioNotify.setConfigData(configData);
  }

  @ReactMethod
  public void setAudioConfig(ReadableMap options) {
    AudioNotifyHelper audioNotify = AudioNotifyHelper.getInstance(mContext);
    AudioNotifyHelper.AudioConfigData configData = audioNotify.getConfigData();

    // 设置封面图
    String cover = options.hasKey("cover") ? options.getString("cover") : null;
    if (cover != null) {
      configData.setCover(cover);
    }

    // 设置标题
    String title = options.hasKey("title") ? options.getString("title") : null;
    if (title != null) {
      configData.setTitle(title);
    }

    // 设置描述
    String description = options.hasKey("description") ? options.getString("description") : null;
    if (description != null) {
      configData.setDescription(description);
    }

    // 设置喜欢状态
    boolean isLike = options.hasKey("is_like") && options.getBoolean("is_like");
    configData.setLike(isLike);

    // 设置播放状态
    boolean isPlay = options.hasKey("is_play") && options.getBoolean("is_play");
    configData.setPlay(isPlay);

    // 更新数据
    audioNotify.setConfigData(configData);
  }

  @ReactMethod
  public void toNotify() {
    AudioNotifyHelper audioNotify = AudioNotifyHelper.getInstance(mContext);
    if (audioNotify.getConfigData() == null) {
      // 异常，配置错误
      throw new RuntimeException("AudioNotification Need to first init.");
    }
    audioNotify.toNotify();
  }

  @ReactMethod
  public void toCancel() {
    AudioNotifyHelper audioNotify = AudioNotifyHelper.getInstance(mContext);
    if (audioNotify.getConfigData() == null) {
      // 异常，配置错误
      throw new RuntimeException("AudioNotification Need to first init.");
    }
    audioNotify.toCancel();
  }

  @ReactMethod
  public void toUpdate() {
    AudioNotifyHelper audioNotify = AudioNotifyHelper.getInstance(mContext);
    if (audioNotify.getConfigData() == null) {
      // 异常，配置错误
      throw new RuntimeException("AudioNotification Need to first init.");
    }
    audioNotify.toUpdate();
  }
}
