// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio;

import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.wang.avi.AVLoadingIndicatorView;
import com.warkiz.widget.IndicatorSeekBar;
import eu.gsottbauer.equalizerview.EqualizerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XSingleRadioMainActivity_ViewBinding extends XRadioFragmentActivity_ViewBinding {
  private XSingleRadioMainActivity target;

  private View view2131230836;

  private View view2131230777;

  private View view2131230780;

  private View view2131230790;

  private View view2131230789;

  private View view2131230784;

  @UiThread
  public XSingleRadioMainActivity_ViewBinding(XSingleRadioMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XSingleRadioMainActivity_ViewBinding(final XSingleRadioMainActivity target, View source) {
    super(target, source);

    this.target = target;

    View view;
    target.mLayoutContainer = Utils.findRequiredViewAsType(source, R.id.layout_drag_drop_bg, "field 'mLayoutContainer'", RelativeLayout.class);
    target.mEqualizer = Utils.findRequiredViewAsType(source, R.id.equalizer, "field 'mEqualizer'", EqualizerView.class);
    view = Utils.findRequiredView(source, R.id.fb_play, "field 'mBtnPlay' and method 'onClick'");
    target.mBtnPlay = Utils.castView(view, R.id.fb_play, "field 'mBtnPlay'", FloatingActionButton.class);
    view2131230836 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mLoadingProgress = Utils.findRequiredViewAsType(source, R.id.play_progressBar1, "field 'mLoadingProgress'", AVLoadingIndicatorView.class);
    target.mTvBuffering = Utils.findRequiredViewAsType(source, R.id.tv_percent, "field 'mTvBuffering'", TextView.class);
    target.mImageOverlay = Utils.findRequiredViewAsType(source, R.id.img_overlay_bg, "field 'mImageOverlay'", ImageView.class);
    target.mImageBg = Utils.findRequiredViewAsType(source, R.id.img_bg, "field 'mImageBg'", ImageView.class);
    target.mLayoutFb = Utils.findRequiredViewAsType(source, R.id.layout_facebook, "field 'mLayoutFb'", MaterialRippleLayout.class);
    target.mLayoutInsta = Utils.findRequiredViewAsType(source, R.id.layout_instagram, "field 'mLayoutInsta'", MaterialRippleLayout.class);
    target.mLayoutTw = Utils.findRequiredViewAsType(source, R.id.layout_twitter, "field 'mLayoutTw'", MaterialRippleLayout.class);
    target.mLayoutWeb = Utils.findRequiredViewAsType(source, R.id.layout_website, "field 'mLayoutWeb'", MaterialRippleLayout.class);
    target.mSeekbar = Utils.findRequiredViewAsType(source, R.id.seekBar1, "field 'mSeekbar'", IndicatorSeekBar.class);
    target.mLayoutContent = Utils.findRequiredViewAsType(source, R.id.layout_content, "field 'mLayoutContent'", LinearLayout.class);
    target.mImgCoverArt = Utils.findRequiredViewAsType(source, R.id.img_play_song, "field 'mImgCoverArt'", ImageView.class);
    target.mTvSong = Utils.findRequiredViewAsType(source, R.id.tv_drag_song, "field 'mTvSong'", TextView.class);
    target.mTvSleepMode = Utils.findRequiredViewAsType(source, R.id.tv_sleep_timer, "field 'mTvSleepMode'", TextView.class);
    target.mTvSinger = Utils.findRequiredViewAsType(source, R.id.tv_drag_singer, "field 'mTvSinger'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_facebook, "method 'onClick'");
    view2131230777 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_instagram, "method 'onClick'");
    view2131230780 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_website, "method 'onClick'");
    view2131230790 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_twitter, "method 'onClick'");
    view2131230789 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_share, "method 'onClick'");
    view2131230784 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  public void unbind() {
    XSingleRadioMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mLayoutContainer = null;
    target.mEqualizer = null;
    target.mBtnPlay = null;
    target.mLoadingProgress = null;
    target.mTvBuffering = null;
    target.mImageOverlay = null;
    target.mImageBg = null;
    target.mLayoutFb = null;
    target.mLayoutInsta = null;
    target.mLayoutTw = null;
    target.mLayoutWeb = null;
    target.mSeekbar = null;
    target.mLayoutContent = null;
    target.mImgCoverArt = null;
    target.mTvSong = null;
    target.mTvSleepMode = null;
    target.mTvSinger = null;

    view2131230836.setOnClickListener(null);
    view2131230836 = null;
    view2131230777.setOnClickListener(null);
    view2131230777 = null;
    view2131230780.setOnClickListener(null);
    view2131230780 = null;
    view2131230790.setOnClickListener(null);
    view2131230790 = null;
    view2131230789.setOnClickListener(null);
    view2131230789 = null;
    view2131230784.setOnClickListener(null);
    view2131230784 = null;

    super.unbind();
  }
}
