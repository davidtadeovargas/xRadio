// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio;

import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XRadioShowUrlActivity_ViewBinding extends XRadioFragmentActivity_ViewBinding {
  private XRadioShowUrlActivity target;

  @UiThread
  public XRadioShowUrlActivity_ViewBinding(XRadioShowUrlActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XRadioShowUrlActivity_ViewBinding(XRadioShowUrlActivity target, View source) {
    super(target, source);

    this.target = target;

    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.progressBar1, "field 'mProgressBar'", ProgressBar.class);
    target.mWebViewShowPage = Utils.findRequiredViewAsType(source, R.id.webview, "field 'mWebViewShowPage'", WebView.class);
  }

  @Override
  public void unbind() {
    XRadioShowUrlActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mProgressBar = null;
    target.mWebViewShowPage = null;

    super.unbind();
  }
}
