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

package com.ypyglobal.xradio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import com.warkiz.widget.IndicatorSeekBar;
import com.ypyglobal.xradio.dataMng.XRadioNetUtils;
import com.ypyglobal.xradio.model.ConfigureModel;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.model.UIConfigModel;
import com.ypyglobal.xradio.setting.XRadioSettingManager;
import com.ypyglobal.xradio.stream.constant.IYPYStreamConstants;
import com.ypyglobal.xradio.stream.manager.YPYStreamManager;
import com.ypyglobal.xradio.stream.mediaplayer.YPYMediaPlayer;
import com.ypyglobal.xradio.ypylibs.imageloader.GlideImageLoader;
import com.ypyglobal.xradio.ypylibs.imageloader.target.GlideViewGroupTarget;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.ShareActionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import eu.gsottbauer.equalizerview.EqualizerView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class XSingleRadioMainActivity extends XRadioFragmentActivity implements IYPYStreamConstants, View.OnClickListener {

    public static final String TAG = XSingleRadioMainActivity.class.getSimpleName();

    @BindView(R.id.layout_drag_drop_bg)
    RelativeLayout mLayoutContainer;

    @BindView(R.id.equalizer)
    EqualizerView mEqualizer;

    @BindView(R.id.fb_play)
    FloatingActionButton mBtnPlay;

    @BindView(R.id.play_progressBar1)
    AVLoadingIndicatorView mLoadingProgress;

    @BindView(R.id.tv_percent)
    TextView mTvBuffering;

    @BindView(R.id.img_overlay_bg)
    ImageView mImageOverlay;

    @BindView(R.id.img_bg)
    ImageView mImageBg;

    @BindView(R.id.layout_facebook)
    MaterialRippleLayout mLayoutFb;

    @BindView(R.id.layout_instagram)
    MaterialRippleLayout mLayoutInsta;

    @BindView(R.id.layout_twitter)
    MaterialRippleLayout mLayoutTw;

    @BindView(R.id.layout_website)
    MaterialRippleLayout mLayoutWeb;

    @BindView(R.id.seekBar1)
    IndicatorSeekBar mSeekbar;

    @BindView(R.id.layout_content)
    LinearLayout mLayoutContent;

    @BindView(R.id.img_play_song)
    ImageView mImgCoverArt;

    @BindView(R.id.tv_drag_song)
    TextView mTvSong;

    @BindView(R.id.tv_sleep_timer)
    TextView mTvSleepMode;

    @BindView(R.id.tv_drag_singer)
    TextView mTvSinger;

    private CropCircleTransformation mCropCircleTransform;
    private int mTypeUI = UI_PLAYER_ROTATE_DISK;

    private ConfigureModel mConfigureModel;

    private RotateAnimation rotate;

    public String mUrlHost;
    public String mApiKey;
    private ApplicationBroadcast mApplicationBroadcast;
    private AudioManager mAudioManager;
    private BlurTransformation mBlurTransform;

    private UIConfigModel mUIConfigModel;
    private int mBgMode;


    @Override
    public int getResId() {
        return R.layout.activity_single_radio;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XRadioSettingManager.setOnline(this, true);
        setIsAllowPressMoreToExit(true);

    }

    @Override
    public void onDoWhenDone() {
        super.onDoWhenDone();
        setUpActionBar();
        showAppRate();
        setUpColorWidget();

        mCropCircleTransform = new CropCircleTransformation(this);
        mBlurTransform=new BlurTransformation(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        updateVolume();

        UIConfigModel model = mTotalMng.getUiConfigModel();
        mTypeUI = model != null ? model.getUiPlayer() : UI_PLAYER_ROTATE_DISK;

        updateInfoOfPlayingTrack();
        mEqualizer.stopBars();

        mSeekbar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                if (fromUserTouch) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        registerApplicationBroadcastReceiver();
        boolean b=AUTO_PLAY_IN_SINGLE_MODE;
        if(b){
            onActionPlay();
        }
        b=BLUR_BACKGROUND_IN_SINGLE_MODE;
        if(!b || mBgMode==UI_BG_JUST_ACTIONBAR){
            mImageOverlay.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDoWhenResume() {
        super.onDoWhenResume();
        updateVolume();
    }

    private void setUpActionBar() {
        mConfigureModel = mTotalMng.getConfigureModel();
        removeElevationActionBar();
        setUpCustomizeActionBar(Color.TRANSPARENT);
        setActionBarTitle(R.string.title_home_screen);

        mUrlHost = mConfigureModel != null ? mConfigureModel.getUrlEndPoint() : null;
        mApiKey = mConfigureModel != null ? mConfigureModel.getApiKey() : null;
    }

    private void setUpColorWidget() {
        try {
            mUIConfigModel = mTotalMng.getUiConfigModel();
            mBgMode = mUIConfigModel != null ? mUIConfigModel.getIsFullBg() : UI_BG_JUST_ACTIONBAR;
            if (mBgMode == UI_BG_FULL) {
                mLayoutContainer.setBackgroundColor(Color.TRANSPARENT);
                if (mLayoutAds != null) {
                    mLayoutAds.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyData() {
        boolean b=RESET_TIMER;
        if(b){
            XRadioSettingManager.setSleepMode(this,0);
        }
        XRadioSettingManager.setOnline(this, false);
        if (YPYStreamManager.getInstance().isPrepareDone()) {
            startMusicService(ACTION_STOP);
        }
        else {
            YPYStreamManager.getInstance().onDestroy();
        }
        super.onDestroyData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            boolean isOnlineApp = mConfigureModel != null ? mConfigureModel.isOnlineApp() : false;
            if (!isOnlineApp) {
                menu.findItem(R.id.action_term_of_use).setVisible(false);
                menu.findItem(R.id.action_privacy_policy).setVisible(false);
            }
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.action_facebook).setVisible(!TextUtils.isEmpty(URL_FACEBOOK));
            menu.findItem(R.id.action_insta).setVisible(!TextUtils.isEmpty(URL_INSTAGRAM));
            menu.findItem(R.id.action_website).setVisible(!TextUtils.isEmpty(URL_WEBSITE));
            menu.findItem(R.id.action_twitter).setVisible(!TextUtils.isEmpty(URL_TWITTER));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sleep_mode:
                showDialogSleepMode();
                break;
            case R.id.action_rate_me:
                String urlApp = String.format(URL_FORMAT_LINK_APP, getPackageName());
                ShareActionUtils.goToUrl(this, urlApp);
                XRadioSettingManager.setRateApp(this, true);
                break;
            case R.id.action_share:
                String urlApp1 = String.format(URL_FORMAT_LINK_APP, getPackageName());
                String msg = String.format(getString(R.string.info_share_app), getString(R.string.app_name), urlApp1);
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/*");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.title_menu_share)));
                break;
            case R.id.action_contact_us:
                ShareActionUtils.shareViaEmail(this, YOUR_CONTACT_EMAIL, "", "");
                break;
            case R.id.action_facebook:
                goToUrl(getString(R.string.title_facebook), URL_FACEBOOK);
                break;
            case R.id.action_twitter:
                goToUrl(getString(R.string.title_twitter), URL_TWITTER);
                break;
            case R.id.action_website:
                goToUrl(getString(R.string.title_website), URL_WEBSITE);
                break;
            case R.id.action_insta:
                goToUrl(getString(R.string.title_instagram), URL_INSTAGRAM);
                break;
            case R.id.action_term_of_use:
                String host = mConfigureModel != null ? mConfigureModel.getUrlEndPoint() : null;
                if (!TextUtils.isEmpty(host)) {
                    StringBuilder mStringBuilder = new StringBuilder(host);
                    mStringBuilder.append(XRadioNetUtils.METHOD_TERM_OF_USE);
                    goToUrl(getString(R.string.title_term_of_use), mStringBuilder.toString());
                }
                break;
            case R.id.action_privacy_policy:
                String host1 = mConfigureModel != null ? mConfigureModel.getUrlEndPoint() : null;
                if (!TextUtils.isEmpty(host1)) {
                    StringBuilder mStringBuilder = new StringBuilder(host1);
                    mStringBuilder.append(XRadioNetUtils.METHOD_PRIVACY_POLICY);
                    goToUrl(getString(R.string.title_privacy_policy), mStringBuilder.toString());
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            increaseVolume();
            return true;

        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            downVolume();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void startPlayingRadio() {
        if (!ApplicationUtils.isOnline(this)) {
            if (YPYStreamManager.getInstance().isPrepareDone()) {
                startMusicService(ACTION_STOP);
            }
            showToast(R.string.info_connect_to_play);
            return;
        }
        if (YPYStreamManager.getInstance().isPrepareDone()) {
            return;
        }
        ArrayList<RadioModel> mListPlaying = (ArrayList<RadioModel>) mTotalMng.getListData(TYPE_SINGLE_RADIO);
        if (mListPlaying != null && mListPlaying.size() > 0) {
            ArrayList<RadioModel> mListDatas = (ArrayList<RadioModel>) mListPlaying.clone();
            YPYStreamManager.getInstance().setListMusicRadio(mListDatas);
            startPlayRadio(mListPlaying.get(0));
        }
    }

    public void startPlayRadio(RadioModel trackModel) {
        try {
            boolean b = YPYStreamManager.getInstance().setCurrentData(trackModel);
            if (b) {
                startMusicService(ACTION_PLAY);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            startMusicService(ACTION_STOP);
        }

    }

    public void updateInfo(RadioModel mRadioModel) {
        try {
            if (mRadioModel != null) {
                YPYMediaPlayer.StreamInfo mStreamInfo = YPYStreamManager.getInstance().getStreamInfo();
                if (mStreamInfo != null) {
                    String title = mStreamInfo.title;
                    if (TextUtils.isEmpty(title)) {
                        title = mRadioModel.getName();
                    }
                    String singer = mStreamInfo.artist;
                    if (TextUtils.isEmpty(singer)) {
                        singer = mRadioModel.getTags();
                    }
                    this.mTvSong.setText(title);
                    this.mTvSinger.setText(singer);
                }
                else {
                    this.mTvSong.setText(mRadioModel.getName());
                    this.mTvSinger.setText(mRadioModel.getTags());
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateInfoOfPlayingTrack() {
        try {
            RadioModel mRadioModel = mTotalMng.getSingRadioModel();
            if (mRadioModel != null) {
                updateInfo(mRadioModel);
                String imgSong = mRadioModel.getArtWork(mUrlHost);
                processUpdateImage(imgSong);
                String urlFB = mRadioModel.getUrlFacebook();
                mLayoutFb.setVisibility(TextUtils.isEmpty(urlFB) ? View.GONE : View.VISIBLE);

                String urlTW = mRadioModel.getUrlTwitter();
                mLayoutTw.setVisibility(TextUtils.isEmpty(urlTW) ? View.GONE : View.VISIBLE);

                String urlWeb = mRadioModel.getUrlWebsite();
                mLayoutWeb.setVisibility(TextUtils.isEmpty(urlWeb) ? View.GONE : View.VISIBLE);

                String urlInsta = mRadioModel.getUrlInstagram();
                mLayoutInsta.setVisibility(TextUtils.isEmpty(urlInsta) ? View.GONE : View.VISIBLE);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void registerApplicationBroadcastReceiver() {
        if (mApplicationBroadcast != null) {
            return;
        }
        mApplicationBroadcast = new ApplicationBroadcast();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(getPackageName() + ACTION_BROADCAST_PLAYER);
        registerReceiver(mApplicationBroadcast, mIntentFilter);
    }

    private class ApplicationBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (intent != null) {
                    String action = intent.getAction();
                    if (!TextUtils.isEmpty(action)) {
                        String packageName = getPackageName();
                        if (action.equals(packageName + ACTION_BROADCAST_PLAYER)) {
                            String actionPlay = intent.getStringExtra(KEY_ACTION);

                            if (!TextUtils.isEmpty(actionPlay)) {
                                if (actionPlay.equalsIgnoreCase(ACTION_UPDATE_COVER_ART)) {
                                    String value = intent.getStringExtra(KEY_VALUE);
                                    processUpdateImage(value);
                                }
                                else {
                                    long value = intent.getLongExtra(KEY_VALUE, -1);
                                    processBroadcast(actionPlay, value);
                                }

                            }
                        }

                    }
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void processBroadcast(String actionPlay, long value) {
        if (actionPlay.equalsIgnoreCase(ACTION_LOADING)) {
            showLoading(true);
        }
        if (actionPlay.equalsIgnoreCase(ACTION_DIMINISH_LOADING)) {
            showLoading(false);
        }
        if (actionPlay.equalsIgnoreCase(ACTION_RESET_INFO)) {
            processUpdateImage(null);
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_BUFFERING)) {
            showLoading(false);
            updatePercent(value);
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_PAUSE)) {
            updateStatePlayer(false);
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_PLAY)) {
            updateStatePlayer(true);
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_STOP) || actionPlay.equalsIgnoreCase(ACTION_ERROR)) {
            updateStatePlayer(false);
            if (mTvSleepMode != null) {
                mTvSleepMode.setVisibility(View.INVISIBLE);
            }
            if (actionPlay.equalsIgnoreCase(ACTION_ERROR)) {
                int resId = ApplicationUtils.isOnline(this) ? R.string.info_play_error : R.string.info_connect_to_play;
                showToast(resId);
            }
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_UPDATE_INFO)) {
            RadioModel mRadioModel = YPYStreamManager.getInstance().getCurrentRadio();
            updateInfo(mRadioModel);
        }
        else if (actionPlay.equalsIgnoreCase(ACTION_UPDATE_SLEEP_MODE)) {
            updateSleepMode(value);
        }

    }

    private void updateSleepMode(long value) {
        try {
            if (value > 0) {
                mTvSleepMode.setVisibility(View.VISIBLE);
                mTvSleepMode.setText(getStringTimer(value));
            }
            else {
                mTvSleepMode.setVisibility(View.INVISIBLE);
                mTvSleepMode.setText("00:00");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void processUpdateImage(String imgSong) {
        boolean b=BLUR_BACKGROUND_IN_SINGLE_MODE;
        try {
            if (TextUtils.isEmpty(imgSong)) {
                RadioModel ringtoneModel = YPYStreamManager.getInstance().getCurrentRadio();
                imgSong = ringtoneModel.getArtWork(mUrlHost);
            }
            if (!TextUtils.isEmpty(imgSong)) {
                if (mTypeUI == UI_PLAYER_CIRCLE_DISK || mTypeUI == UI_PLAYER_ROTATE_DISK) {
                    GlideImageLoader.displayImage(this, mImgCoverArt, imgSong, mCropCircleTransform, R.drawable.ic_big_circle_img_default);
                }
                else {
                    GlideImageLoader.displayImage(this, mImgCoverArt, imgSong, R.drawable.ic_big_rect_img_default);
                }
                if(b && mBgMode==UI_BG_FULL){
                    mImageOverlay.setVisibility(View.VISIBLE);
                    GlideImageLoader.displayImage(this, mImageBg, imgSong,mBlurTransform, R.drawable.background_transparent);
                }
            }
            else {
                resetDefaultImg();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetDefaultImg() {
        if (mImgCoverArt != null) {
            if (mTypeUI == UI_PLAYER_CIRCLE_DISK || mTypeUI == UI_PLAYER_ROTATE_DISK) {
                mImgCoverArt.setImageResource(R.drawable.ic_big_circle_img_default);
            }
            else {
                mImgCoverArt.setImageResource(R.drawable.ic_big_rect_img_default);
            }
            mImageBg.setImageResource(R.drawable.background_transparent);
            mImageOverlay.setVisibility(View.GONE);
        }
    }



    public void showLoading(boolean b) {
        mLayoutContent.setVisibility(View.INVISIBLE);
        mTvBuffering.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
        if (b) {
            mLoadingProgress.setVisibility(View.VISIBLE);
            mLoadingProgress.show();
            if (mEqualizer.isAnimating()) {
                mEqualizer.stopBars();
            }
        }
        else {
            if (mLoadingProgress.getVisibility() == View.VISIBLE) {
                mLoadingProgress.hide();
                mLoadingProgress.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void updatePercent(long percent) {
        mTvBuffering.setVisibility(View.VISIBLE);
        mLayoutContent.setVisibility(View.INVISIBLE);
        pauseRotateAnim();
        if (percent > 0) {
            String msg = String.format(getString(R.string.format_buffering), String.valueOf(percent) + "%");
            mTvBuffering.setText(msg);
        }
        if (mEqualizer.isAnimating()) {
            mEqualizer.stopBars();
        }

    }


    public void updateStatePlayer(boolean isPlaying) {
        mLayoutContent.setVisibility(View.VISIBLE);
        mTvBuffering.setVisibility(View.INVISIBLE);
        int playId = isPlaying ? R.drawable.ic_pause_white_36dp : R.drawable.ic_play_arrow_white_36dp;
        mBtnPlay.setImageResource(playId);
        if (isPlaying) {
            mEqualizer.animateBars();
        }
        else {
            mEqualizer.stopBars();
        }
        if (mTypeUI == UI_PLAYER_ROTATE_DISK) {
            if (isPlaying) {
                startRotateAnim();
            }
            else {
                pauseRotateAnim();
            }
        }

    }

    @Override
    protected void onDestroy() {
        pauseRotateAnim();
        if (mApplicationBroadcast != null) {
            unregisterReceiver(mApplicationBroadcast);
            mApplicationBroadcast = null;
        }
        super.onDestroy();
    }

    private void onActionPlay(){
        if (YPYStreamManager.getInstance().isPrepareDone()) {
            startMusicService(ACTION_TOGGLE_PLAYBACK);
        }
        else {
            startPlayingRadio();
        }
    }

    @OnClick({R.id.fb_play, R.id.btn_facebook, R.id.btn_instagram, R.id.btn_website,
            R.id.btn_twitter, R.id.btn_share})
    @Override
    public void onClick(View view) {
        RadioModel mRadioModel = YPYStreamManager.getInstance().getCurrentRadio();
        String nameRadio = mRadioModel != null ? mRadioModel.getName() : null;
        switch (view.getId()) {
            case R.id.fb_play:
                onActionPlay();
                break;
            case R.id.btn_facebook:
                String urlFB = mRadioModel != null ? mRadioModel.getUrlFacebook() : null;
                if (!TextUtils.isEmpty(urlFB)) {
                    goToUrl(nameRadio, urlFB);
                }
                break;
            case R.id.btn_instagram:
                String urlInsta = mRadioModel != null ? mRadioModel.getUrlInstagram() : null;
                if (!TextUtils.isEmpty(urlInsta)) {
                    goToUrl(nameRadio, urlInsta);
                }
                break;
            case R.id.btn_twitter:
                String urlTW = mRadioModel != null ? mRadioModel.getUrlTwitter() : null;
                if (!TextUtils.isEmpty(urlTW)) {
                    goToUrl(nameRadio, urlTW);
                }
                break;
            case R.id.btn_website:
                String urlWeb = mRadioModel != null ? mRadioModel.getUrlWebsite() : null;
                if (!TextUtils.isEmpty(urlWeb)) {
                    goToUrl(nameRadio, urlWeb);
                }
                break;
            case R.id.btn_share:
                shareRadioModel(mRadioModel);
                break;
        }
    }

    public void updateVolume() {
        try {
            int values = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mSeekbar.setMax(maxVolume);
            mSeekbar.setProgress(values);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void increaseVolume() {
        try {
            int values = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            values++;
            if (values >= maxVolume) {
                values = maxVolume;
            }
            mSeekbar.setProgress(values);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, values, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void downVolume() {
        try {
            int values = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            values--;
            if (values < 0) {
                values = 0;
            }
            mSeekbar.setProgress(values);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, values, 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pauseRotateAnim() {
        try {
            if (mImgCoverArt != null) {
                if (rotate != null) {
                    mImgCoverArt.clearAnimation();
                    rotate.cancel();
                    rotate = null;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRotateAnim() {
        try {
            if (mTypeUI == UI_PLAYER_ROTATE_DISK && mImgCoverArt != null) {
                if (rotate != null) {
                    mImgCoverArt.clearAnimation();
                    rotate.cancel();
                    rotate = null;
                }
                final float toDegrees = DEGREE * 360;
                rotate = new RotateAnimation(0, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f) {
                    private boolean isFirstTime;

                    @Override
                    public boolean getTransformation(long currentTime, Transformation outTransformation) {
                        if (!isFirstTime) {
                            isFirstTime = true;
                            setStartTime(currentTime);
                        }
                        return super.getTransformation(currentTime, outTransformation);
                    }
                };
                rotate.setDuration(DEGREE * DELTA_TIME);
                rotate.setRepeatCount(1000);
                mImgCoverArt.startAnimation(rotate);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void setUpBackground(RelativeLayout mLayoutBg, String bg){
        try{
            if(mLayoutBg!=null){
                GlideViewGroupTarget mTarget = new GlideViewGroupTarget(this, mLayoutBg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                };
                if(!TextUtils.isEmpty(bg)){
                    Glide.with(this).load(Uri.parse(bg)).asBitmap()
                            .transform(mBlurTransform)
                            .placeholder(R.drawable.default_bg_app).into(mTarget);
                    mImageOverlay.setVisibility(View.VISIBLE);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
