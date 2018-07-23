/*
 * Copyright (c) 2017. YPY Global - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at.
 *
 *         http://ypyglobal.com/sourcecode/policy
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ypyglobal.xradio.ypylibs.imageloader.target;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;


public abstract class GlideGroupTarget<Z> extends ViewTarget<ViewGroup,Z> implements  GlideAnimation.ViewAdapter {

    public GlideGroupTarget(ViewGroup view) {
        super(view);
    }

    /**
     * Returns the current {@link Drawable} being displayed in the view using
     * {@link android.widget.ImageView#getDrawable()}.
     */
    @Override
    public Drawable getCurrentDrawable() {
        return view.getBackground();
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link android.widget.ImageView#setImageDrawable(Drawable)}.
     *
     * @param drawable {@inheritDoc}
     */
    @Override
    public void setDrawable(Drawable drawable) {
        view.setBackground(drawable);
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link android.widget.ImageView#setImageDrawable(Drawable)}.
     *
     * @param placeholder {@inheritDoc}
     */
    @Override
    public void onLoadStarted(Drawable placeholder) {
        view.setBackground(placeholder);
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link android.widget.ImageView#setImageDrawable(Drawable)}.
     *
     * @param errorDrawable {@inheritDoc}
     */
    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {
        view.setBackground(errorDrawable);
    }

    /**
     * Sets the given {@link Drawable} on the view using
     * {@link android.widget.ImageView#setImageDrawable(Drawable)}.
     *
     * @param placeholder {@inheritDoc}
     */
    @Override
    public void onLoadCleared(Drawable placeholder) {
        view.setBackground(placeholder);
    }

    @Override
    public void onResourceReady(Z resource, GlideAnimation<? super Z> glideAnimation) {

        this.setResource(resource);
    }

    protected abstract void setResource(Z resource);
}
