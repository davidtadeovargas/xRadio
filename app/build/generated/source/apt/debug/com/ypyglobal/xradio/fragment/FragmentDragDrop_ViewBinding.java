// Generated code from Butter Knife. Do not modify!
package com.ypyglobal.xradio.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.balysv.materialripple.MaterialRippleLayout;
import com.like.LikeButton;
import com.wang.avi.AVLoadingIndicatorView;
import com.warkiz.widget.IndicatorSeekBar;
import com.ypyglobal.xradio.R;
import eu.gsottbauer.equalizerview.EqualizerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FragmentDragDrop_ViewBinding implements Unbinder {
  private FragmentDragDrop target;

  private View view2131230836;

  private View view2131230776;

  private View view2131230781;

  private View view2131230783;

  private View view2131230777;

  private View view2131230780;

  private View view2131230790;

  private View view2131230789;

  private View view2131230784;

  @UiThread
  public FragmentDragDrop_ViewBinding(final FragmentDragDrop target, View source) {
    this.target = target;

    View view;
    target.mEqualizer = Utils.findRequiredViewAsType(source, R.id.equalizer, "field 'mEqualizer'", EqualizerView.class);
    target.mLayoutDragDropBg = Utils.findRequiredViewAsType(source, R.id.layout_drag_drop_bg, "field 'mLayoutDragDropBg'", RelativeLayout.class);
    target.mLoadingProgress = Utils.findRequiredViewAsType(source, R.id.play_progressBar1, "field 'mLoadingProgress'", AVLoadingIndicatorView.class);
    view = Utils.findRequiredView(source, R.id.fb_play, "field 'mBtnPlay' and method 'onClick'");
    target.mBtnPlay = Utils.castView(view, R.id.fb_play, "field 'mBtnPlay'", FloatingActionButton.class);
    view2131230836 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.mTvBuffering = Utils.findRequiredViewAsType(source, R.id.tv_percent, "field 'mTvBuffering'", TextView.class);
    target.mBtnLike = Utils.findRequiredViewAsType(source, R.id.btn_favorite, "field 'mBtnLike'", LikeButton.class);
    target.mLayoutFb = Utils.findRequiredViewAsType(source, R.id.layout_facebook, "field 'mLayoutFb'", MaterialRippleLayout.class);
    target.mLayoutInsta = Utils.findRequiredViewAsType(source, R.id.layout_instagram, "field 'mLayoutInsta'", MaterialRippleLayout.class);
    target.mLayoutTw = Utils.findRequiredViewAsType(source, R.id.layout_twitter, "field 'mLayoutTw'", MaterialRippleLayout.class);
    target.mLayoutWeb = Utils.findRequiredViewAsType(source, R.id.layout_website, "field 'mLayoutWeb'", MaterialRippleLayout.class);
    target.mSeekbar = Utils.findRequiredViewAsType(source, R.id.seekBar1, "field 'mSeekbar'", IndicatorSeekBar.class);
    target.mLayoutContent = Utils.findRequiredViewAsType(source, R.id.layout_content, "field 'mLayoutContent'", LinearLayout.class);
    target.mImgOverlay = Utils.findRequiredViewAsType(source, R.id.img_overlay, "field 'mImgOverlay'", ImageView.class);
    target.mTvSong = Utils.findRequiredViewAsType(source, R.id.tv_drag_song, "field 'mTvSong'", TextView.class);
    target.mTvSinger = Utils.findRequiredViewAsType(source, R.id.tv_drag_singer, "field 'mTvSinger'", TextView.class);
    target.mTvRadioName = Utils.findRequiredViewAsType(source, R.id.tv_title_drag_drop, "field 'mTvRadioName'", TextView.class);
    target.mTvBitRate = Utils.findRequiredViewAsType(source, R.id.tv_bitrate, "field 'mTvBitRate'", TextView.class);
    target.mImgCoverArt = Utils.findRequiredViewAsType(source, R.id.img_play_song, "field 'mImgCoverArt'", ImageView.class);
    target.mImgBg = Utils.findRequiredViewAsType(source, R.id.img_bg_drag_drop, "field 'mImgBg'", ImageView.class);
    target.mTvSleepMode = Utils.findRequiredViewAsType(source, R.id.tv_sleep_timer, "field 'mTvSleepMode'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_close, "method 'onClick'");
    view2131230776 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_next, "method 'onClick'");
    view2131230781 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_prev, "method 'onClick'");
    view2131230783 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
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
  @CallSuper
  public void unbind() {
    FragmentDragDrop target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mEqualizer = null;
    target.mLayoutDragDropBg = null;
    target.mLoadingProgress = null;
    target.mBtnPlay = null;
    target.mTvBuffering = null;
    target.mBtnLike = null;
    target.mLayoutFb = null;
    target.mLayoutInsta = null;
    target.mLayoutTw = null;
    target.mLayoutWeb = null;
    target.mSeekbar = null;
    target.mLayoutContent = null;
    target.mImgOverlay = null;
    target.mTvSong = null;
    target.mTvSinger = null;
    target.mTvRadioName = null;
    target.mTvBitRate = null;
    target.mImgCoverArt = null;
    target.mImgBg = null;
    target.mTvSleepMode = null;

    view2131230836.setOnClickListener(null);
    view2131230836 = null;
    view2131230776.setOnClickListener(null);
    view2131230776 = null;
    view2131230781.setOnClickListener(null);
    view2131230781 = null;
    view2131230783.setOnClickListener(null);
    view2131230783 = null;
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
  }
}
