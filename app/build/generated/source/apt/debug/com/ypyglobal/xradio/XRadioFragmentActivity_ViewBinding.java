// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XRadioFragmentActivity_ViewBinding implements Unbinder {
  private XRadioFragmentActivity target;

  @UiThread
  public XRadioFragmentActivity_ViewBinding(XRadioFragmentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XRadioFragmentActivity_ViewBinding(XRadioFragmentActivity target, View source) {
    this.target = target;

    target.mLayoutBg = Utils.findRequiredViewAsType(source, R.id.layout_bg, "field 'mLayoutBg'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XRadioFragmentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mLayoutBg = null;
  }
}
