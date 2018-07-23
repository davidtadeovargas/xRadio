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

package com.ypyglobal.xradio.stream.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.ypyglobal.xradio.dataMng.TotalDataManager;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.setting.XRadioSettingManager;
import com.ypyglobal.xradio.stream.constant.IYPYStreamConstants;
import com.ypyglobal.xradio.stream.manager.YPYStreamManager;

import java.util.ArrayList;


public class YPYIntentReceiver extends BroadcastReceiver implements IYPYStreamConstants {

	public static final String TAG = YPYIntentReceiver.class.getSimpleName();
	private ArrayList<RadioModel> mListTrack;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent == null) {
			return;
		}
		String action = intent.getAction();
		if (TextUtils.isEmpty(action)) {
			return;
		}
		mListTrack = YPYStreamManager.getInstance().getListMusicRadio();
		String packageName = context.getPackageName();
		if (action.equals(android.media.AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
			startService(context, ACTION_PAUSE);
		}
		else if (action.equals(packageName + ACTION_NEXT)) {
			startService(context, ACTION_NEXT);
		}
		else if (action.equals(packageName + ACTION_TOGGLE_PLAYBACK)) {
			startService(context, ACTION_TOGGLE_PLAYBACK);
		}
		else if (action.equals(packageName + ACTION_PREVIOUS)) {
			startService(context, ACTION_PREVIOUS);
		}
		else if (action.equals(packageName + ACTION_STOP)) {
			startService(context, ACTION_STOP);
			if(!XRadioSettingManager.getOnline(context)){
				try{
					YPYStreamManager.getInstance().getInstance().onDestroy();
					TotalDataManager.getInstance().onDestroy();
				}
				catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		else if (action.equals(Intent.ACTION_MEDIA_BUTTON)) {
			if(mListTrack==null || mListTrack.size()==0){
				YPYStreamManager.getInstance().onDestroy();
				startService(context, ACTION_STOP);
				return;
			}
			KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
			if (keyEvent.getAction() != KeyEvent.ACTION_DOWN){
				return;
			}
			switch (keyEvent.getKeyCode()) {
				case KeyEvent.KEYCODE_HEADSETHOOK:
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
					if(XRadioSettingManager.getOnline(context)){
						startService(context, ACTION_TOGGLE_PLAYBACK);
					}
					else{
						startService(context, ACTION_STOP);
					}
					break;
				case KeyEvent.KEYCODE_MEDIA_PLAY:
					startService(context, ACTION_PLAY);
					break;
				case KeyEvent.KEYCODE_MEDIA_PAUSE:
					if(XRadioSettingManager.getOnline(context)){
						startService(context, ACTION_PAUSE);
					}
					else{
						startService(context, ACTION_STOP);
					}
					break;
				case KeyEvent.KEYCODE_MEDIA_STOP:
					startService(context, ACTION_STOP);
					break;
				case KeyEvent.KEYCODE_MEDIA_NEXT:
					startService(context, ACTION_NEXT);
					break;
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					startService(context,ACTION_PREVIOUS);
					break;
			}
		}
	}

	private void startService(Context context, String action){
		Intent mIntent1= new Intent(context,YPYStreamService.class);
		mIntent1.setAction(context.getPackageName() +action);
		context.startService(mIntent1);
	}

}
