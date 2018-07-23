package com.ypyglobal.xradio.ypylibs.ads;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.ypyglobal.xradio.ypylibs.task.IYPYCallback;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;


public class FBAdvertisement extends YPYAdvertisement {

    public static final String FB_ADS="facebook";
    private AdView fbAdView;
    private InterstitialAd mFBInterstitialAd;
    private InterstitialAd loopInterstitialAd;
    private AdView fbAdMediumView;

    public FBAdvertisement(Context mContext,String bannerId,String interstitialId,String testId) {
        super(mContext,FB_ADS,bannerId,interstitialId,testId);
    }
    public FBAdvertisement(Context mContext,String bannerId,String interstitialId,String testId,long timeout) {
        super(mContext,FB_ADS,bannerId,interstitialId,testId,timeout);
    }

    @Override
    public void setUpAdBanner(ViewGroup mLayoutAds, boolean isAllowShowAds) {
        if (isAllowShowAds) {
            if (ApplicationUtils.isOnline(mContext)
                    && mLayoutAds != null && mLayoutAds.getChildCount()==0) {
                if(fbAdView!=null){
                    fbAdView.destroy();
                }
                fbAdView = new AdView(mContext, bannerId, AdSize.BANNER_HEIGHT_50);
                mLayoutAds.addView(fbAdView);
                AdSettings.addTestDevice(testId);
                fbAdView.setAdListener(new AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        mLayoutAds.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                });
                // Request an ad
                fbAdView.loadAd();
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
            if (ApplicationUtils.isOnline(mContext)
                    && mLayoutAds != null && mLayoutAds.getChildCount()==0) {
                if(fbAdMediumView!=null){
                    fbAdMediumView.destroy();
                }
                fbAdMediumView = new AdView(mContext, mediumId, AdSize.RECTANGLE_HEIGHT_250);
                mLayoutAds.addView(fbAdMediumView);
                AdSettings.addTestDevice(testId);
                fbAdMediumView.setAdListener(new AdListener() {
                    @Override
                    public void onError(Ad ad, AdError adError) {

                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        mLayoutAds.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                });
                // Request an ad
                fbAdMediumView.loadAd();
                mLayoutAds.setVisibility(View.GONE);
                return;

            }
        }
        if(mLayoutAds!=null && mLayoutAds.getChildCount()==0){
            mLayoutAds.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInterstitialAd(boolean isAllowShowAds, IYPYCallback mCallback) {
        if (ApplicationUtils.isOnline(mContext) && isAllowShowAds) {
            mFBInterstitialAd = new InterstitialAd(mContext, interstitialId);
            AdSettings.addTestDevice(testId);
            mFBInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    if(mCallback!=null){
                        mCallback.onAction();
                    }
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e("DCM","=========>onError");
                    if(mCallback!=null){
                        mCallback.onAction();
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("DCM","=========>onAdLoaded");
                    try{
                        mHandlerAds.removeCallbacksAndMessages(null);
                        if(mFBInterstitialAd !=null){
                            mFBInterstitialAd.show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            });
            mFBInterstitialAd.loadAd();
            mHandlerAds.postDelayed(() -> {
                if(mFBInterstitialAd!=null){
                    mFBInterstitialAd.setAdListener(null);
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
        if (loopInterstitialAd != null && loopInterstitialAd.isAdLoaded()) {
            loopInterstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    try {
                        if (!isDestroy && loopInterstitialAd != null) {
                            loopInterstitialAd.loadAd();
                            if (mCallback != null) {
                                mCallback.onAction();
                            }
                        }

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {

                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

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
    public void setUpLoopInterstitial() {
        try {
            if (ApplicationUtils.isOnline(mContext) && loopInterstitialAd == null) {
                loopInterstitialAd = new InterstitialAd(mContext, interstitialId);
                AdSettings.addTestDevice(testId);
                loopInterstitialAd.loadAd();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            if(loopInterstitialAd!=null){
                loopInterstitialAd.destroy();
            }
            if(mFBInterstitialAd!=null){
                mFBInterstitialAd.destroy();
            }
            if(fbAdView!=null){
                fbAdView.destroy();
            }
            if(fbAdMediumView!=null){
                fbAdMediumView.destroy();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
