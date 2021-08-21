package com.haxifang.reactnativeaudionotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.facebook.react.bridge.ReactContext;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AudioNotifyHelper {
  private static final String TAG = "tzmax";
  private static AudioNotifyHelper instance;
  private Context mContext;
  public static ReactContext mReactContext;

  public static String NOTIFICATION_CHANNEL_ID;
  public static String INTENT_ACTION_LABEL;
  private int NOTIFY_ID;
  private int REQUEST_CODE = 0;
  private RemoteViews audioNotifyView;
  private Notification notify;
  private NotificationManager notifyManager;
  private AudioNotificationReceiver notifyReceiver;
  private AudioConfigData mConfigData; // 配置数据

  private Boolean mOngoing = true; // 通知不可清除
  private Boolean mShowWhen = false;
  private int mPriority = Notification.PRIORITY_MAX;
  private int mSmallIcon = R.color.catalyst_logbox_background;

  public static final int ROOT_VIEW_ID = R.layout.notification_audio_small; // 通知模版 ID
  public static final int TMP_INFO_COVER = R.id.image_cover;
  public static final int TMP_INFO_TITLE = R.id.text_01;
  public static final int TMP_INFO_DESCRIPTION = R.id.text_02;
  public static final int TO_ACTION_LIKE_ID = R.id.image_01;
  public static final int TO_ACTION_BACK_ID = R.id.image_02;
  public static final int TO_ACTION_PLAY_ID = R.id.image_03;
  public static final int TO_ACTION_NEXT_ID = R.id.image_04;
  public static final int TO_ACTION_CLOSE_ID = R.id.image_05;

  public static AudioNotifyHelper getInstance(Context context) {
    if (instance == null) {
      instance = new AudioNotifyHelper(context);
    }
    return instance;
  }

  private AudioNotifyHelper(Context context) {
    mContext = context;
    NOTIFICATION_CHANNEL_ID = mContext.getPackageName() + "_AudioNotify";
    INTENT_ACTION_LABEL = NOTIFICATION_CHANNEL_ID + ".musicnotificaion.TO";
    NOTIFY_ID = android.os.Process.myPid();
    // NOTIFY_ID = 10001;
    notifyManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
    notify = buildNotification(mContext);

    // 启动服务，通过服务将图标点击的回调广播注册到 App
    mContext.startService(new Intent(mContext, AudioNotificationService.class));

  }

  // 推送通知
  public void toNotify() {
    // notifyManager.notify(NOTIFY_ID, notify);
    toUpdate();
  }

  // 更新推送视图
  public void toUpdate() {
    this.updateView();
    this.notifyManager.notify(NOTIFICATION_CHANNEL_ID, NOTIFY_ID, notify);
  }

  // 关闭通知
  public void toCancel() {
    this.notifyManager.cancel(NOTIFICATION_CHANNEL_ID, NOTIFY_ID);
  }

  private Notification buildNotification(Context context) {

    Notification.Builder builder = null;

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      builder = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(mSmallIcon)
        .setOngoing(mOngoing)
        .setShowWhen(mShowWhen);
    } else {

      builder = new Notification.Builder(context)
        .setPriority(mPriority)
        .setSmallIcon(mSmallIcon)
        .setOngoing(mOngoing);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        builder.setShowWhen(mShowWhen);
      }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      setupNotificationChannel(context);
      builder.setChannelId(NOTIFICATION_CHANNEL_ID);
    }

    audioNotifyView = new RemoteViews(context.getPackageName(), ROOT_VIEW_ID);

    bindActionEvent(); // 注册通知视图上可点击图标的点击事件

    builder.setContent(audioNotifyView);

    return builder.build();
  }

  // 绑定可点击按钮点击意图，配合 AudioNotificationService
  private void bindActionEvent() {
//    int[] views = new int[]{
//      TO_ACTION_LIKE_ID, // 喜欢按钮点击事件
//      TO_ACTION_BACK_ID, // 上一首按钮点击事件
//      TO_ACTION_PLAY_ID, // 播放按钮点击事件
//      TO_ACTION_NEXT_ID, // 下一首按钮点击事件
//    };
//
//    for (int item : views) {
//      Intent intent = new Intent().setAction(INTENT_ACTION_LABEL);
//      intent.putExtra("action", item);
//      Log.d(TAG, "bindActionEvent: 初始化" + item);
//      setOnClickPendingIntent(item, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT));
//    }

//    notifyReceiver = new AudioNotificationReceiver();
//    IntentFilter intentFilter = new IntentFilter();
//    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".LIKE");
//    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".LAST");
//    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".PLAY");
//    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".NEXT");
//    intentFilter.addAction(AudioNotifyHelper.INTENT_ACTION_LABEL + ".CLOSE");
//    // 注册全局广播
//    mContext.getApplicationContext().registerReceiver(notifyReceiver, intentFilter);

    Intent intent1 = new Intent().setAction(INTENT_ACTION_LABEL + ".LIKE");
    intent1.putExtra("action", TO_ACTION_LIKE_ID);
    setOnClickPendingIntent(TO_ACTION_LIKE_ID, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent1, PendingIntent.FLAG_CANCEL_CURRENT));

    Intent intent2 = new Intent().setAction(INTENT_ACTION_LABEL + ".LAST");
    intent2.putExtra("action", TO_ACTION_BACK_ID);
    setOnClickPendingIntent(TO_ACTION_BACK_ID, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent2, PendingIntent.FLAG_CANCEL_CURRENT));

    Intent intent3 = new Intent().setAction(INTENT_ACTION_LABEL + ".PLAY");
    intent3.putExtra("action", TO_ACTION_PLAY_ID);
    setOnClickPendingIntent(TO_ACTION_PLAY_ID, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent3, PendingIntent.FLAG_CANCEL_CURRENT));

    Intent intent4 = new Intent().setAction(INTENT_ACTION_LABEL + ".NEXT");
    intent4.putExtra("action", TO_ACTION_NEXT_ID);
    setOnClickPendingIntent(TO_ACTION_NEXT_ID, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent4, PendingIntent.FLAG_CANCEL_CURRENT));

    Intent intent5 = new Intent().setAction(INTENT_ACTION_LABEL + ".CLOSE");
    intent5.putExtra("action", TO_ACTION_CLOSE_ID);
    setOnClickPendingIntent(TO_ACTION_CLOSE_ID, PendingIntent.getBroadcast(mContext, REQUEST_CODE, intent5, PendingIntent.FLAG_CANCEL_CURRENT));


  }

  // 更新视图数据
  private void updateView() {
    if (audioNotifyView == null || mConfigData == null) {
      // 判断 root view 未初始化或 configData 未初始化
      return;
    }

    AudioConfigData config = getConfigData();
    RemoteViews rootView = audioNotifyView;

    // 设置音频封面图
    String cover = config.getCover();
    if (cover != null && !cover.equals("")) {
      try {
        FutureTarget<Bitmap> futureTarget =
          Glide.with(mContext)
            .asBitmap()
            .load(cover)
            .submit(120, 120);
        Bitmap bitmap = futureTarget.get();
        // Do something with the Bitmap and then when you're done with it:
        Glide.with(mContext).clear(futureTarget);
        rootView.setImageViewBitmap(TMP_INFO_COVER, bitmap);
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // 设置音频标题
    String title = config.getTitle();
    if (title != null && !title.equals("")) {
      rootView.setTextViewText(TMP_INFO_TITLE, title);
    }

    // 设置音频描述
    String description = config.getDescription();
    if (description != null && !description.equals("")) {
      rootView.setTextViewText(TMP_INFO_DESCRIPTION, description);
    }

    // 设置是否喜欢状态
    boolean isLike = config.isLike();
    if (isLike) {
      rootView.setImageViewResource(TO_ACTION_LIKE_ID, R.drawable.ic_action_like_on);
    } else {
      rootView.setImageViewResource(TO_ACTION_LIKE_ID, R.drawable.ic_action_like_off);
    }

    // 设置是否播放状态
    boolean isPlay = config.isPlay();
    if (isPlay) {
      rootView.setImageViewResource(TO_ACTION_PLAY_ID, R.drawable.ic_action_suspend);
    } else {
      rootView.setImageViewResource(TO_ACTION_PLAY_ID, R.drawable.ic_action_play);
    }

  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  private void setupNotificationChannel(Context context) {
    String channelName = context.getPackageName();
    String channelDescription = "Notifications from " + channelName;
    int importance = NotificationManager.IMPORTANCE_HIGH;

    NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
    channel.setDescription(channelDescription);
    NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    manager.createNotificationChannel(channel);
  }

  // 设置音频标题
  private void setAudioTitle(String title) {
    audioNotifyView.setTextViewText(TMP_INFO_TITLE, title);
  }

  // 设置音频封面
  private void setAudioCover(String url) {
    try {
      FutureTarget<Bitmap> futureTarget =
        Glide.with(mContext)
          .asBitmap()
          .load(url)
          .submit(120, 120);
      Bitmap bitmap = futureTarget.get();
      // Do something with the Bitmap and then when you're done with it:
      Glide.with(mContext).clear(futureTarget);
      audioNotifyView.setImageViewBitmap(TMP_INFO_COVER, bitmap);
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

//    Glide.with(mContext).asBitmap().load(url)
//      .placeholder(R.drawable.ic_action_play).into(notificationTarget);

  }

  // 设置音频描述
  private void setAudioDescription(String description) {
    audioNotifyView.setTextViewText(TMP_INFO_DESCRIPTION, description);
  }

  // 设置配置数据
  public void setConfigData(AudioConfigData configData) {
    this.mConfigData = configData;
  }

  // 获取当前配置数据
  public AudioConfigData getConfigData() {
    return this.mConfigData;
  }

  // 设置绑定按钮点击事件
  public void setOnClickPendingIntent(int viewID, PendingIntent pendingIntent) {
    //    audioNotifyView.setOnClickFillInIntent(R.id.image_01, new Intent().setAction("COPY_SHOW"));
    audioNotifyView.setOnClickPendingIntent(viewID, pendingIntent);
  }

  // 为了将事件传递回去需要存一个 mReactContext
  public void setReactContext(ReactContext reactContext) {
    mReactContext = reactContext;
  }

  // 音频通知配置数据
  public static class AudioConfigData {
    private String title, cover, description;
    private boolean isLike, isPlay;

    public AudioConfigData(String title, String cover, String description, boolean isLike, boolean isPlay) {
      this.title = title;
      this.cover = cover;
      this.description = description;
      this.isLike = isLike;
      this.isPlay = isPlay;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getCover() {
      return cover;
    }

    public void setCover(String cover) {
      this.cover = cover;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public boolean isLike() {
      return isLike;
    }

    public void setLike(boolean like) {
      isLike = like;
    }

    public boolean isPlay() {
      return isPlay;
    }

    public void setPlay(boolean play) {
      isPlay = play;
    }
  }

}
