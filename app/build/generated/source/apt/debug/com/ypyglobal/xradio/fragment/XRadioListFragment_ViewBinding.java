// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.ypyglobal.xradio.R;
import com.ypyglobal.xradio.ypylibs.view.CircularProgressBar;
import com.ypyglobal.xradio.ypylibs.view.YPYRecyclerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XRadioListFragment_ViewBinding implements Unbinder {
  private XRadioListFragment target;

  @UiThread
  public XRadioListFragment_ViewBinding(XRadioListFragment target, View source) {
    this.target = target;

    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.list_datas, "field 'mRecyclerView'", YPYRecyclerView.class);
    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.progressBar1, "field 'mProgressBar'", CircularProgressBar.class);
    target.mTvNoResult = Utils.findRequiredViewAsType(source, R.id.tv_no_result, "field 'mTvNoResult'", TextView.class);
    target.mRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swiperefresh, "field 'mRefreshLayout'", SwipeRefreshLayout.class);
    target.mFooterView = Utils.findRequiredView(source, R.id.loading_footer, "field 'mFooterView'");
  }

  @Override
  @CallSuper
  public void unbind() {
    XRadioListFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRecyclerView = null;
    target.mProgressBar = null;
    target.mTvNoResult = null;
    target.mRefreshLayout = null;
    target.mFooterView = null;
  }
}
