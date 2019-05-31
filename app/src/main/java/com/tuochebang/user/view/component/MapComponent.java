package com.tuochebang.user.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MapComponent extends ScrollView {
    public MapComponent(Context context) {
        super(context);
        initView(context);
    }

    public MapComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MapComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
