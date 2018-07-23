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

package com.ypyglobal.xradio.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.ypyglobal.xradio.adapter.RadioAdapter;
import com.ypyglobal.xradio.dataMng.XRadioNetUtils;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.ypylibs.adapter.YPYRecyclerViewAdapter;
import com.ypyglobal.xradio.ypylibs.model.ResultModel;
import com.ypyglobal.xradio.ypylibs.utils.ApplicationUtils;

import java.util.ArrayList;

public class FragmentDetailList extends XRadioListFragment<RadioModel> {

    private int mTypeUI;
    private long mGenreId;
    private String mKeyword;

    @Override
    public YPYRecyclerViewAdapter createAdapter(ArrayList<RadioModel> listObjects) {
        RadioAdapter mRadioAdapter = new RadioAdapter(mContext,listObjects,mUrlHost,mSizeH,mTypeUI);
        mRadioAdapter.setListener(mObject ->mContext.startPlayingList(mObject,listObjects));
        mRadioAdapter.setOnRadioListener((model, isFavorite) -> mContext.updateFavorite(model,TYPE_TAB_FAVORITE,isFavorite));
        return mRadioAdapter;
    }

    @Override
    public ResultModel<RadioModel> getListModelFromServer() {
        ResultModel<RadioModel> mResultModel=null;
        boolean isOnlineApp=mConfigureMode!=null?mConfigureMode.isOnlineApp():false;
        if(ApplicationUtils.isOnline(mContext) && isOnlineApp){
            if(mType==TYPE_DETAIL_GENRE){
                mResultModel = XRadioNetUtils.getListRadioModel(mUrlHost,mApiKey,mGenreId,0,NUMBER_ITEM_PER_PAGE);
            }
            else if(mType==TYPE_SEARCH){
                mResultModel = XRadioNetUtils.searchRadioModel(mUrlHost,mApiKey,mKeyword,0,NUMBER_ITEM_PER_PAGE);
            }
        }
        else{
            if(!isOnlineApp && mType==TYPE_SEARCH){
                mResultModel = mContext.mTotalMng.searchRadioModel(mKeyword);
            }
        }
        if(mResultModel!=null && mResultModel.isResultOk()){
            mContext.mTotalMng.updateFavoriteForList(mResultModel.getListModels(),TYPE_TAB_FAVORITE);
        }
        return mResultModel;
    }

    @Override
    public ResultModel<RadioModel> getListModelFromServer(int offset, int limit) {
        ResultModel<RadioModel> mResultModel=null;
        if(mType==TYPE_DETAIL_GENRE){
            mResultModel = XRadioNetUtils.getListRadioModel(mUrlHost,mApiKey,mGenreId,offset,limit);
        }
        else if(mType==TYPE_SEARCH){
            mResultModel = XRadioNetUtils.searchRadioModel(mUrlHost,mApiKey,mKeyword,offset,limit);
        }
        if(mResultModel!=null && mResultModel.isResultOk()){
            mContext.mTotalMng.updateFavoriteForList(mResultModel.getListModels(),TYPE_TAB_FAVORITE);
        }
        return mResultModel;
    }

    @Override
    public void setUpUI() {
        if(mType==TYPE_DETAIL_GENRE){
            mTypeUI=mUIConfigureModel!=null?mUIConfigureModel.getUiDetailGenre():UI_FLAT_LIST;
        }
        else{
            mTypeUI=mUIConfigureModel!=null?mUIConfigureModel.getUiSearch():UI_FLAT_LIST;
        }
        setUpUIRecyclerView(mTypeUI);
    }

    @Override
    public void onExtractData() {
        super.onExtractData();
        Bundle args = getArguments();
        if (args != null) {
            mGenreId = args.getLong(KEY_GENRE_ID,-1);
            if(mType==TYPE_SEARCH){
                mKeyword = args.getString(KEY_SEARCH);
            }
        }
    }

    public void startSearch(String keyword){
        try{
            if (!TextUtils.isEmpty(keyword) && mContext != null) {
                this.mKeyword = keyword;
                setLoadingData(false);
                startLoadData();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
