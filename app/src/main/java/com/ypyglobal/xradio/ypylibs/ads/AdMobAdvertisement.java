package com.ypyglobal.xradio.ypylibs.ads;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.ypyglobal.xradio.ypylibs.task.IYPYCallback;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;



public class AdMobAdvertisement extends YPYAdvertisement{

    public static final String ADMOB_ADS="admob";

    private AdView adView;
    private InterstitialAd interstitialAd;
    private InterstitialAd loopInterstitialAd;
    private AdView adMediumView;

    public AdMobAdvertisement(Context mContext,String bannerId,String interstitialId,String testId) {
        super(mContext,ADMOB_ADS,bannerId,interstitialId,testId);
    }

    public AdMobAdvertisement(Context mContext,String bannerId,String interstitialId,String testId,long timeout) {
        super(mContext,ADMOB_ADS,bannerId,interstitialId,testId,timeout);
    }

    public void setAppId(String appId) {
        if(!TextUtils.isEmpty(appId)){
            MobileAds.initialize(mContext, appId);
        }
    }

    @Override
    public void setUpAdBanner(ViewGroup mLayoutAds, boolean isAllowShowAds) {
        if (isAllowShowAds) {
            if (ApplicationUtils.isOnline(mContext) && mLayoutAds != null && mLayoutAds.getChildCount()==0) {
                if(adView!=null){
                    adView.destroy();
                }
                adView = new AdView(mContext);
                adView.setAdUnitId(bannerId);
                adView.setAdSize(AdSize.SMART_BANNER);
                mLayoutAds.addView(adView);
                AdRequest mAdRequest = new AdRequest.Builder().addTestDevice(testId==null?"":testId).build();
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mLayoutAds.setVisibility(View.VISIBLE);

                    }
                });
                adView.loadAd(mAdRequest);
                mLayoutAds.setVisibility(View.GONE);
                return;

            }
        }
        if(mLayoutAds!=null && mLayoutAds.getChildCount()==0){
            mLayoutAds.setVisibility(View.GONE);
        }
    }

    @Override
    public void setUpMediumBanner(ViewGroup mLayoutAds, boolean isAllowShowAds) {
        if (isAllowShowAds) {
            if (ApplicationUtils.isOnline(mContext) && mLayoutAds != null && mLayoutAds.getChildCount()==0) {
                if(adMediumView!=null){
                    adMediumView.destroy();
                }
                adMediumView = new AdView(mContext);
                adMediumView.setAdUnitId(mediumId);
                adMediumView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                mLayoutAds.addView(adMediumView);
                AdRequest mAdRequest = new AdRequest.Builder().addTestDevice(testId==null?"":testId).build();
                adMediumView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mLayoutAds.setVisibility(View.VISIBLE);

                    }
                });
                adMediumView.loadAd(mAdRequest);
                mLayoutAds.setVisibility(View.GONE);
                return;

            }
        }
        if(mLayoutAds!=null && mLayoutAds.getChildCount()==0){
            mLayoutAds.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInterstitialAd(boolean isAllowShowAds,IYPYCallback mCallback) {
        if (ApplicationUtils.isOnline(mContext) && isAllowShowAds) {
            interstitialAd = new InterstitialAd(mContext.getApplicationContext());
            interstitialAd.setAdUnitId(interstitialId);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(testId==null?"":testId).build();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mHandlerAds.removeCallbacksAndMessages(null);
                    try{
                        if(interstitialAd !=null){
                            interstitialAd.show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (mCallback != null) {
                        mCallback.onAction();
                    }
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    if (mCallback != null) {
                        mCallback.onAction();
                    }
                }
            });
            interstitialAd.loadAd(adRequest);
            mHandlerAds.postDelayed(() -> {
                if(interstitialAd!=null){
                    interstitialAd.setAdListener(null);
                    interstitialAd=null;
                }
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }, timeOutLoadAds);
            return;

        }
        if (mCallback != null) {
            mCallback.onAction();
        }
    }

    @Override
    public void showLoopInterstitialAd(IYPYCallback mCallback) {
        if (loopInterstitialAd != null && loopInterstitialAd.isLoaded()) {
            YPYLog.e("DCM", "=========>showLoopInterstitialAd");
            loopInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    try {
                        if (!isDestroy && loopInterstitialAd != null) {
                            AdRequest adRequest = new AdRequest.Builder().addTestDevice(testId).build();
                            loopInterstitialAd.loadAd(adRequest);
                            if (mCallback != null) {
                                mCallback.onAction();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            loopInterstitialAd.show();
            return;
        }
        if (mCallback != null) {
            mCallback.onAction();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if(adView!=null){
                adView.destroy();
            }
            if(adMediumView!=null){
                adMediumView.destroy();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setUpLoopInterstitial() {
        try {
            if (ApplicationUtils.isOnline(mContext) && loopInterstitialAd == null) {
                loopInterstitialAd = new InterstitialAd(mContext.getApplicationContext());
                loopInterstitialAd.setAdUnitId(interstitialId);
                AdRequest adRequest = new AdRequest.Builder().addTestDevice(testId).build();
                loopInterstitialAd.loadAd(adRequest);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
