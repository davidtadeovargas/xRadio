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

package com.ypyglobal.xradio.ypylibs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.ypylibs.utils.IOUtils;

import java.io.File;


public abstract class YPYSplashActivity extends YPYFragmentActivity {

    public static final String TAG = YPYSplashActivity.class.getSimpleName();
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1000;
    public static final int REQUEST_PERMISSION_CODE = 1001;

    private boolean isCheckGoogle;
    private GoogleApiAvailability googleAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCheckGoogle) {
            isCheckGoogle = true;
            checkGooglePlayService();
        }
    }

    private void checkGooglePlayService() {
        googleAPI = GoogleApiAvailability.getInstance();
        try {
            int result = googleAPI.isGooglePlayServicesAvailable(this);
            if (result == ConnectionResult.SUCCESS) {
                startCheck();
            }
            else {
                if (googleAPI.isUserResolvableError(result)) {
                    isCheckGoogle = false;
                    googleAPI.showErrorDialogFragment(this, result, REQUEST_GOOGLE_PLAY_SERVICES);
                }
                else {
                    showToast(googleAPI.getErrorString(result));
                    backToHome();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }





    private void startCheck() {
        File mFile = getDirectoryCached();
        if (mFile == null) {
            createFullDialog(-1, R.string.title_info, R.string.title_settings, R.string.title_cancel,
                    getString(R.string.info_error_sdcard), () -> {
                        isCheckGoogle = false;
                        startActivityForResult(new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS), 0);

                    }, () -> backToHome()).show();
            return;
        }
        boolean b = isNeedGrantPermission();
        if (!b) {
            onInitData();
        }

    }
    private boolean isNeedGrantPermission() {
        try {
            if (IOUtils.isMarshmallow()) {
                if (!isGrantAllPermission(getListPermissionNeedGrant())) {
                    ActivityCompat.requestPermissions(this, getListPermissionNeedGrant(), REQUEST_PERMISSION_CODE);
                    return true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == REQUEST_PERMISSION_CODE) {
                if (isGrantAllPermission(grantResults)) {
                    startCheck();
                }
                else {
                    showToast(R.string.info_permission_denied);
                    backToHome();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            showToast(R.string.info_permission_denied);
            backToHome();
        }

    }

    public abstract int getResId();
    public abstract void onInitData() ;
    public abstract File getDirectoryCached() ;
    public abstract String[] getListPermissionNeedGrant() ;


    @Override
    public boolean backToHome() {
        onDestroyData();
        finish();
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
