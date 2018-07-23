// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio;

import android.support.annotation.UiThread;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.ypyglobal.xradio.ypylibs.view.CircularProgressBar;
import com.ypyglobal.xradio.ypylibs.view.YPYViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XMultiRadioMainActivity_ViewBinding extends XRadioFragmentActivity_ViewBinding {
  private XMultiRadioMainActivity target;

  private View view2131230786;

  private View view2131230785;

  private View view2131230787;

  @UiThread
  public XMultiRadioMainActivity_ViewBinding(XMultiRadioMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XMultiRadioMainActivity_ViewBinding(final XMultiRadioMainActivity target, View source) {
    super(target, source);

    this.target = target;

    View view;
    target.mTabLayout = Utils.findRequiredViewAsType(source, R.id.tab_layout, "field 'mTabLayout'", TabLayout.class);
    target.mAppBarLayout = Utils.findRequiredViewAsType(source, R.id.app_bar, "field 'mAppBarLayout'", AppBarLayout.class);
    target.mViewpager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'mViewpager'", YPYViewPager.class);
    target.mLayoutContainer = Utils.findRequiredViewAsType(source, R.id.container, "field 'mLayoutContainer'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.btn_small_play, "field 'mBtnSmallPlay' and method 'onClick'");
    target.mBtnSmallPlay = Utils.castView(view, R.id.btn_small_play, "field 'mBtnSmallPlay'", ImageView.class);
    view2131230786 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_small_next, "field 'mBtnSmallNext' and method 'onClick'");
    target.mBtnSmallNext = Utils.castView(view, R.id.btn_small_next, "field 'mBtnSmallNext'", ImageView.class);
    view2131230785 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_small_prev, "field 'mBtnSmallPrev' and method 'onClick'");
    target.mBtnSmallPrev = Utils.castView(view, R.id.btn_small_prev, "field 'mBtnSmallPrev'", ImageView.class);
    view2131230787 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mImgSmallSong = Utils.findRequiredViewAsType(source, R.id.img_song, "field 'mImgSmallSong'", ImageView.class);
    target.mTvRadioName = Utils.findRequiredViewAsType(source, R.id.tv_radio_name, "field 'mTvRadioName'", TextView.class);
    target.mTvSmallInfo = Utils.findRequiredViewAsType(source, R.id.tv_info, "field 'mTvSmallInfo'", TextView.class);
    target.mProgressBar = Utils.findRequiredViewAsType(source, R.id.img_status_loading, "field 'mProgressBar'", CircularProgressBar.class);
    target.mLayoutSmallControl = Utils.findRequiredViewAsType(source, R.id.layout_small_control, "field 'mLayoutSmallControl'", RelativeLayout.class);
    target.mLayoutDragDropContainer = Utils.findRequiredViewAsType(source, R.id.drag_drop_container, "field 'mLayoutDragDropContainer'", FrameLayout.class);
    target.mLayoutTotalDragDrop = Utils.findRequiredView(source, R.id.layout_total_drag_drop, "field 'mLayoutTotalDragDrop'");
  }

  @Override
  public void unbind() {
    XMultiRadioMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTabLayout = null;
    target.mAppBarLayout = null;
    target.mViewpager = null;
    target.mLayoutContainer = null;
    target.mBtnSmallPlay = null;
    target.mBtnSmallNext = null;
    target.mBtnSmallPrev = null;
    target.mImgSmallSong = null;
    target.mTvRadioName = null;
    target.mTvSmallInfo = null;
    target.mProgressBar = null;
    target.mLayoutSmallControl = null;
    target.mLayoutDragDropContainer = null;
    target.mLayoutTotalDragDrop = null;

    view2131230786.setOnClickListener(null);
    view2131230786 = null;
    view2131230785.setOnClickListener(null);
    view2131230785 = null;
    view2131230787.setOnClickListener(null);
    view2131230787 = null;

    super.unbind();
  }
}
