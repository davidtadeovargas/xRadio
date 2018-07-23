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

package com.ypyglobal.xradio.model;

import com.google.gson.annotations.SerializedName;
import com.ypyglobal.xradio.dataMng.XRadioNetUtils;


public class UIConfigModel {

    @SerializedName("is_full_bg")
    private int isFullBg;

    @SerializedName("ui_top_chart")
    private int uiTopChart;

    @SerializedName("ui_genre")
    private int uiGenre;

    @SerializedName("ui_favorite")
    private int uiFavorite;

    @SerializedName("ui_themes")
    private int uiThemes;

    @SerializedName("ui_detail_genre")
    private int uiDetailGenre;

    @SerializedName("ui_player")
    private int uiPlayer;

    @SerializedName("ui_search")
    private int uiSearch;

    @SerializedName("app_type")
    private int appType;

    public int getIsFullBg() {
        return isFullBg;
    }

    public void setIsFullBg(int isFullBg) {
        this.isFullBg = isFullBg;
    }

    public int getUiTopChart() {
        return uiTopChart;
    }

    public void setUiTopChart(int uiTopChart) {
        this.uiTopChart = uiTopChart;
    }

    public int getUiGenre() {
        return uiGenre;
    }

    public void setUiGenre(int uiGenre) {
        this.uiGenre = uiGenre;
    }

    public int getUiFavorite() {
        return uiFavorite;
    }

    public void setUiFavorite(int uiFavorite) {
        this.uiFavorite = uiFavorite;
    }

    public int getUiThemes() {
        return uiThemes;
    }

    public void setUiThemes(int uiThemes) {
        this.uiThemes = uiThemes;
    }

    public int getUiDetailGenre() {
        return uiDetailGenre;
    }

    public void setUiDetailGenre(int uiDetailGenre) {
        this.uiDetailGenre = uiDetailGenre;
    }

    public int getUiPlayer() {
        return uiPlayer;
    }

    public void setUiPlayer(int uiPlayer) {
        this.uiPlayer = uiPlayer;
    }

    public int getUiSearch() {
        return uiSearch;
    }

    public void setUiSearch(int uiSearch) {
        this.uiSearch = uiSearch;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public boolean isMultiApp(){
        return appType== XRadioNetUtils.TYPE_APP_MULTI;
    }
}
