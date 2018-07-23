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

package com.ypyglobal.xradio.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.XMultiRadioMainActivity;
import com.ypyglobal.xradio.constants.IXRadioConstants;
import com.ypyglobal.xradio.model.ConfigureModel;
import com.ypyglobal.xradio.model.UIConfigModel;
import com.ypyglobal.xradio.ypylibs.adapter.YPYRecyclerViewAdapter;
import com.ypyglobal.xradio.ypylibs.executor.YPYExecutorSupplier;
import com.ypyglobal.xradio.ypylibs.fragment.YPYFragment;
import com.ypyglobal.xradio.ypylibs.model.AbstractModel;
import com.ypyglobal.xradio.ypylibs.model.ResultModel;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;
import com.ypyglobal.xradio.ypylibs.view.CircularProgressBar;
import com.ypyglobal.xradio.ypylibs.view.YPYRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;



public abstract class XRadioListFragment<T> extends YPYFragment implements IXRadioConstants
        ,YPYRecyclerView.OnDBRecyclerViewListener {

    protected XMultiRadioMainActivity mContext;
    protected ArrayList<T> mListModels;

    @BindView(R.id.list_datas)
    YPYRecyclerView mRecyclerView;

    @BindView(R.id.progressBar1)
    CircularProgressBar mProgressBar;

    @BindView(R.id.tv_no_result)
    TextView mTvNoResult;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.loading_footer)
    View mFooterView;

    public int mType=-1;
    private boolean isDestroy;
    public YPYRecyclerViewAdapter mAdapter;

    private boolean isAllowLoadMore;
    public boolean isShowWhenNoData;
    private boolean isAllowRefresh=true;
    private boolean isTab;

    public int mNumberItemPerPage=NUMBER_ITEM_PER_PAGE;
    public int mMaxPage=MAX_PAGE;
    private boolean isAllowReadCache;
    private boolean isOfflineData;
    private boolean isGetFromCacheWhenNoData;

    public UIConfigModel mUIConfigureModel;
    public ConfigureModel mConfigureMode;
    public String mUrlHost;
    public String mApiKey;
    public int mSizeH;

    @Override
    public View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void findView() {
        mContext=(XMultiRadioMainActivity)getActivity();
        mRefreshLayout.setOnRefreshListener(() -> onRefreshData(false));
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mRefreshLayout.setEnabled(isAllowRefresh);

        mUIConfigureModel= mContext.mTotalMng.getUiConfigModel();
        mConfigureMode=mContext.mTotalMng.getConfigureModel();

        mUrlHost=mConfigureMode!=null?mConfigureMode.getUrlEndPoint():null;
        mApiKey=mConfigureMode!=null?mConfigureMode.getApiKey():null;

        setUpUI();

        if(isAllowLoadMore){
            mRecyclerView.setOnDBListViewListener(this);
        }

        if((isTab && isFirstInTab()) || !isTab){
            startLoadData();
        }

    }

    public void onRefreshData(boolean isNeedHide){
        if(mContext!=null && mProgressBar!=null){
            if(mProgressBar!=null && mProgressBar.getVisibility()==View.VISIBLE){
                mRefreshLayout.setRefreshing(false);
                return;
            }
            if(isAllowLoadMore && mFooterView!=null && mFooterView.getVisibility()==View.VISIBLE){
                mRefreshLayout.setRefreshing(false);
                return;
            }
            onReceiveData(true,isNeedHide);
        }

    }


    @Override
    public void hideFooterView() {
        if(mFooterView!=null){
            mFooterView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFooterView() {
        if(mFooterView!=null){
            mFooterView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void startLoadData() {
        super.startLoadData();
        if(mContext!=null && !isLoadingData()){
            setLoadingData(true);
            onReceiveData(false,true);
        }
    }

    private void onReceiveData(boolean isNeedRefresh,boolean isNeedHideRecycler){
        if(isNeedRefresh){
            mRecyclerView.onResetData(false);
        }
        if(isNeedHideRecycler){
            mRecyclerView.setVisibility(View.GONE);
            showLoading(true);
        }
        YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            ArrayList<T> mListModels=null;
            ResultModel<T> resultModel=null;
            boolean isNeedCheckOnline = false;
            if(isOfflineData || (!isNeedRefresh && isAllowReadCache && mType>0 && !ApplicationUtils.isOnline(mContext))){
                mListModels= (ArrayList<T>) mContext.mTotalMng.getListData(mType);
                if(isOfflineData && mListModels==null){
                    mContext.mTotalMng.readTypeData(mContext,mType);
                    mListModels=(ArrayList<T>) mContext.mTotalMng.getListData(mType);
                }
            }
            if(!isOfflineData && (mListModels==null || isNeedRefresh)){
                isNeedCheckOnline=true;
                resultModel = getListModelFromServer();
                if(resultModel!=null && resultModel.isResultOk()){
                    if(isAllowReadCache && mType>0){
                        mContext.mTotalMng.setListCacheData(mType,resultModel.getListModels());
                        mListModels=(ArrayList<T>) mContext.mTotalMng.getListData(mType);
                    }
                    if(mListModels==null || mListModels.size()==0){
                        mListModels=resultModel.getListModels();
                    }
                }
                else{
                    if(isGetFromCacheWhenNoData){
                        mListModels= (ArrayList<T>) mContext.mTotalMng.getListData(mType);
                        if(mListModels==null){
                            mContext.mTotalMng.readTypeData(mContext,mType);
                            mListModels=(ArrayList<T>) mContext.mTotalMng.getListData(mType);
                        }
                    }
                }
            }
            ResultModel<T> finalResultModel = resultModel;
            boolean finalIsNeedCheckOnline = isNeedCheckOnline;
            ArrayList<T> finalMListModels = mListModels;
            mContext.runOnUiThread(() -> {
                showLoading(false);
                mRefreshLayout.setRefreshing(false);
                if(finalIsNeedCheckOnline && (finalResultModel ==null || !finalResultModel.isResultOk())){
                    if(isGetFromCacheWhenNoData){
                        setUpInfo(finalMListModels);
                        return;
                    }
                    String msg= finalResultModel !=null? finalResultModel.getMsg():null;
                    if(!TextUtils.isEmpty(msg)){
                        showResult(msg);
                        return;
                    }
                    int msgId=!ApplicationUtils.isOnline(mContext)?R.string.info_lose_internet:R.string.info_server_error;
                    showResult(msgId);
                    return;
                }
                if(isNeedRefresh){
                    onDoWhenRefreshList();
                }
                setUpInfo(finalMListModels);
            });
        });
    }

    public void onDoWhenRefreshList(){

    }

    @Override
    public void onLoadNextPlaceObject() {
        if(!ApplicationUtils.isOnline(mContext)){
            hideFooterView();
            mRefreshLayout.setRefreshing(false);
            mContext.showToast(R.string.info_lose_internet);
            mRecyclerView.setStartAddingPage(false);
            return;
        }
        YPYExecutorSupplier.getInstance().forBackgroundTasks().execute(() -> {
            final int originalSize = mListModels!=null?mListModels.size():0;
            ResultModel<T> resultModel = getListModelFromServer(originalSize, mNumberItemPerPage);
            final ArrayList<T> listLoadMores=(resultModel!=null && resultModel.isResultOk())?resultModel.getListModels():null;
            final int sizeLoaded=listLoadMores!=null?listLoadMores.size():0;
            final boolean isLoadOkNumberItem= sizeLoaded>=mNumberItemPerPage;
            mContext.runOnUiThread(() -> {
                hideFooterView();
                boolean isAllowLoadPage = isLoadOkNumberItem && mRecyclerView.getCurrentPage()<mMaxPage;
                YPYLog.e(TAG,"=========>isLoadOkNumberItem="+isLoadOkNumberItem+"==>isAllowLoadPage="+isAllowLoadPage);
                mRecyclerView.setAllowAddPage(isAllowLoadPage);
                if(isAllowLoadPage){
                    int page = mRecyclerView.getCurrentPage()+1;
                    mRecyclerView.setCurrentPage(page);
                }
                if(sizeLoaded>0){
                    mListModels.addAll(listLoadMores);
                    if(mAdapter!=null){
                        mAdapter.notifyItemRangeChanged(originalSize,sizeLoaded);
                    }
                    mContext.mTotalMng.saveListCacheModelInThread(mType);
                }
                mRecyclerView.setStartAddingPage(false);
            });
        });
    }

    private void setUpInfo(ArrayList<T> listObjects){
        if(isDestroy) return;
        this.mRecyclerView.setAdapter(null);
        if(!isOfflineData){
            if(this.mListModels!=null){
                this.mListModels.clear();
                this.mListModels=null;
            }
        }
        this.mListModels=listObjects;
        YPYLog.e("DCM","=====>setUpInfo="+(mListModels!=null?mListModels.size():0)+"==>type="+mType);
        int size = mListModels!=null?mListModels.size():0;
        if(size>0 || (isShowWhenNoData && mListModels!=null)){
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = createAdapter(listObjects);
            if(mAdapter!=null){
                mRecyclerView.setAdapter(mAdapter);
            }
            if(isAllowLoadMore){
                boolean b= checkAllowLoadMore(size);
                mRecyclerView.setAllowAddPage(b);
                if(b){
                    int page = mRecyclerView.getCurrentPage()+1;
                    mRecyclerView.setCurrentPage(page);
                }
            }

        }
        if(!isShowWhenNoData){
            updateInfo();
        }
    }

    public abstract YPYRecyclerViewAdapter createAdapter(ArrayList<T> listObjects);
    public abstract ResultModel<T> getListModelFromServer();
    public abstract void setUpUI();


    public ResultModel<T> getListModelFromServer(int offset,int limit){
        return null;
    }

    private boolean checkAllowLoadMore(int sizeLoaded){
        int page = (int) Math.floor((float)sizeLoaded/(float)mNumberItemPerPage);
        YPYLog.e(TAG,"=======>page="+page+"==>mMaxPage="+mMaxPage);
        if(page<mMaxPage && sizeLoaded>=mNumberItemPerPage){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        isDestroy=true;
        try{
            if(!isOfflineData){
                if(mRefreshLayout!=null){
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setEnabled(false);
                }

                if(mRecyclerView!=null){
                    mRecyclerView.setAdapter(null);
                }
                if(mListModels!=null){
                    mListModels.clear();
                    mListModels=null;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void updateInfo() {
        if (mTvNoResult != null) {
            boolean b = mListModels != null && mListModels.size() > 0;
            mTvNoResult.setVisibility(b ? View.GONE : View.VISIBLE);
            if(!b){
                mTvNoResult.setText(R.string.title_no_data);
            }
        }
    }

    public void showLoading(boolean b){
        if(mProgressBar!=null){
            mProgressBar.setVisibility(b?View.VISIBLE:View.GONE);
            if(b){
                mRecyclerView.setVisibility(View.GONE);
                mTvNoResult.setVisibility(View.GONE);
            }
        }
    }

    public void showResult(int resId){
        if(mContext!=null){
            showResult(mContext.getString(resId));
        }
    }

    public void showResult(String msg){
        if (mTvNoResult != null) {
            mTvNoResult.setText(msg);
            if(mAdapter==null){
                mTvNoResult.setVisibility(View.VISIBLE);
            }
            else{
                mTvNoResult.setVisibility(View.GONE);
                mContext.showToast(msg);
            }
        }
    }

    @Override
    public void onExtractData() {
        super.onExtractData();
        Bundle args = getArguments();
        if (args != null) {
            mType =args.getInt(KEY_TYPE_FRAGMENT,-1);
            isAllowLoadMore =args.getBoolean(KEY_ALLOW_MORE,false);
            isAllowReadCache =args.getBoolean(KEY_ALLOW_READ_CACHE,false);
            isTab =args.getBoolean(KEY_IS_TAB,false);
            isAllowRefresh =args.getBoolean(KEY_ALLOW_REFRESH,true);
            isShowWhenNoData =args.getBoolean(KEY_ALLOW_SHOW_NO_DATA,false);
            mNumberItemPerPage =args.getInt(KEY_NUMBER_ITEM_PER_PAGE, NUMBER_ITEM_PER_PAGE);
            mMaxPage =args.getInt(KEY_MAX_PAGE,MAX_PAGE);
            isOfflineData =args.getBoolean(KEY_OFFLINE_DATA,false);
            isGetFromCacheWhenNoData =args.getBoolean(KEY_READ_CACHE_WHEN_NO_DATA,false);
        }
    }

    @Override
    public void notifyData() {
        super.notifyData();
        if(mAdapter!=null){
            mAdapter.notifyDataSetChanged();
            if (!isShowWhenNoData) {
                updateInfo();
            }
        }
    }


    @Override
    public void notifyData(int pos) {
        super.notifyData(pos);
        if(mAdapter!=null){
            mAdapter.notifyItemChanged(pos);
        }
    }

    public void notifyFavorite(long trackId, boolean isFav) {
        if (mContext != null && mListModels != null && mListModels.size() > 0) {
            int index = mContext.mTotalMng.updateFavoriteForId((ArrayList<? extends AbstractModel>) mListModels, trackId, isFav);
            if (index >= 0) {
                mContext.runOnUiThread(() -> notifyData(index));
            }
        }
    }


    public void setUpUIRecyclerView(int mTypeUI){
        try{
            if(mRecyclerView!=null){
                int dialogMargin = getResources().getDimensionPixelOffset(R.dimen.dialog_margin);
                int smallMargin = getResources().getDimensionPixelOffset(R.dimen.small_margin);
                mSizeH = (int) ((mContext.getScreenWidth()-3f*dialogMargin)/2f);

                if(mTypeUI==UI_FLAT_LIST || mTypeUI==UI_CARD_LIST){
                    Drawable mDrawableVer=null;
                    try{
                        mDrawableVer=mContext.getSupportDrawable(R.drawable.alpha_divider_small_verti);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    mContext.setUpRecyclerViewAsListView(mRecyclerView,mTypeUI==UI_CARD_LIST?mDrawableVer:null);
                }
                else if(mTypeUI==UI_CARD_GRID || mTypeUI==UI_FLAT_GRID){
                    Drawable mDrawableVer=null;
                    try{
                        if(mTypeUI==UI_FLAT_GRID){
                            mDrawableVer=mContext.getSupportDrawable(R.drawable.alpha_divider_large_verti);
                        }
                        else{
                            mDrawableVer=mContext.getSupportDrawable(R.drawable.alpha_divider_small_verti);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    mContext.setUpRecyclerViewAsGridView(mRecyclerView,2,mDrawableVer,null);
                    GridLayoutManager layoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
                    layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            RecyclerView.Adapter mAdapter = mRecyclerView.getAdapter();
                            switch (mAdapter.getItemViewType(position)) {
                                case YPYRecyclerViewAdapter.TYPE_HEADER_VIEW:
                                    return 2;
                                default:
                                    return 1;
                            }
                        }
                    });
                }
                else if(mTypeUI==UI_MAGIC_GRID){
                    Drawable mDrawableVer=null;
                    try{
                        mDrawableVer=mContext.getSupportDrawable(R.drawable.alpha_divider_small_verti);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    mContext.setUpRecyclerViewAsStaggered(mRecyclerView,2,mDrawableVer,null);
                }
                if(mTypeUI!=UI_FLAT_LIST){
                    mRecyclerView.setPadding(smallMargin,dialogMargin,smallMargin,0);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public boolean isCheckBack() {
        if(!isOfflineData && mFooterView!=null && mFooterView.getVisibility()==View.VISIBLE){
            return true;
        }
        if(mProgressBar!=null && mProgressBar.getVisibility()==View.VISIBLE){
            return true;
        }
        return super.isCheckBack();
    }
}
