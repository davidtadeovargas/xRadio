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

import com.ypyglobal.xradio.adapter.RadioAdapter;
import com.ypyglobal.xradio.model.RadioModel;
import com.ypyglobal.xradio.ypylibs.adapter.YPYRecyclerViewAdapter;
import com.ypyglobal.xradio.ypylibs.model.ResultModel;

import java.util.ArrayList;


public class FragmentFavorite extends XRadioListFragment<RadioModel> {

    private int mTypeUI;

    @Override
    public YPYRecyclerViewAdapter createAdapter(ArrayList<RadioModel> listObjects) {
        RadioAdapter mRadioAdapter = new RadioAdapter(mContext,listObjects,mUrlHost,mSizeH,mTypeUI);
        mRadioAdapter.setListener(mObject ->mContext.startPlayingList(mObject,listObjects));
        mRadioAdapter.setOnRadioListener((model, isFavorite) -> mContext.updateFavorite(model,TYPE_TAB_FAVORITE,isFavorite));
        return mRadioAdapter;
    }

    @Override
    public ResultModel<RadioModel> getListModelFromServer() {
        return null;
    }

    @Override
    public void setUpUI() {
        mTypeUI=mUIConfigureModel!=null?mUIConfigureModel.getUiFavorite():UI_FLAT_LIST;
        setUpUIRecyclerView(mTypeUI);

    }

    @Override
    public void notifyData() {
        super.notifyData();
        if(mAdapter==null){
            onRefreshData(false);
        }
    }
}
