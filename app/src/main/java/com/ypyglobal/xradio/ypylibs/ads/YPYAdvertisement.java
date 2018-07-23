package com.ypyglobal.xradio.ypylibs.ads;

import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import com.ypyglobal.xradio.ypylibs.task.IYPYCallback;



public abstract class YPYAdvertisement {

    public static final long DEFAULT_TIME_OUT_LOAD_ADS = 15000;

    public Context mContext;
    private String adTypes;

    protected String testId;
    protected String bannerId;
    protected String mediumId;
    protected String interstitialId;

    public Handler mHandlerAds = new Handler();
    public long timeOutLoadAds=DEFAULT_TIME_OUT_LOAD_ADS;
    protected boolean isDestroy;


    public YPYAdvertisement(Context mContext,String adTypes,
                            String bannerId,String interstitialId,String testId) {
        this.mContext = mContext;
        this.adTypes=adTypes;
        this.bannerId=bannerId;
        this.interstitialId=interstitialId;
        this.testId=testId;
    }

    public YPYAdvertisement(Context mContext, String adTypes,String bannerId,
                            String interstitialId,String testId, long timeOutLoadAds) {
        this.mContext = mContext;
        this.adTypes = adTypes;
        this.timeOutLoadAds = timeOutLoadAds;
        this.bannerId=bannerId;
        this.interstitialId=interstitialId;
        this.testId=testId;
    }

    public String getAdTypes() {
        return adTypes;
    }

    public void setAdTypes(String adTypes) {
        this.adTypes = adTypes;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }

    public abstract void setUpAdBanner(ViewGroup mLayoutAds, boolean isAllowShowAds);
    public abstract void setUpMediumBanner(ViewGroup mLayoutAds, boolean isAllowShowAds);
    public abstract void showInterstitialAd(boolean isAllowShowAds,IYPYCallback mCallback);
    public abstract void showLoopInterstitialAd(IYPYCallback mCallback);
    public abstract void setUpLoopInterstitial();

    public void onDestroy(){
        isDestroy=true;
        mHandlerAds.removeCallbacksAndMessages(null);
    }

}
