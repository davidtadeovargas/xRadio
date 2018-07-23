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

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.setting.XRadioSettingManager;
import com.ypyglobal.xradio.ypylibs.ads.YPYAdvertisement;
import com.ypyglobal.xradio.ypylibs.fragment.IYPYFragmentConstants;
import com.ypyglobal.xradio.ypylibs.fragment.YPYFragment;
import com.ypyglobal.xradio.ypylibs.imageloader.target.GlideViewGroupTarget;
import com.ypyglobal.xradio.ypylibs.listener.IYPYSearchViewInterface;
import com.ypyglobal.xradio.ypylibs.task.IYPYCallback;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.IOUtils;
import com.ypyglobal.xradio.ypylibs.utils.ResolutionUtils;
import com.ypyglobal.xradio.ypylibs.view.DividerItemDecoration;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;



public class YPYFragmentActivity extends AppCompatActivity implements IYPYFragmentConstants {

    public static final String TAG = YPYFragmentActivity.class.getSimpleName();

    //fuck support appcombat
    public static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    public static final int[] CHECKED_STATE_SET = new int[]{android.R.attr.state_checked};
    public static final int[] EMPTY_STATE_SET = new int[0];

    private Dialog mProgressDialog;

    private int screenWidth;
    private int screenHeight;

    public ArrayList<Fragment> mListFragments;

    private boolean isAllowPressMoreToExit;
    private int countToExit;
    private long pivotTime;

    private ConnectionChangeReceiver mNetworkBroadcast;
    private INetworkListener mNetworkListener;

    public ViewGroup mLayoutAds;
    public SearchView searchView;

    public Drawable mBackDrawable;
    public int mContentActionColor;
    public int mIconColor;

    public YPYAdvertisement mAdvertisement;
    private BlurTransformation mBlurBgTranform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFormat(PixelFormat.RGBA_8888);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                |WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        this.createProgressDialog();

        this.mContentActionColor =getResources().getColor(R.color.icon_action_bar_color);
        this.mIconColor =getResources().getColor(R.color.icon_color);

        this.mBackDrawable = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        this.mBackDrawable.setColorFilter(mContentActionColor, PorterDuff.Mode.SRC_ATOP);
        this.mBlurBgTranform = new BlurTransformation(this,10);

