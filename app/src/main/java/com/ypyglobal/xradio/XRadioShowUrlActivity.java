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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;

import butterknife.BindView;


public class XRadioShowUrlActivity extends XRadioFragmentActivity {

    public static final String TAG = XRadioShowUrlActivity.class.getSimpleName();
    public static final String KEY_HEADER = "KEY_HEADER";
    public static final String KEY_SHOW_URL = "KEY_SHOW_URL";

    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;

    @BindView(R.id.webview)
    WebView mWebViewShowPage;

    private String mUrl;

    private String mNameHeader;

    @Override
    protected void onDoBeforeSetView() {
        super.onDoBeforeSetView();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public int getResId() {
        return R.layout.activity_show_url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpCustomizeActionBar(Color.TRANSPARENT,true);
        Intent args = getIntent();
        if (args != null) {
            mUrl = args.getStringExtra(KEY_SHOW_URL);
            mNameHeader = args.getStringExtra(KEY_HEADER);
            YPYLog.d(TAG, "===========>url=" + mUrl);
            Toast.makeText(getApplicationContext(), mUrl, Toast.LENGTH_LONG).show();
        }
        if (!TextUtils.isEmpty(mNameHeader)) {
            setActionBarTitle(mNameHeader);
        }
        if(TextUtils.isEmpty(mUrl)){
            backToHome();
            return;
        }
        mWebViewShowPage.getSettings().setJavaScriptEnabled(true);
        mWebViewShowPage.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(mProgressBar!=null){
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        if(ApplicationUtils.isOnline(this)){
            if(!mUrl.startsWith("http")){
                mUrl = "http://"+mUrl;
            }
            mWebViewShowPage.loadUrl(mUrl);
        }

    }


    @Override
    public void onDoWhenNetworkOn() {
        super.onDoWhenNetworkOn();
        if(mWebViewShowPage!=null){
            mWebViewShowPage.loadUrl(mUrl);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWebViewShowPage != null) {
            mWebViewShowPage.destroy();
        }
    }

    @Override
    public boolean backToHome() {
        finish();
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebViewShowPage.canGoBack()) {
                mWebViewShowPage.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
