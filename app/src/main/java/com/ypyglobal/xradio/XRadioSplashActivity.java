/*
 * Copyright (c) 2017. YPY Global - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *         http://ypyglobal.com/sourcecode/policy
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ypyglobal.xradio;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.wang.avi.AVLoadingIndicatorView;
import com.ypyglobal.xradio.dataMng.TotalDataManager;
import com.ypyglobal.xradio.model.ConfigureModel;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.model.UIConfigModel;
import com.ypyglobal.xradio.ypylibs.activity.YPYSplashActivity;
import com.ypyglobal.xradio.ypylibs.ads.AdMobAdvertisement;
import com.ypyglobal.xradio.ypylibs.ads.FBAdvertisement;
import com.ypyglobal.xradio.ypylibs.ads.YPYAdvertisement;
import com.ypyglobal.xradio.ypylibs.executor.YPYExecutorSupplier;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


import static com.ypyglobal.xradio.constants.IXRadioConstants.ADMOB_TEST_DEVICE;
import static com.ypyglobal.xradio.constants.IXRadioConstants.DEBUG;
import static com.ypyglobal.xradio.constants.IXRadioConstants.FACEBOOK_TEST_DEVICE;
import static com.ypyglobal.xradio.constants.IXRadioConstants.SHOW_ADS;



public class XRadioSplashActivity extends YPYSplashActivity {

    public static final String TAG = XRadioSplashActivity.class.getSimpleName();

    public static final String[] LIST_PERMISSIONS= {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Handler mHandler = new Handler();

    @BindView(R.id.progressBar1)
    AVLoadingIndicatorView mProgressBar;

    @BindView(R.id.layout_bg)
    RelativeLayout mLayoutBg;

    private TotalDataManager mTotalMng;
    private Unbinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        mBinder=ButterKnife.bind(this);
        YPYLog.setDebug(DEBUG);
        mTotalMng =TotalDataManager.getInstance();
        setUpBackground(mLayoutBg);
    }

    @Override
    public int getResId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onInitData() {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.show();
        YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            mTotalMng.readConfigure(this);
            runOnUiThread(() -> {
                setUpBackground(mLayoutBg);
                onStartCreateAds();
            });
            mTotalMng.readAllCache(this);
            runOnUiThread(() -> goToMainActivity());
        });
    }

    @Override
    public File getDirectoryCached() {
        return mTotalMng.getDirectoryCached();
    }

    @Override
    public String[] getListPermissionNeedGrant() {
        return LIST_PERMISSIONS;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBinder!=null){
            mBinder.unbind();
        }
        mHandler.removeCallbacksAndMessages(null);

    }

    public void goToMainActivity() {
        boolean isSingleRadio = mTotalMng.isSingleRadio();
        RadioModel mSingleRadio=mTotalMng.getSingRadioModel();
        if(isSingleRadio && mSingleRadio==null){
            boolean isOnline= ApplicationUtils.isOnline(this);
            showToast(isOnline?R.string.info_single_radio_error:R.string.info_connect_to_play);
            return;
        }
        showInterstitialAd(SHOW_ADS,() -> {
            try{
                if(mProgressBar!=null){
                    mProgressBar.hide();
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                UIConfigModel mUIConfigureModel = mTotalMng.getUiConfigModel();
                boolean isMulti=mUIConfigureModel!=null?mUIConfigureModel.isMultiApp():false;
                if(isMulti){
                    Intent mIntent = new Intent(XRadioSplashActivity.this, XMultiRadioMainActivity.class);
                    startActivity(mIntent);
                    finish();
                }
                else{
                    Intent mIntent = new Intent(XRadioSplashActivity.this, XSingleRadioMainActivity.class);
                    startActivity(mIntent);
                    finish();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

    }


    @Override
    public YPYAdvertisement createAds() {
        ConfigureModel model = mTotalMng.getConfigureModel();
        if(model!=null){
            String bannerId=model.getBannerId();
            String interstitialId=model.getInterstitialId();
            String adType = !TextUtils.isEmpty(model.getAdType()) ?model.getAdType(): AdMobAdvertisement.ADMOB_ADS;
            String appId=model.getAppId();

            if(adType.equalsIgnoreCase(AdMobAdvertisement.ADMOB_ADS)){
                AdMobAdvertisement mAdmob = new AdMobAdvertisement(this,bannerId,interstitialId,ADMOB_TEST_DEVICE );
                mAdmob.setAppId(appId);
                return mAdmob;
            }
            else if(adType.equalsIgnoreCase(FBAdvertisement.FB_ADS)){
                FBAdvertisement mFB = new FBAdvertisement(this, bannerId, interstitialId, FACEBOOK_TEST_DEVICE);
                return mFB;
            }
        }
        return null;

    }
}