        int[] mRes = ResolutionUtils.getDeviceResolution(this);
        if (mRes != null && mRes.length == 2) {
            screenWidth = mRes[0];
            screenHeight = mRes[1];
        }
    }



    public void onStartCreateAds(){
        mAdvertisement=createAds();
    }

    public YPYAdvertisement createAds(){
        return null;
    }


    public void setUpBackground(RelativeLayout mLayoutBg){
        try{
            if(mLayoutBg!=null){
                GlideViewGroupTarget mTarget = new GlideViewGroupTarget(this, mLayoutBg) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        super.setResource(resource);
                    }
                };
                String bg=XRadioSettingManager.getBackgroundUrl(this);
                if(!TextUtils.isEmpty(bg)){
                    Glide.with(this).load(Uri.parse(bg)).asBitmap()
                            .transform(mBlurBgTranform)
                            .placeholder(R.drawable.default_bg_app).into(mTarget);
                }
                else{
                    String startColor = XRadioSettingManager.getStartColor(this);
                    int orientation = XRadioSettingManager.getOrientation(this);
                    String endColor = XRadioSettingManager.getEndColor(this);
                    if (!TextUtils.isEmpty(startColor) || !TextUtils.isEmpty(endColor)) {
                        GradientDrawable.Orientation gradOrientation = ApplicationUtils.getOrientation(orientation);
                        GradientDrawable gradientDrawable = getGradientDrawable(parseColor(startColor),0,parseColor(endColor),gradOrientation);
                        mLayoutBg.setBackground(gradientDrawable);
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public int parseColor(String color){
        try{
            if(!TextUtils.isEmpty(color) && color.startsWith("#") && color.length()>=7){
                return Color.parseColor(color);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public void setUpOverlayBackground(boolean useBg) {
        if(IOUtils.isLollipop()){
            if(useBg){
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public void setUpImageViewBaseOnColor(View mView, int color, int idDrawabe, boolean isReset) {
        Drawable mDrawable = getResources().getDrawable(idDrawabe);
        if (isReset) {
            mDrawable.clearColorFilter();
        }
        else {
            mDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        if (mView instanceof Button) {
            mView.setBackgroundDrawable(mDrawable);
        }
        else if (mView instanceof ImageView) {
            ((ImageView) mView).setImageDrawable(mDrawable);
        }
        else if (mView instanceof ImageButton) {
            ((ImageView) mView).setImageDrawable(mDrawable);
        }
    }

    public void setUpBottomBanner(int resId, boolean isAllowShowAds) {
        setUpAdBanner(findViewById(resId),isAllowShowAds);
    }


    public void setUpAdBanner(ViewGroup mLayoutAds, boolean isAllowShowAds) {
        this.mLayoutAds = mLayoutAds;
        if(mAdvertisement!=null){
            mAdvertisement.setUpAdBanner(mLayoutAds,isAllowShowAds);
        }
        else{
            hideAds();
        }
    }


    public void showInterstitialAd(boolean isAllowShowAds,final IYPYCallback mCallback) {
        if(mAdvertisement!=null){
            mAdvertisement.showInterstitialAd(isAllowShowAds,mCallback);
        }
        else{
            if(mCallback!=null){
                mCallback.onAction();
            }
        }

    }

    public void hideAds() {
        if(mLayoutAds!=null){
            mLayoutAds.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdvertisement!=null){
            mAdvertisement.onDestroy();
        }
        if (mNetworkBroadcast != null) {
            unregisterReceiver(mNetworkBroadcast);
            mNetworkBroadcast = null;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (searchView != null && !searchView.isIconified()) {
                hiddenKeyBoardForSearchView();
                return true;
            }
            boolean b=backToHome();
            if(b){
                return true;
            }
            if (!isAllowPressMoreToExit) {
                showQuitDialog();
            }
            else {
                pressMoreToExitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean backToHome(){
        if(isFragmentCheckBack()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            return backToHome();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setIsAllowPressMoreToExit(boolean isAllowPressMoreToExit) {
        this.isAllowPressMoreToExit = isAllowPressMoreToExit;
    }

    private void pressMoreToExitApp() {
        if (countToExit >= 1) {
            long delaTime = System.currentTimeMillis() - pivotTime;
            if (delaTime <= 2000) {
                onDestroyData();
                finish();
                return;
            }
            else {
                countToExit = 0;
            }
        }
        pivotTime = System.currentTimeMillis();
        showToast(R.string.info_press_again_to_exit);
        countToExit++;
    }

    public MaterialDialog createFullDialog(int iconId, int mTitleId, int mYesId, int mNoId, String messageId, final IYPYCallback mCallback, final IYPYCallback mNeCallback) {
        MaterialDialog.Builder mBuilder = new MaterialDialog.Builder(this);
        mBuilder.title(mTitleId);
        if (iconId != -1) {
            mBuilder.iconRes(iconId);
        }
        mBuilder.content(messageId);
        mBuilder.backgroundColor(getResources().getColor(R.color.dialog_bg_color));
        mBuilder.titleColor(getResources().getColor(R.color.dialog_color_text));
        mBuilder.contentColor(getResources().getColor(R.color.dialog_color_text));
        mBuilder.positiveColor(getResources().getColor(R.color.colorAccent));
        mBuilder.negativeColor(getResources().getColor(R.color.dialog_color_secondary_text));
        mBuilder.negativeText(mNoId);
        mBuilder.positiveText(mYesId);
        mBuilder.autoDismiss(true);
        mBuilder.callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                if (mCallback != null) {
                    mCallback.onAction();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                if (mNeCallback != null) {
                    mNeCallback.onAction();
                }
            }
        });
        return mBuilder.build();
    }


    public void showQuitDialog() {
        int mNoId = R.string.title_no;
        int mTitleId = R.string.title_confirm;
        int mYesId = R.string.title_yes;
        int iconId = R.drawable.ic_launcher;
        int messageId = R.string.info_close_app;

        createFullDialog(iconId, mTitleId, mYesId, mNoId, getString(messageId), new IYPYCallback() {
            @Override
            public void onAction() {
                onDestroyData();
                finish();
            }
        }, null).show();

    }

    private void createProgressDialog() {
        this.mProgressDialog = new Dialog(this);
        this.mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mProgressDialog.setContentView(R.layout.item_progress_bar);
        this.mProgressDialog.setCancelable(false);
        this.mProgressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            }
            return false;
        });
    }

    public void showProgressDialog() {
        showProgressDialog(R.string.info_loading);
    }

    public void showProgressDialog(int messageId) {
        showProgressDialog(getString(messageId));
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog != null) {
            TextView mTvMessage = mProgressDialog.findViewById(R.id.tv_message);
            mTvMessage.setText(message);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        }
    }

    public void dismissProgressDialog() {
        try{
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public int getScreenWidth() {
        return screenWidth;
    }

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    public void showToast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToastWithLongTime(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onDestroyData() {

    }

    public void createArrayFragment() {
        mListFragments = new ArrayList<>();
    }

    public void addFragment(Fragment mFragment) {
        if (mFragment != null && mListFragments != null) {
            synchronized (mListFragments) {
                mListFragments.add(mFragment);
            }
        }
    }

    public boolean backStack() {
        if (mListFragments != null && mListFragments.size() > 0) {
            int count = mListFragments.size();
            if (count > 0) {
                synchronized (mListFragments) {
                    Fragment mFragment = mListFragments.remove(count - 1);
                    if (mFragment != null) {
                        if (mFragment instanceof YPYFragment) {
                            ((YPYFragment) mFragment).backToHome(this);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public void goToFragment(String tag, int idContainer, String fragmentName, String parentTag, Bundle mBundle) {
        goToFragment(tag, idContainer, fragmentName, 0, parentTag, mBundle);
    }

    public void goToFragment(String tag, int idContainer, String fragmentName, int parentId, Bundle mBundle) {
        goToFragment(tag, idContainer, fragmentName, parentId, null, mBundle);
    }

    public void goToFragment(String tag, int idContainer, String fragmentName, int parentId, String parentTag, Bundle mBundle) {
        if (!TextUtils.isEmpty(tag) && getSupportFragmentManager().findFragmentByTag(tag) != null) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mBundle != null) {
            if (parentId != 0) {
                mBundle.putInt(KEY_ID_FRAGMENT, parentId);
            }
            if (!TextUtils.isEmpty(parentTag)) {
                mBundle.putString(KEY_NAME_FRAGMENT, parentTag);
            }
        }
        Fragment mFragment = Fragment.instantiate(this, fragmentName, mBundle);
        addFragment(mFragment);
        transaction.add(idContainer, mFragment, tag);
        if (parentId != 0) {
            Fragment mFragmentParent = getSupportFragmentManager().findFragmentById(parentId);
            if (mFragmentParent != null) {
                transaction.hide(mFragmentParent);
            }
        }
        if (!TextUtils.isEmpty(parentTag)) {
            Fragment mFragmentParent = getSupportFragmentManager().findFragmentByTag(parentTag);
            if (mFragmentParent != null) {
                transaction.hide(mFragmentParent);
            }
        }
        transaction.commit();
    }

    public void registerNetworkBroadcastReceiver(INetworkListener networkListener) {
        if (mNetworkBroadcast != null) {
            return;
        }
        mNetworkBroadcast = new ConnectionChangeReceiver();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetworkBroadcast, mIntentFilter);
        mNetworkListener = networkListener;
    }

    private class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mNetworkListener != null) {
                mNetworkListener.onNetworkState(ApplicationUtils.isOnline(YPYFragmentActivity.this));
            }
        }
    }

    public interface INetworkListener {
        public void onNetworkState(boolean isNetworkOn);
    }


    public void setActionBarTitle(String title) {
        ActionBar mAb = getSupportActionBar();
        if (mAb != null) {
            mAb.setTitle(title);
        }
    }

    public void setActionBarTitle(int titleId) {
        setActionBarTitle(getResources().getString(titleId));
    }

    public void showBackButton(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            if(mBackDrawable!=null){
                getSupportActionBar().setHomeAsUpIndicator(mBackDrawable);
            }
        }
    }

    public void setUpCustomizeActionBar(int actionBarBgColor){
        setUpCustomizeActionBar(actionBarBgColor,-1,false);
    }

    public void setUpCustomizeActionBar(int actionBarBgColor,boolean isShowBack){
        setUpCustomizeActionBar(actionBarBgColor,-1,isShowBack);
    }

    public void setUpCustomizeActionBar(int actionBarBgColor,int actionBarContentColor,boolean isNeedShowBack) {
        Toolbar mToolbar =findViewById(R.id.my_toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if(actionBarBgColor>=0){
                setColorForActionBar(actionBarBgColor);
            }
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
            int colorIcon=mContentActionColor;
            if(actionBarContentColor>=0){
                colorIcon=actionBarContentColor;
            }
            mToolbar.setTitleTextColor(colorIcon);
            drawable.setColorFilter(colorIcon, PorterDuff.Mode.SRC_ATOP);
            mToolbar.setOverflowIcon(drawable);

            this.mBackDrawable = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            this.mBackDrawable.setColorFilter(colorIcon, PorterDuff.Mode.SRC_ATOP);

            if(isNeedShowBack){
                showBackButton();
            }
        }
    }

    public void removeElevationActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setElevation(0);
        }
    }

    public void setColorForActionBar(int color) {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public void initSetupForSearchView(Menu menu, int idSearch, final IYPYSearchViewInterface mListener) {
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(idSearch));
        ImageView searchBtn = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        setUpImageViewBaseOnColor(searchBtn, mContentActionColor, R.drawable.ic_search_white_24dp, false);

        ImageView closeBtn = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        setUpImageViewBaseOnColor(closeBtn, mContentActionColor, R.drawable.ic_close_white_24dp, false);

        EditText searchEditText =searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(mContentActionColor);
        searchEditText.setHintTextColor(mContentActionColor);

        try{
            ImageView searchSubmit =searchView.findViewById (android.support.v7.appcompat.R.id.search_go_btn);
            if(searchSubmit!=null){
                searchSubmit.setColorFilter (mContentActionColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                hiddenKeyBoardForSearchView();
                if (mListener != null) {
                    mListener.onProcessSearchData(keyword);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                if (mListener != null) {
                    mListener.onStartSuggestion(keyword);
                }
                return true;
            }
        });
        searchView.setOnSearchClickListener(v -> {
            searchView.onActionViewExpanded();
            if (mListener != null) {
                mListener.onClickSearchView();
            }
        });
        searchView.setOnCloseListener(() -> {
            if (mListener != null) {
                mListener.onCloseSearchView();
            }
            return true;
        });
        searchView.setQueryHint(getString(R.string.title_search));
        searchView.setSubmitButtonEnabled(true);
    }

    public void hiddenKeyBoardForSearchView() {
        if (searchView != null && !searchView.isIconified()) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            ApplicationUtils.hiddenVirtualKeyboard(this, searchView);
        }
    }


    @Override
    public void onBackPressed() {
        if (searchView != null && !searchView.isIconified()) {
            hiddenKeyBoardForSearchView();
        }
        else {
            super.onBackPressed();
        }

    }
    public String getCurrentFragmentTag() {
        if (mListFragments != null && mListFragments.size() > 0) {
            Fragment mFragment = mListFragments.get(0);
            if (mFragment instanceof YPYFragment) {
                return mFragment.getTag();
            }
        }
        return null;
    }



    public void setUpRecyclerViewAsListView(RecyclerView mRecyclerView, Drawable mDivider){
        if(mDivider!=null){
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, mDivider));
        }
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutMngList = new LinearLayoutManager(this);
        mLayoutMngList.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutMngList);
    }

    public void setUpRecyclerViewAsGridView(RecyclerView mRecyclerView,int numberColumn,Drawable mDividerVer,Drawable mDividerHori) {
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,numberColumn);
        mRecyclerView.setLayoutManager(layoutManager);
        if(mDividerVer!=null){
            DividerItemDecoration mItemDecorVerti = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, mDividerVer);
            mRecyclerView.addItemDecoration(mItemDecorVerti);
        }
        if(mDividerHori!=null){
            DividerItemDecoration mHoriItem = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST, mDividerHori);
            mRecyclerView.addItemDecoration(mHoriItem);
        }

    }
    public void setUpRecyclerViewAsStaggered(RecyclerView mRecyclerView,int numberColumn,Drawable mDividerVer,Drawable mDividerHori) {
        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if(mDividerVer!=null){
            DividerItemDecoration mItemDecorVerti = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, mDividerVer);
            mRecyclerView.addItemDecoration(mItemDecorVerti);
        }
        if(mDividerHori!=null){
            DividerItemDecoration mHoriItem = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL_LIST, mDividerHori);
            mRecyclerView.addItemDecoration(mHoriItem);
        }

    }

    public boolean isGrantAllPermission(String[] grantResults){
        if(grantResults!=null && grantResults.length>0){
            for(String mStr:grantResults){
                if(ContextCompat.checkSelfPermission(this, mStr)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isGrantAllPermission(int[] grantResults){
        if(grantResults!=null && grantResults.length>0){
            int size = grantResults.length;
            for(int i=0;i<size;i++){
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isFragmentCheckBack(){
        if (mListFragments != null && mListFragments.size() > 0) {
            for (Fragment mFragment : mListFragments) {
                if (mFragment instanceof YPYFragment) {
                    boolean isBack = ((YPYFragment) mFragment).isCheckBack();
                    if (isBack) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public GradientDrawable getGradientDrawable(int startColor, int midColor, int endColor) {
        return getGradientDrawable(startColor,midColor,endColor, GradientDrawable.Orientation.TL_BR);
    }
    public GradientDrawable getGradientDrawable(int startColor, int midColor, int endColor,GradientDrawable.Orientation orientation) {
        GradientDrawable gradientDrawable;
        if (midColor == 0) {
            gradientDrawable = new GradientDrawable(
                    orientation,
                    new int[]{startColor, endColor});
        }
        else {
            gradientDrawable = new GradientDrawable(
                    orientation,
                    new int[]{startColor, midColor, endColor});
        }
        return gradientDrawable;
    }
    public Drawable getSupportDrawable(int resId) {
        try{
            if (resId != 0) {
                final Drawable d = AppCompatResources.getDrawable(this, resId);
                if (d != null) {
                    if (Build.VERSION.SDK_INT == 21
                            && VECTOR_DRAWABLE_CLAZZ_NAME.equals(d.getClass().getName())) {
                        fixVectorDrawableTinting(d);
                    }
                    return d;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    private void fixVectorDrawableTinting(final Drawable drawable) {
        try{
            final int[] originalState = drawable.getState();
            if (originalState == null || originalState.length == 0) {
                // The drawable doesn't have a state, so set it to be checked
                drawable.setState(CHECKED_STATE_SET);
            } else {
                // Else the drawable does have a state, so clear it
                drawable.setState(EMPTY_STATE_SET);
            }
            // Now set the original state
            drawable.setState(originalState);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
