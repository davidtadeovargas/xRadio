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

package com.ypyglobal.xradio.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.ypyglobal.xradio.dataMng.XRadioNetUtils;
import com.ypyglobal.xradio.ypylibs.model.AbstractModel;


public class RadioModel extends AbstractModel {

    @SerializedName("bitrate")
    private String bitRate;

    @SerializedName("tags")
    private String tags;

    @SerializedName("type_radio")
    private String typeRadio;

    @SerializedName("source_radio")
    private String sourceRadio;

    @SerializedName("link_radio")
    private String linkRadio;

    @SerializedName("user_agent_radio")
    private String userAgentRadio;

    @SerializedName("url_facebook")
    private String urlFacebook;

    @SerializedName("url_twitter")
    private String urlTwitter;

    @SerializedName("url_instagram")
    private String urlInstagram;

    @SerializedName("url_website")
    private String urlWebsite;

    private transient String song;
    private transient String artist;
    private transient String songImg;

    public RadioModel(long id, String name, String image) {
        super(id, name, image);
    }

    public String getBitRate() {
        return bitRate;
    }

    public String getTags() {
        return tags;
    }

    public String getTypeRadio() {
        return typeRadio;
    }

    public String getSourceRadio() {
        return sourceRadio;
    }

    public String getLinkRadio() {
        return linkRadio;
    }

    public String getUserAgentRadio() {
        return userAgentRadio;
    }

    public String getUrlFacebook() {
        return urlFacebook;
    }

    public String getUrlTwitter() {
        return urlTwitter;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTypeRadio(String typeRadio) {
        this.typeRadio = typeRadio;
    }

    public void setSourceRadio(String sourceRadio) {
        this.sourceRadio = sourceRadio;
    }

    public void setLinkRadio(String linkRadio) {
        this.linkRadio = linkRadio;
    }

    public void setUserAgentRadio(String userAgentRadio) {
        this.userAgentRadio = userAgentRadio;
    }

    public void setUrlFacebook(String urlFacebook) {
        this.urlFacebook = urlFacebook;
    }

    public void setUrlTwitter(String urlTwitter) {
        this.urlTwitter = urlTwitter;
    }

    public void setUrlWebsite(String urlWebsite) {
        this.urlWebsite = urlWebsite;
    }

    @Override
    public RadioModel cloneObject() {
        RadioModel model= new RadioModel(id,name,image);
        model.setFavorite(isFavorite);
        model.setBitRate(bitRate);
        model.setLinkRadio(linkRadio);
        model.setTypeRadio(typeRadio);
        model.setSourceRadio(sourceRadio);
        model.setTags(tags);
        model.setUrlFacebook(urlFacebook);
        model.setUrlTwitter(urlTwitter);
        model.setUrlWebsite(urlWebsite);
        model.setUrlInstagram(urlInstagram);
        model.setUserAgentRadio(userAgentRadio);
        return model;
    }
    @Override
    public String getArtWork(String urlHost) {
        if(!TextUtils.isEmpty(image) && !image.startsWith("http") && !TextUtils.isEmpty(urlHost)){
            image=urlHost+ XRadioNetUtils.FOLDER_RADIOS+image;
        }
        return super.getImage();
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String getShareStr() {
        StringBuilder mStringBuilder = new StringBuilder();
        if(!TextUtils.isEmpty(name)){
            mStringBuilder.append(name+"\n");
        }
        return mStringBuilder.toString();
    }

    public String getSongImg() {
        return songImg;
    }

    public void setSongImg(String songImg) {
        this.songImg = songImg;
    }

    public String getMetaData(){
        if(!TextUtils.isEmpty(song)){
            StringBuilder mStringBuilder = new StringBuilder();
            mStringBuilder.append(song);
            if(!TextUtils.isEmpty(artist)){
                mStringBuilder.append(" - ");
                mStringBuilder.append(artist);
            }
            return mStringBuilder.toString();

        }
        return null;
    }

    public String getUrlInstagram() {
        return urlInstagram;
    }

    public void setUrlInstagram(String urlInstagram) {
        this.urlInstagram = urlInstagram;
    }
}
