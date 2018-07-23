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

package com.ypyglobal.xradio.ypylibs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class YPYFragment extends Fragment implements IYPYFragmentConstants {

	public View mRootView;
	private boolean isExtractData;

	public String mNameFragment;
	public int mIdFragment;
	private boolean isAllowFindViewContinuous;
	private boolean isCreated;
	private boolean isFirstInTab;

	public ArrayList<Fragment> mListFragments;
	private boolean isLoadingData;
	private Unbinder mBinder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = onInflateLayout(inflater,container,savedInstanceState);
		if(mRootView!=null){
			mBinder= ButterKnife.bind(this,mRootView);
		}
		return mRootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (!isExtractData) {
			isExtractData = true;
			onExtractData();
			findView();
		}
		else{
			if(isAllowFindViewContinuous){
				findView();
			}
		}
		isCreated=true;
	}

	public void createArrayFragment(){
		mListFragments= new ArrayList<>();
	}

	@Override
	public void onStart() {
		super.onStart();
		if(isAllowFindViewContinuous && isCreated){
			findView();
		}

	}

	public abstract View onInflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	public abstract void findView();
	
	public void onExtractData(){
		Bundle args = getArguments();
		if (args != null) {
			mNameFragment = args.getString(KEY_NAME_FRAGMENT);
			mIdFragment = args.getInt(KEY_ID_FRAGMENT);
		}
	}
	
	public void backToHome(FragmentActivity mContext) {
		FragmentTransaction mFragmentTransaction = null;
		FragmentManager mFragmentManager = mContext.getSupportFragmentManager();
		mFragmentTransaction = mFragmentManager.beginTransaction();
		mFragmentTransaction.remove(this);

		Fragment mFragmentHome = getFragmentHome(mContext);
		if(mFragmentHome!=null){
			mFragmentTransaction.show(mFragmentHome);
		}
		mFragmentTransaction.commit();
	}

	public void setAllowFindViewContinuous(boolean isAllowFindViewContinuous) {
		this.isAllowFindViewContinuous = isAllowFindViewContinuous;
	}

	public Fragment getFragmentHome(FragmentActivity mContext){
		Fragment mFragmentHome=null;
		if(mIdFragment>0){
			mFragmentHome = mContext.getSupportFragmentManager().findFragmentById(mIdFragment);
		}
		else{
			if(!TextUtils.isEmpty(mNameFragment)){
				mFragmentHome = mContext.getSupportFragmentManager().findFragmentByTag(mNameFragment);
			}
		}
		return mFragmentHome;
	}

	public void notifyData(){

	}

	public void notifyData(int pos){

	}

	public void startLoadData(){

	}

	public void onNetworkChange(boolean isNetworkOn){

	}

	public boolean isLoadingData() {
		return isLoadingData;
	}

	public void setLoadingData(boolean loadingData) {
		isLoadingData = loadingData;
	}
	public boolean isFirstInTab() {
		return isFirstInTab;
	}

	public void setFirstInTab(boolean firstInTab) {
		isFirstInTab = firstInTab;
	}


	public boolean isCheckBack(){
		return false;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mBinder!=null){
			mBinder.unbind();
		}
	}
}
