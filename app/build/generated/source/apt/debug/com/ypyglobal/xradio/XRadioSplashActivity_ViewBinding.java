// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.wang.avi.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XRadioSplashActivity_ViewBinding implements Unbinder {
  private XRadioSplashActivity target;

  @UiThread
  public XRadioSplashActivity_ViewBinding(XRadioSplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XRadioSplashActivity_ViewBinding(XRadioSplashActivity target, View source) {
    this.target = target;

    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.progressBar1, "field 'mProgressBar'", AVLoadingIndicatorView.class);
    target.mLayoutBg = Utils.findRequiredViewAsType(source, R.id.layout_bg, "field 'mLayoutBg'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XRadioSplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mProgressBar = null;
    target.mLayoutBg = null;
  }
}
