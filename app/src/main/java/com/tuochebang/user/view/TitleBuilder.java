package com.tuochebang.user.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.tuochebang.user.R;

public class TitleBuilder {
    private ImageView ivLeft;
    private ImageView ivRight;
    private View rootView;
    private TextView tvLeft;
    private TextView tvRight;
    private TextView tvTitle;

    public View getRootView() {
        return this.rootView;
    }

    public TextView getTvTitle() {
        return this.tvTitle;
    }

    public ImageView getIvLeft() {
        return this.ivLeft;
    }

    public ImageView getIvRight() {
        return this.ivRight;
    }

    public TextView getTvLeft() {
        return this.tvLeft;
    }

    public TextView getTvRight() {
        return this.tvRight;
    }

    public TitleBuilder(Activity context) {
        this.rootView = context.findViewById(R.id.rl_titlebar);
        if (this.rootView != null) {
            this.tvTitle = (TextView) this.rootView.findViewById(R.id.titlebar_tv);
            this.ivLeft = (ImageView) this.rootView.findViewById(R.id.titlebar_iv_left);
            this.ivRight = (ImageView) this.rootView.findViewById(R.id.titlebar_iv_right);
            this.tvLeft = (TextView) this.rootView.findViewById(R.id.titlebar_tv_left);
            this.tvRight = (TextView) this.rootView.findViewById(R.id.titlebar_tv_right);
        }
    }

    public TitleBuilder(View context) {
        this.rootView = context.findViewById(R.id.rl_titlebar);
        if (this.rootView != null) {
            this.tvTitle = (TextView) this.rootView.findViewById(R.id.titlebar_tv);
            this.ivLeft = (ImageView) this.rootView.findViewById(R.id.titlebar_iv_left);
            this.ivRight = (ImageView) this.rootView.findViewById(R.id.titlebar_iv_right);
            this.tvLeft = (TextView) this.rootView.findViewById(R.id.titlebar_tv_left);
            this.tvRight = (TextView) this.rootView.findViewById(R.id.titlebar_tv_right);
        }
    }

    public TitleBuilder setTitleBgRes(int resid) {
        this.rootView.setBackgroundResource(resid);
        return this;
    }

    public TitleBuilder setTitleText(String text) {
        this.tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        this.tvTitle.setText(text);
        return this;
    }

    public TitleBuilder setLeftImage(int resId) {
        this.ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        this.ivLeft.setImageResource(resId);
        return this;
    }

    public TitleBuilder setLeftText(String text) {
        this.tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE);
        this.tvLeft.setText(text);
        return this;
    }

    public TitleBuilder setLeftOnClickListener(OnClickListener listener) {
        if (this.ivLeft.getVisibility() == View.GONE) {
            this.ivLeft.setOnClickListener(listener);
        } else if (this.tvLeft.getVisibility() == View.VISIBLE) {
            this.tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    public TitleBuilder setRightImage(int resId) {
        this.ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        this.ivRight.setImageResource(resId);
        return this;
    }

    public TitleBuilder setRightText(String text) {
        this.tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        this.tvRight.setText(text);
        return this;
    }

    public TitleBuilder setRightTextColor(Context context, int resId) {
        this.tvRight.setTextColor(context.getResources().getColor(resId));
        return this;
    }

    public TitleBuilder setTitleTextColor(Context context, int resId) {
        this.tvTitle.setTextColor(context.getResources().getColor(resId));
        return this;
    }

    public TitleBuilder setRightOnClickListener(OnClickListener listener) {
        if (this.ivRight.getVisibility() == View.VISIBLE) {
            this.ivRight.setOnClickListener(listener);
        } else if (this.tvRight.getVisibility() == View.VISIBLE) {
            this.tvRight.setOnClickListener(listener);
        }
        return this;
    }

    public View build() {
        return this.rootView;
    }
}
