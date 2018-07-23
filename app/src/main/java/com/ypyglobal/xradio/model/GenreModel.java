package com.ypyglobal.xradio.model;

import android.text.TextUtils;

import com.ypyglobal.xradio.dataMng.XRadioNetUtils;
import com.ypyglobal.xradio.ypylibs.model.AbstractModel;


public class GenreModel extends AbstractModel {

    public GenreModel(long id, String name, String image) {
        super(id, name, image);
    }

    @Override
    public String getArtWork(String urlHost) {
        if(!TextUtils.isEmpty(image) && !image.startsWith("http") && !TextUtils.isEmpty(urlHost)){
            image=urlHost+ XRadioNetUtils.FOLDER_GENRES+image;
        }
        return super.getImage();
    }
}
