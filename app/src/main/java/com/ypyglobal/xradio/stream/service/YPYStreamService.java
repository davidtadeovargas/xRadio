/*
 * Copyright (c) 2018. YPY Global - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *         http://ypyglobal.com/sourcecode/policy
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ypyglobal.xradio.stream.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.XMultiRadioMainActivity;
import com.ypyglobal.xradio.XSingleRadioMainActivity;
import com.ypyglobal.xradio.dataMng.TotalDataManager;
import com.ypyglobal.xradio.dataMng.XRadioNetUtils;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.setting.XRadioSettingManager;
import com.ypyglobal.xradio.stream.audiofocus.AudioFocusHelper;
import com.ypyglobal.xradio.stream.audiofocus.IStreamFocusableListener;
import com.ypyglobal.xradio.stream.constant.IYPYStreamConstants;
import com.ypyglobal.xradio.stream.manager.YPYStreamManager;
import com.ypyglobal.xradio.stream.mediaplayer.YPYMediaPlayer;
import com.ypyglobal.xradio.ypylibs.executor.YPYExecutorSupplier;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.IOUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;


public class YPYStreamService extends Service implements IYPYStreamConstants, IStreamFocusableListener {

    public static final String TAG = "DCM";
    public static final String ANDROID8_CHANNEL_ONE_NAME = "XRadioChannel";
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PLAYING = 2;
    public static final int STATE_PAUSE = 3;
    public static final int STATE_STOP = 4;
    public static final int STATE_ERROR = 5;

    private int mCurrentState = STATE_STOP;

    private static final float MAX_VOLUME = 1f;
    public static final float DUCK_VOLUME = 0.1f;

    private AudioFocusHelper mAudioFocusHelper;
    private Notification mNotification;
    private RadioModel mCurrentTrack;
    private boolean isStartLoading;

    private Handler mHandlerSleep = new Handler();
    private int mMinuteCount;


    private enum AudioFocus {
        NO_FOCUS_NO_DUCK, // we don't have audio focus, and can't duck
        NO_FOCUS_CAN_DUCK, // we don't have focus, but can play at a low volume
        FOCUSED // we have full audio focus
    }

    private AudioFocus mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
    private YPYMediaPlayer mMediaPlayer;
    private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mAudioFocusHelper = new AudioFocusHelper(this.getApplicationContext(), this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                String packageName = getPackageName();
                if (action.equalsIgnoreCase(packageName + ACTION_TOGGLE_PLAYBACK)) {
                    onActionTogglePlay();
                }
                else if (action.equalsIgnoreCase(packageName + ACTION_PLAY)) {
                    startSleepMode();
                    onActionPlay();
                }
                else if (action.equalsIgnoreCase(packageName + ACTION_NEXT)) {
                    onActionNext();
                }
                else if (action.equalsIgnoreCase(packageName + ACTION_PREVIOUS)) {
                    onActionPrevious();
                }
                else if (action.equalsIgnoreCase(packageName + ACTION_STOP)) {
                    onActionStop();
                }
                else if (action.equals(packageName + ACTION_UPDATE_SLEEP_MODE)) {
                    startSleepMode();
                }
            }
        }
        return START_NOT_STICKY;
    }

    private void startSleepMode() {
        try{
            int minute = XRadioSettingManager.getSleepMode(this);
            mHandlerSleep.removeCallbacksAndMessages(null);
            if (minute > 0) {
                this.mMinuteCount = minute*ONE_MINUTE;
                startCountSleep();
            }
            else{
                sendMusicBroadcast(ACTION_UPDATE_SLEEP_MODE,0);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    private void startCountSleep(){
        try {
            if(mMinuteCount>0){
                mHandlerSleep.postDelayed(()->{
                    mMinuteCount=mMinuteCount-1000;
                    sendMusicBroadcast(ACTION_UPDATE_SLEEP_MODE,mMinuteCount);
                    if(mMinuteCount<=0){
                        onActionStop();
                    }
                    else{
                        startCountSleep();
                    }
                }, 1000);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void onActionStop() {
        isStartLoading = false;
        boolean isError=mCurrentState==STATE_ERROR;
        try {
            releaseData(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(isError){
            sendMusicBroadcast(ACTION_ERROR);
        }
        else{
            sendMusicBroadcast(ACTION_STOP);
        }
    }

    private void onActionPrevious() {
        try {
            mCurrentTrack = YPYStreamManager.getInstance().prevPlay();
            if (mCurrentTrack != null) {
                startPlayNewSong();
            }
            else {
                mCurrentState = STATE_ERROR;
                onActionStop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onActionNext() {
        try {
            mCurrentTrack = YPYStreamManager.getInstance().nextPlay();
            if (mCurrentTrack != null) {
                startPlayNewSong();
            }
            else {
                mCurrentState = STATE_ERROR;
                onActionStop();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onActionPlay() {
        processPlayRequest(true);
    }

    private void onActionTogglePlay() {
        try {
            if (mCurrentState == STATE_PAUSE || mCurrentState == STATE_STOP) {
                processPlayRequest(false);
            }
            else {
                processPauseRequest();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processPauseRequest() {
        if (mCurrentTrack == null || mMediaPlayer == null) {
            mCurrentState = STATE_ERROR;
            onActionStop();
            return;
        }
        try {
            if (mCurrentState == STATE_PLAYING) {
                mCurrentState = STATE_PAUSE;
                mMediaPlayer.pause();
                setUpNotification();
                sendMusicBroadcast(ACTION_PAUSE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            onActionNext();
        }
    }

    private void processPlayRequest(boolean isForces) {
        mCurrentTrack = YPYStreamManager.getInstance().getCurrentRadio();
        if (mCurrentTrack == null) {
            mCurrentState = STATE_ERROR;
            onActionStop();
            return;
        }
        if (mCurrentState == STATE_STOP || mCurrentState == STATE_PLAYING || isForces) {
            startPlayNewSong();
            sendMusicBroadcast(ACTION_NEXT);
        }
        else if (mCurrentState == STATE_PAUSE) {
            mCurrentState = STATE_PLAYING;
            configAndStartMediaPlayer();
            setUpNotification();
        }

    }

    private synchronized void startPlayNewSong() {
        tryToGetAudioFocus();
        if (!isStartLoading) {
            mCurrentState = STATE_STOP;
            isStartLoading = true;
            if (mCurrentTrack == null) {
                mCurrentState = STATE_ERROR;
                onActionStop();
                return;
            }
            if (mMediaPlayer != null) {
                releaseMedia();
            }
            startStreamMusic();
        }

    }

    private synchronized void startStreamMusic() {
        if (mCurrentTrack != null) {
            releaseMedia();
            sendMusicBroadcast(ACTION_LOADING);
            setUpNotification();
            YPYStreamManager.getInstance().setLoading(true);
            YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
                final String uriStream = mCurrentTrack.getLinkRadio();
                YPYLog.e(TAG, "=========>uriStream=" + uriStream);
                setUpMediaForStream(uriStream);
                isStartLoading = false;


            });
        }
    }

    private boolean setUpMediaForStream(final String path) {
        createMediaPlayer();
        try {
            if (mMediaPlayer != null) {
                mCurrentState = STATE_PREPARING;
                mMediaPlayer.setDataSource(path);
                return true;

            }
        }
        catch (Exception ex) {
            Log.d(TAG, "IOException playing next song: " + ex.getMessage());
            ex.printStackTrace();
            onActionStop();
        }
        return false;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseData(true);
        try {
            giveUpAudioFocus();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onGainedAudioFocus() {
        try {
            mAudioFocus = AudioFocus.FOCUSED;
            if (mCurrentState == STATE_PLAYING) {
                configAndStartMediaPlayer();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLostAudioFocus(boolean canDuck) {
        try {
            mAudioFocus = canDuck ? AudioFocus.NO_FOCUS_CAN_DUCK : AudioFocus.NO_FOCUS_NO_DUCK;
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                configAndStartMediaPlayer();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tryToGetAudioFocus() {
        try {
            if (mAudioFocus != null && mAudioFocus != AudioFocus.FOCUSED
                    && mAudioFocusHelper != null
                    && mAudioFocusHelper.requestFocus())
                mAudioFocus = AudioFocus.FOCUSED;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void giveUpAudioFocus() {
        try {
            if (mAudioFocus != null && mAudioFocus == AudioFocus.FOCUSED &&
                    mAudioFocusHelper != null && mAudioFocusHelper.abandonFocus()) {
                mAudioFocus = AudioFocus.NO_FOCUS_NO_DUCK;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void configAndStartMediaPlayer() {
        try {
            if (mMediaPlayer != null && (mCurrentState == STATE_PLAYING || mCurrentState == STATE_PAUSE)) {
                if (mAudioFocus == AudioFocus.NO_FOCUS_NO_DUCK) {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                        sendMusicBroadcast(ACTION_PAUSE);
                    }
                    return;
                }
                else if (mAudioFocus == AudioFocus.NO_FOCUS_CAN_DUCK) {
                    mMediaPlayer.setVolume(DUCK_VOLUME);
                }
                else {
                    mMediaPlayer.setVolume(MAX_VOLUME);
                }
                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    sendMusicBroadcast(ACTION_PLAY);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMusicBroadcast(String action) {
        sendMusicBroadcast(action, -1);
    }

    private void sendMusicBroadcast(String action, long value) {
        try {
            Intent mIntent = new Intent(getPackageName() + ACTION_BROADCAST_PLAYER);
            mIntent.putExtra(KEY_ACTION, action);
            if (value != -1) {
                mIntent.putExtra(KEY_VALUE, value);
            }
            sendBroadcast(mIntent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMusicBroadcast(String action, String value) {
        try {
            Intent mIntent = new Intent(getPackageName() + ACTION_BROADCAST_PLAYER);
            mIntent.putExtra(KEY_ACTION, action);
            mIntent.putExtra(KEY_VALUE, value);
            sendBroadcast(mIntent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpNotification() {
        if (mCurrentTrack == null) {
            return;
        }
        try {
            boolean isSingle=TotalDataManager.getInstance().isSingleRadio();
            String packageName = getPackageName();
            PendingIntent pi;
            if(isSingle){
                Intent mIntent = new Intent(this.getApplicationContext(), XSingleRadioMainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pi = PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_ID, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            }
            else{
                Intent mIntent = new Intent(this.getApplicationContext(), XMultiRadioMainActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                pi = PendingIntent.getActivity(getApplicationContext(), NOTIFICATION_ID, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            }

            String CHANNEL_ONE_ID = getPackageName() + ".N2";
            String CHANNEL_ONE_NAME = getPackageName()+ANDROID8_CHANNEL_ONE_NAME;
            if (IOUtils.hasAndroid80()) {
                try {
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                            CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_LOW);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setShowBadge(true);
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            mBuilder.setSmallIcon(R.drawable.ic_notification_24dp);
            mBuilder.setColor(getResources().getColor(R.color.color_noti_background));
            mBuilder.setShowWhen(false);
            if (IOUtils.hasAndroid80()) {
                mBuilder.setChannelId(CHANNEL_ONE_ID);
            }

            Intent nextIntent = new Intent(this, YPYIntentReceiver.class);
            nextIntent.setAction(packageName + ACTION_NEXT);
            PendingIntent pendingNextIntent = PendingIntent.getBroadcast(this, 100, nextIntent, 0);

            Intent stopIntent = new Intent(this, YPYIntentReceiver.class);
            stopIntent.setAction(packageName + ACTION_STOP);
            PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 100, stopIntent, 0);

            Intent toggleIntent = new Intent(this, YPYIntentReceiver.class);
            toggleIntent.setAction(packageName + ACTION_TOGGLE_PLAYBACK);
            PendingIntent pendingToggleIntent = PendingIntent.getBroadcast(this, 100, toggleIntent, 0);


            int resId=isSingle?R.layout.item_single_notification_music:R.layout.item_multi_notification_music;
            RemoteViews notificationView = new RemoteViews(getPackageName(), resId);
            notificationView.setOnClickPendingIntent(R.id.btn_stop, stopPendingIntent);
            if(!isSingle){
                notificationView.setOnClickPendingIntent(R.id.btn_next, pendingNextIntent);
            }
            notificationView.setOnClickPendingIntent(R.id.btn_play, pendingToggleIntent);
            notificationView.setTextViewText(R.id.tv_radio_name, mCurrentTrack.getName());

            String info=mCurrentTrack.getTags();
            if(!TextUtils.isEmpty(mCurrentTrack.getSong())){
                info=mCurrentTrack.getMetaData();
            }
            if(TextUtils.isEmpty(info)){
                info=getString(R.string.title_unknown);
            }
            notificationView.setTextViewText(R.id.tv_info, info);

            boolean isPlay = YPYStreamManager.getInstance().isPlaying();
            if(isPlay){
                notificationView.setImageViewResource(R.id.btn_play, R.drawable.ic_pause_white_36dp);
            }
            else{
                notificationView.setImageViewResource(R.id.btn_play, R.drawable.ic_play_arrow_white_36dp);
            }
            mBuilder.setCustomContentView(notificationView);
            mBuilder.setPriority(android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT);

            mNotification = mBuilder.build();
            mNotification.contentIntent = pi;
            mNotification.flags |= Notification.FLAG_NO_CLEAR;
            startForeground(NOTIFICATION_ID, mNotification);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void createMediaPlayer() {
        try {
            mMediaPlayer = new YPYMediaPlayer(this);
            mMediaPlayer.setOnStreamListener(new YPYMediaPlayer.OnStreamListener() {
                @Override
                public void onPrepare() {
                    sendMusicBroadcast(ACTION_DIMINISH_LOADING);
                    mCurrentState = STATE_PLAYING;
                    YPYStreamManager.getInstance().setLoading(false);
                    configAndStartMediaPlayer();
                    setUpNotification();
                    YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
                        try{
                            if(mMediaPlayer!=null){
                                YPYMediaPlayer.StreamInfo mStreamInfo = mMediaPlayer.getStreamInfo();
                                if (mStreamInfo != null) {
                                    onUpdateMetaData(mStreamInfo);
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    });
                }

                @Override
                public void onError() {
                    try {
                        YPYStreamManager.getInstance().setLoading(false);
                        sendMusicBroadcast(ACTION_DIMINISH_LOADING);
                        mCurrentState = STATE_ERROR;
                        onActionStop();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onComplete() {
                    mCurrentState = STATE_STOP;
                    onActionNext();
                    sendMusicBroadcast(ACTION_NEXT);
                }

                @Override
                public void onBuffering(long percent) {
                    mCurrentState = STATE_PREPARING;
                    sendMusicBroadcast(ACTION_BUFFERING, percent);
                }

                @Override
                public void onUpdateMetaData(YPYMediaPlayer.StreamInfo info) {
                    YPYStreamManager.getInstance().setStreamInfo(info);
                    if(info!=null){
                        String title = info.title;
                        String artist = info.artist;
                        if (mCurrentTrack != null) {
                            mCurrentTrack.setSong(title);
                            mCurrentTrack.setArtist(artist);
                        }
                        sendMusicBroadcast(ACTION_UPDATE_INFO);
                        setUpNotification();
                        startGetImageOfSong(title,artist,info);
                    }
                    else{
                        if (mCurrentTrack != null) {
                            mCurrentTrack.setSong(null);
                            mCurrentTrack.setArtist(null);
                        }
                        setUpNotification();
                        sendMusicBroadcast(ACTION_RESET_INFO);
                    }

                }
            });
            YPYStreamManager.getInstance().setMediaPlayer(mMediaPlayer);
        }
        catch (Exception e) {
            e.printStackTrace();
            mCurrentState = STATE_ERROR;
            onActionStop();
        }

    }


    private void releaseData(final boolean isDestroyAll) {
        mHandlerSleep.removeCallbacksAndMessages(null);
        releaseMedia();
        try {
            if (isDestroyAll) {
                stopForeground(true);
                YPYStreamManager.getInstance().onDestroy();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startGetImageOfSong(String title, String artist, YPYMediaPlayer.StreamInfo mStreamInfo) {
        if(ApplicationUtils.isOnline(this) && mStreamInfo!=null && (!TextUtils.isEmpty(title)|| !TextUtils.isEmpty(artist))){
            YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
                String lastFmKey = TotalDataManager.getInstance().getLastFmKey();
                String url = XRadioNetUtils.getImageOfSong(title, artist,lastFmKey);
                if(!TextUtils.isEmpty(url)){
                    mStreamInfo.imgUrl=url;
                    sendMusicBroadcast(ACTION_UPDATE_COVER_ART,url);
                }
            });
        }

    }

    private void releaseMedia() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.release();
                YPYStreamManager.getInstance().onResetMedia();
                mMediaPlayer = null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        mCurrentState = STATE_STOP;

    }


}
