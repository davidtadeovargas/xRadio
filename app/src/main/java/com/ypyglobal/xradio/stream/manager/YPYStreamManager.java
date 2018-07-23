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

package com.ypyglobal.xradio.stream.manager;


import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.stream.mediaplayer.YPYMediaPlayer;

import java.util.ArrayList;

public class YPYStreamManager {

    public static final String TAG = YPYStreamManager.class.getSimpleName();
    private static YPYStreamManager musicManager;
    private ArrayList<RadioModel> listMusicRadio;

    private int currentIndex=-1;
    private RadioModel currentData;
    private boolean isLoading;
    private YPYMediaPlayer mediaPlayer;
    private YPYMediaPlayer.StreamInfo streamInfo;

    public static YPYStreamManager getInstance() {
        if (musicManager == null) {
            musicManager = new YPYStreamManager();
        }
        return musicManager;
    }

    private YPYStreamManager() {

    }

    public void onDestroy(){
        if(listMusicRadio !=null){
            listMusicRadio.clear();
            listMusicRadio =null;
        }
        currentIndex=-1;
        currentData=null;
        musicManager=null;
    }

    public ArrayList<RadioModel> getListMusicRadio() {
        return listMusicRadio;
    }

    public boolean setCurrentData(RadioModel RadioModel){
        if(listMusicRadio!=null && listMusicRadio.size()>0){
            for(RadioModel mStreamRadioObject1:listMusicRadio){
                if(mStreamRadioObject1.getId()==RadioModel.getId()){
                    currentData=mStreamRadioObject1;
                    currentIndex=listMusicRadio.indexOf(mStreamRadioObject1);
                    return true;
                }
            }
        }
        return false;
    }
    public void setCurrentData(int indexRadio){
        if(listMusicRadio!=null && listMusicRadio.size()>0){
            if(indexRadio<listMusicRadio.size() && indexRadio>=0){
                setCurrentData(listMusicRadio.get(indexRadio));
            }
        }
    }

    public void setListMusicRadio(ArrayList<RadioModel> listMusicRadio) {
        if(this.listMusicRadio !=null){
            this.listMusicRadio.clear();
            this.listMusicRadio =null;
        }
        this.currentIndex=-1;
        this.currentData=null;
        this.listMusicRadio = listMusicRadio;
        int size =listMusicRadio!=null ?listMusicRadio.size():0;
        if(size>0){
            currentIndex=0;
            currentData=listMusicRadio.get(currentIndex);
        }
    }

    public RadioModel getCurrentRadio() {
        return currentData;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public RadioModel nextPlay(){
        int size =listMusicRadio!=null ?listMusicRadio.size():0;
        if(size>0){
            currentIndex++;
            if(currentIndex>=size){
                currentIndex=0;
            }
            currentData=listMusicRadio.get(currentIndex);
            return currentData;
        }
        return null;
    }
    public RadioModel prevPlay(){
        int size =listMusicRadio!=null ?listMusicRadio.size():0;
        if(size>0){
            currentIndex--;
            if(currentIndex<0){
                currentIndex=size-1;
            }
            currentData=listMusicRadio.get(currentIndex);
            return currentData;
        }
        return null;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isPlaying(){
        if(mediaPlayer!=null){
            return mediaPlayer.isPlaying();
        }
        return false;
    }
    public boolean isPrepareDone(){
        if(mediaPlayer!=null){
            return true;
        }
        return false;
    }

    public void onResetMedia(){
        mediaPlayer=null;
        streamInfo=null;
    }

    public void setMediaPlayer(YPYMediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public YPYMediaPlayer.StreamInfo getStreamInfo() {
        return streamInfo;
    }

    public void setStreamInfo(YPYMediaPlayer.StreamInfo streamInfo) {
        this.streamInfo = streamInfo;
    }


}
