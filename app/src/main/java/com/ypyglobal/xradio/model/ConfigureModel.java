package com.ypyglobal.xradio.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class ConfigureModel {

    @SerializedName("api_key")
    private String apiKey;

    @SerializedName("lastfm_api_key")
    private String lastFmApiKey;

    @SerializedName("cache_expiration")
    private int cacheExpiration;

    @SerializedName("url_endpoint")
    private String urlEndPoint;

    @SerializedName("ad_type")
    private String adType;

    @SerializedName("banner_id")
    private String bannerId;

    @SerializedName("interstitial_id")
    private String interstitialId;

    @SerializedName("app_id")
    private String appId;

    public String getAdType() {
        return adType;
    }

    public String getBannerId() {
        return bannerId;
    }

    public String getInterstitialId() {
        return interstitialId;
    }

    public String getAppId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUrlEndPoint() {
        return urlEndPoint;
    }

    public boolean isOnlineApp(){
        return !TextUtils.isEmpty(urlEndPoint);
    }

    public String getLastFmApiKey() {
        return lastFmApiKey;
    }

    public int getCacheExpiration() {
        return cacheExpiration;
    }

}
