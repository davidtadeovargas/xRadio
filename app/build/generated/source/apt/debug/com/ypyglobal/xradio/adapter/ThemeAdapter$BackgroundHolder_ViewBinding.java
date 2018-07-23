// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.ypylibs.view.MaterialIconView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ThemeAdapter$BackgroundHolder_ViewBinding implements Unbinder {
  private ThemeAdapter.BackgroundHolder target;

  @UiThread
  public ThemeAdapter$BackgroundHolder_ViewBinding(ThemeAdapter.BackgroundHolder target,
      View source) {
    this.target = target;

    target.mImgCheck = Utils.findRequiredViewAsType(source, R.id.icon_check, "field 'mImgCheck'", MaterialIconView.class);
    target.mImgBg = Utils.findRequiredViewAsType(source, R.id.img_theme, "field 'mImgBg'", ImageView.class);
    target.mTvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'mTvName'", TextView.class);
    target.mLayoutRoot = Utils.findRequiredViewAsType(source, R.id.layout_root, "field 'mLayoutRoot'", RelativeLayout.class);
    target.mCardView = Utils.findOptionalViewAsType(source, R.id.card_view, "field 'mCardView'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ThemeAdapter.BackgroundHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mImgCheck = null;
    target.mImgBg = null;
    target.mTvName = null;
    target.mLayoutRoot = null;
    target.mCardView = null;
  }
}
