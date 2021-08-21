package com.haxifang.reactnativeaudionotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.util.concurrent.ExecutionException;

public class AudioNotificationService extends Service {

  private static final String TAG = "tzmax";
  AudioNotificationReceiver notifyReceiver;

  @Override
  public void onCreate() {
    super.onCreate();

    notifyReceiver = new AudioNotificationReceiver();

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".LIKE");
    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".LAST");
    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".PLAY");
    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".NEXT");
    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".CLOSE");

    // 注册全局广播
    registerReceiver(notifyReceiver, intentFilter);
    // Log.d(TAG, "onCreate: 启动服务");
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // 关闭服务，卸载全局广播
    unregisterReceiver(notifyReceiver);
  }


}
