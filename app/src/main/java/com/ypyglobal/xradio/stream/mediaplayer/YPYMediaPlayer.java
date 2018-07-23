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

package com.ypyglobal.xradio.stream.mediaplayer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.un4seen.bass.BASS;
import com.un4seen.bass.BASSHLS;


public class YPYMediaPlayer {
    public static final String TAG = YPYMediaPlayer.class.getSimpleName();

    public static final int TYPE_STREAM_AAC = 1;
    public static final int TYPE_STREAM_MP3 = 2;

    public static final long MAX_PERCENT_BUFFERING = 75;
    public static final int BASS_SYNC_HLS_SEGMENT = 0x10300;
    public static final int BASS_TAG_HLS_EXTINF = 0x14000;

    private Context mContext;

    private String mUrlStream;
    private int mChannelHandle;
    private String mUserAgent;
    private OnStreamListener onStreamListener;
    private Handler mHandler;
    private boolean isPlaying;

    public YPYMediaPlayer(Context mContext) {
        this.mContext = mContext;
        this.mHandler= new Handler(Looper.getMainLooper());
    }

    public YPYMediaPlayer(Context mContext, String mUserAgent) {
        this.mContext = mContext;
        this.mUserAgent = mUserAgent;
        this.mHandler= new Handler(Looper.getMainLooper());
    }

