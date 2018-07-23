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

package com.ypyglobal.xradio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.ypylibs.adapter.YPYRecyclerViewAdapter;
import com.ypyglobal.xradio.ypylibs.imageloader.GlideImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.ypyglobal.xradio.constants.IXRadioConstants.UI_CARD_GRID;
import static com.ypyglobal.xradio.constants.IXRadioConstants.UI_CARD_LIST;
import static com.ypyglobal.xradio.constants.IXRadioConstants.UI_FLAT_GRID;
import static com.ypyglobal.xradio.constants.IXRadioConstants.UI_MAGIC_GRID;

/**
 * @author:YPY Global
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: bl911vn@gmail.com
 * @Website: http://ypyglobal.com
 * Created by dotrungbao on 4/20/18.
 */
public class RadioAdapter extends YPYRecyclerViewAdapter<RadioModel> {

    private final int mTypeUI;
    private final int mSizeH;
    private final String mUrlHost;
    private int mResId;
    private OnRadioListener onRadioListener;

    public RadioAdapter(Context mContext, ArrayList<RadioModel> listObjects, String mUrlHost, int sizeH, int typeUI) {
        super(mContext, listObjects);
        this.mSizeH=sizeH;
        this.mTypeUI=typeUI;
        this.mResId =R.layout.item_flat_list_radio;
        if(mTypeUI==UI_FLAT_GRID){
            this.mResId =R.layout.item_flat_grid_radio;
        }
        else if(mTypeUI== UI_CARD_GRID || mTypeUI==UI_MAGIC_GRID){
            this.mResId =R.layout.item_card_grid_radio;
        }
        else if(mTypeUI==UI_CARD_LIST){
            this.mResId =R.layout.item_card_list_radio;
        }
        this.mUrlHost=mUrlHost;

    }

    @Override
    public void onBindNormalViewHolder(RecyclerView.ViewHolder holder, int position) {
        RadioHolder mHolder = (RadioHolder) holder;
        final RadioModel radioModel = mListObjects.get(position);
        mHolder.mTvName.setText(radioModel.getName());

        String tag=radioModel.getTags();
        if(TextUtils.isEmpty(tag) && !TextUtils.isEmpty(radioModel.getBitRate())){
            tag=String.format(mContext.getString(R.string.format_bitrate),radioModel.getBitRate());
        }
        mHolder.mTvDes.setText(tag);
        mHolder.mBtnFavorite.setLiked(radioModel.isFavorite());

        if(!TextUtils.isEmpty(radioModel.getImage())){
            GlideImageLoader.displayImage(mContext,mHolder.mImgRadio,radioModel.getArtWork(mUrlHost), R.drawable.ic_rect_img_default);
        }
        else{
            mHolder.mImgRadio.setImageResource(R.drawable.ic_rect_img_default);
        }
        mHolder.mBtnFavorite.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                if(onRadioListener!=null){
                    onRadioListener.onFavorite(radioModel,true);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                if(onRadioListener!=null){
                    onRadioListener.onFavorite(radioModel,false);
                }
            }
        });

        if(mTypeUI==UI_FLAT_GRID || mTypeUI==UI_CARD_GRID){
            mHolder.mImgRadio.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onViewDetail(radioModel);
                }
            });
        }
        else{
            mHolder.mLayoutRoot.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onViewDetail(radioModel);
                }
            });
        }

    }
    public void setOnRadioListener(OnRadioListener onRadioListener) {
        this.onRadioListener = onRadioListener;
    }

    public interface OnRadioListener {
        public void onFavorite(RadioModel model,boolean isFavorite);
    }

    @Override
    public RecyclerView.ViewHolder onCreateNormalViewHolder(ViewGroup v, int viewType) {
        View mView = mInflater.inflate(mResId, v, false);
        RadioHolder mHolder = new RadioHolder(mView);
        return mHolder;
    }

    public class RadioHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        public TextView mTvName;

        @BindView(R.id.tv_des)
        public TextView mTvDes;

        @BindView(R.id.img_radio)
        public ImageView mImgRadio;

        @BindView(R.id.layout_root)
        public View mLayoutRoot;

        @BindView(R.id.btn_favourite)
        public LikeButton mBtnFavorite;


        public RadioHolder(View convertView) {
            super(convertView);
            ButterKnife.bind(this,convertView);
            mTvName.setSelected(true);
            if(mSizeH>0 && (mTypeUI==UI_FLAT_GRID || mTypeUI==UI_CARD_GRID)){
                FrameLayout.LayoutParams mLayoutParams = (FrameLayout.LayoutParams) mImgRadio.getLayoutParams();
                mLayoutParams.height=mSizeH;
                mImgRadio.setLayoutParams(mLayoutParams);
            }

        }
    }
}
