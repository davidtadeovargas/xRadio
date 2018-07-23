// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.like.LikeButton;
import com.ypyglobal.xradio.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RadioAdapter$RadioHolder_ViewBinding implements Unbinder {
  private RadioAdapter.RadioHolder target;

  @UiThread
  public RadioAdapter$RadioHolder_ViewBinding(RadioAdapter.RadioHolder target, View source) {
    this.target = target;

    target.mTvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'mTvName'", TextView.class);
    target.mTvDes = Utils.findRequiredViewAsType(source, R.id.tv_des, "field 'mTvDes'", TextView.class);
    target.mImgRadio = Utils.findRequiredViewAsType(source, R.id.img_radio, "field 'mImgRadio'", ImageView.class);
    target.mLayoutRoot = Utils.findRequiredView(source, R.id.layout_root, "field 'mLayoutRoot'");
    target.mBtnFavorite = Utils.findRequiredViewAsType(source, R.id.btn_favourite, "field 'mBtnFavorite'", LikeButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RadioAdapter.RadioHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTvName = null;
    target.mTvDes = null;
    target.mImgRadio = null;
    target.mLayoutRoot = null;
    target.mBtnFavorite = null;
  }
}