    private boolean onInitAudioDevice() {
        if (!BASS.BASS_Init(-1, 44100, 0)) {
            new Exception(TAG + " Can't initialize device").printStackTrace();
            return false;
        }
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_NET_PLAYLIST, 1);
        BASS.BASS_SetConfig(BASS.BASS_CONFIG_NET_PREBUF_WAIT, 0);
        if (!TextUtils.isEmpty(mUserAgent)) {
            BASS.BASS_SetConfigPtr(BASS.BASS_CONFIG_NET_AGENT, mUserAgent); // set proxy server
        }
        String path = mContext.getApplicationInfo().nativeLibraryDir;
        int plug1 = BASS.BASS_PluginLoad(path + "/" + "libbass_aac.so", 0);
        int plug2 = BASS.BASS_PluginLoad(path + "/" + "libbasshls.so", 0);
        if (plug1 != 0 && plug2!=0) {
            return true;
        }
        return false;

    }

    public void release() {
        isPlaying=false;
        mHandler.removeCallbacksAndMessages(null);
        if (mChannelHandle != 0) {
            BASS.BASS_StreamFree(mChannelHandle);
        }
        BASS.BASS_Free();
        BASS.BASS_PluginFree(0);
        mChannelHandle = 0;
    }

    public void setVolume(float volume) {
        if (mChannelHandle != 0) {
            BASS.BASS_ChannelSetAttribute(mChannelHandle, BASS.BASS_ATTRIB_VOL, volume);
        }
    }

    public void setOnStreamListener(OnStreamListener onStreamListener) {
        this.onStreamListener = onStreamListener;
    }

    private BASS.SYNCPROC mEndSync = new BASS.SYNCPROC() {
        public void SYNCPROC(int handle, int channel, int data, Object user) {
            if(onStreamListener!=null){
                onStreamListener.onComplete();
            }
        }
    };

    BASS.SYNCPROC mMetaSync =new BASS.SYNCPROC() {
        public void SYNCPROC(int handle, int channel, int data, Object user) {
            StreamInfo mStreamInfo = getStreamInfo();
            if(onStreamListener!=null){
                onStreamListener.onUpdateMetaData(mStreamInfo);
            }
        }
    };

    public void setDataSource(String url) {
        if (!TextUtils.isEmpty(url)) {
            boolean isInit = onInitAudioDevice();
            if (isInit) {
                this.mUrlStream = url;
//                if (mTypeStream == TYPE_STREAM_AAC) {
//                    mChannelHandle = BASS_AAC.BASS_AAC_StreamCreateURL(mUrlStream, 0, BASS.BASS_STREAM_BLOCK | BASS.BASS_STREAM_STATUS | BASS.BASS_STREAM_AUTOFREE, null, 0);
//                }
//                else if (mTypeStream == TYPE_STREAM_MP3) {
//                }
                mChannelHandle = BASS.BASS_StreamCreateURL(mUrlStream, 0, BASS.BASS_STREAM_BLOCK | BASS.BASS_STREAM_STATUS | BASS.BASS_STREAM_AUTOFREE, null, 0);
                if (mChannelHandle != 0) {
                    Runnable mPlayRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // percentage of buffer filled
                            long progress = BASS.BASS_StreamGetFilePosition(mChannelHandle, BASS.BASS_FILEPOS_BUFFER) * 100
                                    / BASS.BASS_StreamGetFilePosition(mChannelHandle, BASS.BASS_FILEPOS_END);
                            // over 75% full (or end of download)
                            if (progress > MAX_PERCENT_BUFFERING || BASS.BASS_StreamGetFilePosition(mChannelHandle, BASS.BASS_FILEPOS_CONNECTED) == 0) {
                                BASS.BASS_ChannelSetSync(mChannelHandle, BASS.BASS_SYNC_META, 0, mMetaSync, 0); // Shoutcast
                                BASS.BASS_ChannelSetSync(mChannelHandle, BASS.BASS_SYNC_OGG_CHANGE, 0, mMetaSync, 0); // Icecast/OGG
                                BASS.BASS_ChannelSetSync(mChannelHandle, BASSHLS.BASS_SYNC_HLS_SEGMENT, 0, mMetaSync, 0); // Hls
                                BASS.BASS_ChannelSetSync(mChannelHandle, BASS.BASS_SYNC_END, 0, mEndSync, 0);
                                mHandler.removeCallbacksAndMessages(null);
                                if (onStreamListener != null) {
                                    onStreamListener.onPrepare();
                                }
                            }
                            else {
                                if (onStreamListener != null) {
                                    onStreamListener.onBuffering(progress);
                                }
                                mHandler.postDelayed(this, 50);
                            }


                        }
                    };
                    mHandler.postDelayed(mPlayRunnable, 50);
                    return;
                }
            }
        }
        if (onStreamListener != null) {
            onStreamListener.onError();
        }
    }

    public void start(){
        if(mChannelHandle!=0){
            isPlaying= BASS.BASS_ChannelPlay(mChannelHandle, false);
        }
    }
    public void pause(){
        if(mChannelHandle!=0){
            BASS.BASS_ChannelPause(mChannelHandle);
            isPlaying=false;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public StreamInfo getStreamInfo(){
        try{
            if(mChannelHandle!=0){
                String mData = (String) BASS.BASS_ChannelGetTags(mChannelHandle, BASS.BASS_TAG_META);
                if(!TextUtils.isEmpty(mData)){
                    StreamInfo mStreamInfo = new StreamInfo();
                    int ti = mData.indexOf("StreamTitle='");
                    if (ti >= 0) {
                        String title = mData.substring(ti + 13, mData.indexOf("';", ti + 13));
                        if(title!=null && title.contains("-")){
                            String[] datas = title.split("\\ - +");
                            if(datas!=null && datas.length>=2){
                                mStreamInfo.artist = datas[0].trim().replace("f./","ft");
                                mStreamInfo.title = datas[1].trim();
                                if(mStreamInfo.title.contains("-@")){
                                    mStreamInfo.title=mStreamInfo.title.substring(0,mStreamInfo.title.lastIndexOf("-@")).trim();
                                }
                                return mStreamInfo;
                            }

                        }
                        mStreamInfo.title = title;
                    }
                    return mStreamInfo;
                }
                else {
                    String[] ogg = (String[]) BASS.BASS_ChannelGetTags(mChannelHandle, BASS.BASS_TAG_OGG);
                    if (ogg != null && ogg.length > 0) {
                        StreamInfo mStreamInfo = new StreamInfo();
                        for (String s : ogg) {
                            if (s.regionMatches(true, 0, "artist=", 0, 7)) {
                                mStreamInfo.artist =s.substring(7);
                            }
                            else if (s.regionMatches(true, 0, "title=", 0, 6)) {
                                mStreamInfo.title = s.substring(6);
                            }
                        }
                        return mStreamInfo;
                    }
                    else {
                        mData=(String)BASS.BASS_ChannelGetTags(mChannelHandle, BASS_TAG_HLS_EXTINF);
                        if (!TextUtils.isEmpty(mData)) { // got HLS segment info
                            int i=mData.indexOf(',');
                            if (i>0){
                                StreamInfo mStreamInfo = new StreamInfo();
                                mStreamInfo.title=mData.substring(i+1);
                                return mStreamInfo;
                            }
                        }
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


    public interface OnStreamListener {
        public void onPrepare();
        public void onError();
        public void onComplete();
        public void onBuffering(long percent);
        public void onUpdateMetaData(StreamInfo info);
    }

    public class StreamInfo {
        public String title;
        public String artist;
        public String imgUrl;
    }

}
