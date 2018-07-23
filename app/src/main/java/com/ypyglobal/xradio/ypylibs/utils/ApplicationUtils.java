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

package com.ypyglobal.xradio.ypylibs.utils;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


public class ApplicationUtils {

    /**
     * check connection internet
     * @param mContext
     * @return true if connecting
     */
    public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static boolean hasSDcard() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    public static void hiddenVirtualKeyboard(Context mContext, View myEditText) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GradientDrawable.Orientation getOrientation(int gradOrientation) {
        GradientDrawable.Orientation orientation;
        if (gradOrientation == 0) {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT;
        }
        else if (gradOrientation == 180) {
            orientation = GradientDrawable.Orientation.RIGHT_LEFT;
        }
        else if (gradOrientation == 270) {
            orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        }
        else if (gradOrientation == 90) {
            orientation = GradientDrawable.Orientation.BOTTOM_TOP;
        }
        else if (gradOrientation == 315) {
            orientation = GradientDrawable.Orientation.TL_BR;
        }
        else if (gradOrientation == 225) {
            orientation = GradientDrawable.Orientation.TR_BL;
        }
        else if (gradOrientation == 45) {
            orientation = GradientDrawable.Orientation.BL_TR;
        }
        else if (gradOrientation == 135) {
            orientation = GradientDrawable.Orientation.BR_TL;
        }
        else {
            orientation = GradientDrawable.Orientation.TL_BR;
        }
        return orientation;
    }
}
