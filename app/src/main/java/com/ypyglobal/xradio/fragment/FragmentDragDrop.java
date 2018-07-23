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

package com.ypyglobal.xradio.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.wang.avi.AVLoadingIndicatorView;
import com.warkiz.widget.IndicatorSeekBar;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.XMultiRadioMainActivity;
import com.ypyglobal.xradio.constants.IXRadioConstants;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.model.UIConfigModel;
import com.ypyglobal.xradio.stream.constant.IYPYStreamConstants;
import com.ypyglobal.xradio.stream.manager.YPYStreamManager;
import com.ypyglobal.xradio.stream.mediaplayer.YPYMediaPlayer;
import com.ypyglobal.xradio.ypylibs.fragment.YPYFragment;
import com.ypyglobal.xradio.ypylibs.imageloader.GlideImageLoader;

import butterknife.BindView;
import butterknife.OnClick;
import eu.gsottbauer.equalizerview.EqualizerView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class FragmentDragDrop extends YPYFragment implements IXRadioConstants,IYPYStreamConstants, View.OnClickListener {

    private XMultiRadioMainActivity mContext;

    @BindView(R.id.equalizer)
    EqualizerView mEqualizer;

    @BindView(R.id.layout_drag_drop_bg)
    RelativeLayout mLayoutDragDropBg;

    @BindView(R.id.play_progressBar1)
    AVLoadingIndicatorView mLoadingProgress;

    @BindView(R.id.fb_play)
    FloatingActionButton mBtnPlay;

    @BindView(R.id.tv_percent)
    TextView mTvBuffering;

    @BindView(R.id.btn_favorite)
    LikeButton mBtnLike;

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

    @BindView(R.id.img_overlay)
    ImageView mImgOverlay;

    @BindView(R.id.tv_drag_song)
    TextView mTvSong;

    @BindView(R.id.tv_drag_singer)
    TextView mTvSinger;

    @BindView(R.id.tv_title_drag_drop)
    TextView mTvRadioName;

    @BindView(R.id.tv_bitrate)
    TextView mTvBitRate;

    @BindView(R.id.img_play_song)
    ImageView mImgCoverArt;

    @BindView(R.id.img_bg_drag_drop)
    ImageView mImgBg;

    @BindView(R.id.tv_sleep_timer)
    TextView mTvSleepMode;

    private AudioManager mAudioManager;
    private BlurTransformation mBlurTransform;
    private CropCircleTransformation mCropCircleTransform;
    private int mTypeUI=UI_PLAYER_SQUARE_DISK;
    private RotateAnimation rotate;


    @Override
    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drag_drop_detail, container, false);
    }

    @Override
    public void findView() {
        this.mContext = (XMultiRadioMainActivity) getActivity();

        mBtnPlay.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        mBtnPlay.setRippleColor(mContext.getResources().getColor(R.color.ripple_button_color));
        mBtnPlay.setSize(FloatingActionButton.SIZE_NORMAL);

        mBlurTransform=new BlurTransformation(mContext);
        mCropCircleTransform= new CropCircleTransformation(mContext);

        mEqualizer.stopBars();

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mSeekbar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                if(fromUserTouch){
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
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
        updateBackground();
        updateVolume();

        updateInfo(true);
        mBtnLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                RadioModel model=YPYStreamManager.getInstance().getCurrentRadio();
                mContext.updateFavorite(model,TYPE_TAB_FAVORITE,true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                RadioModel model=YPYStreamManager.getInstance().getCurrentRadio();
                mContext.updateFavorite(model,TYPE_TAB_FAVORITE,false);
            }
        });

        UIConfigModel model= mContext.mTotalMng.getUiConfigModel();
        mTypeUI= model!=null?model.getUiPlayer():UI_PLAYER_SQUARE_DISK;

        boolean isLoading = YPYStreamManager.getInstance().isLoading();
        if(isLoading){
            showLoading(true);
        }
        else{
            showLayoutControl();
            updateStatusPlayer(YPYStreamManager.getInstance().isPlaying());
            YPYMediaPlayer.StreamInfo mStrInfo = YPYStreamManager.getInstance().getStreamInfo();
            updateImage(mStrInfo!=null?mStrInfo.imgUrl:null);
        }

    }

    public void showLoading(boolean b) {
        try{
            if(mContext!=null){
                mLayoutContent.setVisibility(View.INVISIBLE);
                mTvBuffering.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
                if(b){
                    mLoadingProgress.setVisibility(View.VISIBLE);
                    mLoadingProgress.show();
                    if(mEqualizer.isAnimating()){
                        mEqualizer.stopBars();
                    }
                }
                else{
                    if(mLoadingProgress.getVisibility()==View.VISIBLE){
                        mLoadingProgress.hide();
                        mLoadingProgress.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    public void updatePercent(long percent){
        try{
            if(mContext!=null){
                mTvBuffering.setVisibility(View.VISIBLE);
                mLayoutContent.setVisibility(View.INVISIBLE);
                pauseRotateAnim();
                if(percent>0){
                    String msg = String.format(mContext.getString(R.string.format_buffering), String.valueOf(percent) +"%");
                    mTvBuffering.setText(msg);
                }
                if(mEqualizer.isAnimating()){
                    mEqualizer.stopBars();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showLayoutControl(){
        if(mContext!=null){
            mLayoutContent.setVisibility(View.VISIBLE);
            mTvBuffering.setVisibility(View.INVISIBLE);
        }
    }

    public void updateStatusPlayer(boolean isPlaying){
        if(mContext!=null){
            showLayoutControl();
            mBtnPlay.setImageResource(isPlaying?R.drawable.ic_pause_white_36dp:R.drawable.ic_play_arrow_white_36dp);
            if(isPlaying){
                mEqualizer.animateBars();
                startRotateAnim();
            }
            else{
                mEqualizer.stopBars();
                pauseRotateAnim();
            }
        }
    }

    public void updateInfo(boolean isNeedUpdateSocial){
        try{
            if(mContext!=null){
                RadioModel mRadioModel=YPYStreamManager.getInstance().getCurrentRadio();
                if(mRadioModel!=null){
                    String nameRadio=mRadioModel.getName();
                    this.mTvRadioName.setText(nameRadio);
                    this.mTvBitRate.setText(String.format(mContext.getString(R.string.format_bitrate),mRadioModel.getBitRate()));
                    YPYMediaPlayer.StreamInfo mStreamInfo = YPYStreamManager.getInstance().getStreamInfo();
                    if(mStreamInfo!=null){
                        String title=mStreamInfo.title;
                        if(TextUtils.isEmpty(title)){
                            title=mRadioModel.getName();
                        }
                        String singer=mStreamInfo.artist;
                        if(TextUtils.isEmpty(singer)){
                            singer=mRadioModel.getTags();
                        }
                        this.mTvSong.setText(title);
                        this.mTvSinger.setText(singer);
                    }
                    else{
                        this.mTvSong.setText(mRadioModel.getName());
                        this.mTvSinger.setText(mRadioModel.getTags());
                    }
                    if(isNeedUpdateSocial){
                        String urlFB=mRadioModel.getUrlFacebook();
                        mLayoutFb.setVisibility(TextUtils.isEmpty(urlFB)?View.GONE:View.VISIBLE);

                        String urlTW=mRadioModel.getUrlTwitter();
                        mLayoutTw.setVisibility(TextUtils.isEmpty(urlTW)?View.GONE:View.VISIBLE);

                        String urlWeb=mRadioModel.getUrlWebsite();
                        mLayoutWeb.setVisibility(TextUtils.isEmpty(urlWeb)?View.GONE:View.VISIBLE);

                        String urlInsta=mRadioModel.getUrlInstagram();
                        mLayoutInsta.setVisibility(TextUtils.isEmpty(urlInsta)?View.GONE:View.VISIBLE);

                        mBtnLike.setLiked(mRadioModel.isFavorite());
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }



    public void updateVolume(){
        try{
            AudioManager mgr = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            int values = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mSeekbar.setMax(maxVolume);
            mSeekbar.setProgress(values);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void increaseVolume(){
        try{
            int values = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            values++;
            if(values>=maxVolume){
                values=maxVolume;
            }
            mSeekbar.setProgress(values);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,values,0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void downVolume(){
        try{
            int values = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            values--;
            if(values<0){
                values=0;
            }
            mSeekbar.setProgress(values);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,values,0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        pauseRotateAnim();
        if (mEqualizer != null) {
            mEqualizer.stopBars();
            mEqualizer.onDestroy();
        }
        super.onDestroy();
    }

    @OnClick({R.id.btn_close, R.id.fb_play, R.id.btn_next, R.id.btn_prev, R.id.btn_facebook,R.id.btn_instagram,R.id.btn_website,
            R.id.btn_twitter, R.id.btn_share})

    @Override
    public void onClick(View view) {
        RadioModel mRadioModel = YPYStreamManager.getInstance().getCurrentRadio();
        String nameRadio=mRadioModel!=null?mRadioModel.getName():null;
        switch (view.getId()) {
            case R.id.btn_close:
                mContext.collapseListenMusic();
                break;
            case R.id.btn_next:
                mContext.startMusicService(ACTION_NEXT);
                break;
            case R.id.btn_prev:
                mContext.startMusicService(ACTION_PREVIOUS);
                break;
            case R.id.fb_play:
                mContext.startMusicService(ACTION_TOGGLE_PLAYBACK);
                break;
            case R.id.btn_facebook:
                String urlFB=mRadioModel!=null?mRadioModel.getUrlFacebook():null;
                if(!TextUtils.isEmpty(urlFB)){
                    mContext.goToUrl(nameRadio,urlFB);
                }
                break;
            case R.id.btn_instagram:
                String urlInsta=mRadioModel!=null?mRadioModel.getUrlInstagram():null;
                if(!TextUtils.isEmpty(urlInsta)){
                    mContext.goToUrl(nameRadio,urlInsta);
                }
                break;
            case R.id.btn_twitter:
                String urlTW=mRadioModel!=null?mRadioModel.getUrlTwitter():null;
                if(!TextUtils.isEmpty(urlTW)){
                    mContext.goToUrl(nameRadio,urlTW);
                }
                break;
            case R.id.btn_website:
                String urlWeb=mRadioModel!=null?mRadioModel.getUrlWebsite():null;
                if(!TextUtils.isEmpty(urlWeb)){
                    mContext.goToUrl(nameRadio,urlWeb);
                }
                break;
            case R.id.btn_share:
                mContext.shareRadioModel(mRadioModel);
                break;
        }
    }

    public void updateImage(String url){
        if(mImgCoverArt!=null){
            if(!TextUtils.isEmpty(url)){
                mImgOverlay.setVisibility(View.VISIBLE);
                if(mTypeUI==UI_PLAYER_CIRCLE_DISK || mTypeUI==UI_PLAYER_ROTATE_DISK){
                    GlideImageLoader.displayImage(mContext,mImgCoverArt,url,mCropCircleTransform,R.drawable.ic_big_circle_img_default);
                }
                else{
                    GlideImageLoader.displayImage(mContext,mImgCoverArt,url,R.drawable.ic_big_rect_img_default);
                }
                GlideImageLoader.displayImage(mContext,mImgBg,url,mBlurTransform,R.drawable.background_transparent);
            }
            else{
                resetDefaultImg();
            }
        }
    }
    private void resetDefaultImg(){
        if(mImgCoverArt!=null){
            if(mTypeUI==UI_PLAYER_CIRCLE_DISK || mTypeUI==UI_PLAYER_ROTATE_DISK){
                mImgCoverArt.setImageResource(R.drawable.ic_big_circle_img_default);
            }
            else{
                mImgCoverArt.setImageResource(R.drawable.ic_big_rect_img_default);
            }
            mImgBg.setImageResource(R.drawable.background_transparent);
            mImgOverlay.setVisibility(View.GONE);
        }
    }

    public void updateBackground(){
        try{
            if(mLayoutDragDropBg!=null){
                mContext.setUpBackground(mLayoutDragDropBg);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void notifyFavorite(long trackId, boolean isFav) {
        try{
            if(mContext!=null && mBtnLike!=null){
                RadioModel mRadioModel = YPYStreamManager.getInstance().getCurrentRadio();
                if(mRadioModel!=null){
                    if(mRadioModel.getId()==trackId){
                        mRadioModel.setFavorite(isFav);
                        mBtnLike.setLiked(isFav);
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void pauseRotateAnim(){
        try{
            if(mImgCoverArt!=null){
                if (rotate != null) {
                    mImgCoverArt.clearAnimation();
                    rotate.cancel();
                    rotate = null;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startRotateAnim() {
        try{
            if(mTypeUI==UI_PLAYER_ROTATE_DISK && mImgCoverArt!=null){
                if (rotate != null) {
                    mImgCoverArt.clearAnimation();
                    rotate.cancel();
                    rotate = null;
                }
                final float toDegrees = DEGREE*360;
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
                rotate.setDuration(DEGREE*DELTA_TIME);
                rotate.setRepeatCount(1000);
                mImgCoverArt.startAnimation(rotate);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    public void updateSleepMode(long value) {
        try {
            if (value > 0) {
                mTvSleepMode.setVisibility(View.VISIBLE);
                mTvSleepMode.setText(mContext.getStringTimer(value));
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




}
