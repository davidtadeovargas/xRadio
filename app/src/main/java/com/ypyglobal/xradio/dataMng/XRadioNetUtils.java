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

package com.ypyglobal.xradio.dataMng;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.ypyglobal.xradio.constants.IXRadioConstants;
import com.ypyglobal.xradio.model.GenreModel;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.model.ThemeModel;
import com.ypyglobal.xradio.model.UIConfigModel;
import com.ypyglobal.xradio.ypylibs.model.ResultModel;
import com.ypyglobal.xradio.ypylibs.utils.DownloadUtils;
import com.ypyglobal.xradio.ypylibs.utils.StringUtils;
import com.ypyglobal.xradio.ypylibs.utils.YPYLog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class XRadioNetUtils implements IXRadioConstants {

    public static final String FORMAT_API_END_POINT= "/api/api.php?method=%1$s";
    public static final String METHOD_GET_GENRES= "getGenres";
    public static final String METHOD_GET_RADIOS= "getRadios";
    public static final String METHOD_GET_THEMES= "getThemes";
    public static final String METHOD_GET_REMOTE_CONFIGS= "getRemoteConfigs";

    public static final String METHOD_PRIVACY_POLICY= "/privacy_policy.php";
    public static final String METHOD_TERM_OF_USE= "/term_of_use.php";

    public static final String KEY_API= "&api_key=";
    public static final String KEY_QUERY= "&q=";
    public static final String KEY_GENRE_ID= "&genre_id=";
    public static final String KEY_APP_TYPE= "&app_type=";
    public static final String KEY_OFFSET= "&offset=";
    public static final String KEY_LIMIT= "&limit=";
    public static final String KEY_IS_FEATURE= "&is_feature=1";

    public static final String FOLDER_GENRES= "/uploads/genres/";
    public static final String FOLDER_RADIOS= "/uploads/radios/";
    public static final String FOLDER_THEMES= "/uploads/themes/";


    public static ResultModel<GenreModel> getListGenreModel(String urlHost,String apiKey){
        try{
            StringBuilder mStringBuilder =new StringBuilder(urlHost);
            mStringBuilder.append(String.format(FORMAT_API_END_POINT,METHOD_GET_GENRES));
            mStringBuilder.append(KEY_API+apiKey);
            String url=mStringBuilder.toString();
            YPYLog.e(TAG,"==========>getListGenreModel="+url);
            Reader mInputStream = DownloadUtils.downloadReader(url);
            Type mTypeToken = new TypeToken<ResultModel<GenreModel>>(){}.getType();
            ResultModel<GenreModel> mDatas = JsonParsingUtils.getResultModel(mInputStream,mTypeToken);
            return mDatas;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static ResultModel<RadioModel> getListTopChartRadio(String urlHost, String apiKey,int offset,int limit){
        return getListRadioModel(urlHost,apiKey,-1,null,offset,limit,true);
    }

    public static ResultModel<RadioModel> getListRadioModel(String urlHost, String apiKey,long genreId,int offset,int limit){
        return getListRadioModel(urlHost,apiKey,genreId,null,offset,limit,false);
    }

    public static ResultModel<RadioModel> searchRadioModel(String urlHost, String apiKey,String query,int offset,int limit){
        return getListRadioModel(urlHost,apiKey,-1,query,offset,limit,false);
    }

    public static ResultModel<RadioModel> getListRadioModel(String urlHost, String apiKey,int offset,int limit){
        return getListRadioModel(urlHost,apiKey,-1,null,offset,limit,false);
    }


    public static ResultModel<RadioModel> getListRadioModel(String urlHost, String apiKey,long genreId,String query,int offset,int limit,boolean isFeature){
        try{
            StringBuilder mStringBuilder =new StringBuilder(urlHost);
            mStringBuilder.append(String.format(FORMAT_API_END_POINT,METHOD_GET_RADIOS));
            mStringBuilder.append(KEY_API+apiKey);
            if(offset>=0){
                mStringBuilder.append(KEY_OFFSET+offset);
            }
            if(limit>0){
                mStringBuilder.append(KEY_LIMIT+limit);
            }
            if(genreId>0){
                mStringBuilder.append(KEY_GENRE_ID+genreId);
            }
            if(isFeature){
                mStringBuilder.append(KEY_IS_FEATURE);
            }
            if(!TextUtils.isEmpty(query)){
                mStringBuilder.append(KEY_QUERY+ StringUtils.urlEncodeString(query));
            }
            String url=mStringBuilder.toString();
            YPYLog.e(TAG,"==========>getListRadioModel="+url);
            Type mTypeToken = new TypeToken<ResultModel<RadioModel>>(){}.getType();
            return getListDataFromServer(url,mTypeToken);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static ResultModel<UIConfigModel> getUIConfigModel(String urlHost,String apiKey){
        try{
            StringBuilder mStringBuilder =new StringBuilder(urlHost);
            mStringBuilder.append(String.format(FORMAT_API_END_POINT,METHOD_GET_REMOTE_CONFIGS));
            mStringBuilder.append(KEY_API+apiKey);
            String url=mStringBuilder.toString();
            YPYLog.e(TAG,"==========>getUIConfigModel="+url);
            Type mTypeToken = new TypeToken<ResultModel<UIConfigModel>>(){}.getType();
            return getListDataFromServer(url,mTypeToken);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ResultModel<ThemeModel> getDefaultThemes(String urlHost,String apiKey){
        return getListThemes(urlHost,apiKey,-1,-1,TYPE_APP_SINGLE);
    }

    public static ResultModel<ThemeModel> getListThemes(String urlHost,String apiKey,int offset,int limit,int appType){
        try{
            StringBuilder mStringBuilder =new StringBuilder(urlHost);
            mStringBuilder.append(String.format(FORMAT_API_END_POINT,METHOD_GET_THEMES));
            mStringBuilder.append(KEY_API+apiKey);
            if(offset>=0){
                mStringBuilder.append(KEY_OFFSET+offset);
            }
            if(limit>0){
                mStringBuilder.append(KEY_LIMIT+limit);
            }
            if(appType>0){
                mStringBuilder.append(KEY_APP_TYPE+appType);
            }
            String url=mStringBuilder.toString();
            YPYLog.e(TAG,"==========>getListThemes="+url);
            Type mTypeToken = new TypeToken<ResultModel<ThemeModel>>(){}.getType();
            return getListDataFromServer(url,mTypeToken);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static <T> ResultModel<T> getListDataFromServer(String url,Type mTypeToken){
        try {
            Reader mInputStream = DownloadUtils.downloadReader(url);
            return JsonParsingUtils.getResultModel(mInputStream,mTypeToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> ResultModel<T> getListDataFromAssets(Context mContext,String uri,Type mTypeToken){
        try {
            InputStream data =mContext.getAssets().open(uri);
            return JsonParsingUtils.getResultModel(new InputStreamReader(data),mTypeToken);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageOfSong(String title,String artist,String apiKey){
        try{
            if(!TextUtils.isEmpty(title)){
                String url;
                String urlImg;
                if(!TextUtils.isEmpty(artist)){
                    url =String.format(FORMAT_LAST_FM, StringUtils.urlEncodeString(title+" "+artist),apiKey);
                    urlImg = JsonParsingUtils.parsingImageSong(DownloadUtils.downloadString(url));
                    if(!TextUtils.isEmpty(urlImg)){
                        return urlImg;
                    }
                }
                url =String.format(FORMAT_LAST_FM, StringUtils.urlEncodeString(artist),apiKey);
                urlImg = JsonParsingUtils.parsingImageSong(DownloadUtils.downloadString(url));
                if(!TextUtils.isEmpty(urlImg)){
                    return urlImg;
                }
                else{
                    url =String.format(FORMAT_LAST_FM, StringUtils.urlEncodeString(title),apiKey);
                    urlImg = JsonParsingUtils.parsingImageSong(DownloadUtils.downloadString(url));
                    return urlImg;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
