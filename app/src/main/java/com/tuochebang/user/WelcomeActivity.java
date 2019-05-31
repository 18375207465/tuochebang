package com.tuochebang.user;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.user.util.ImageCompress;
import com.tuochebang.user.util.SharedPreferencesUtil;
import com.tuochebang.user.util.transforms.StackTransformer;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity implements OnPageChangeListener {
    private static final int VIEW_NO_1 = 0;
    private static final int VIEW_NO_2 = 1;
    private static final int VIEW_NO_3 = 2;
    private static final int VIEW_NO_4 = 3;
    private static final int VIEW_NO_5 = 4;
    static ImageView mOnePointer;
    public static int screenH;
    public static int screenW;
    public Context context;
    List<View> list = new ArrayList();
    OnTouchListener mOnTouchListener = new C09181();
    private ViewPager mPager;
    private MyViewPagerAdapter mPagerAdapter;
    private int preIndex = 0;
    int x1 = 0;
    int x2 = 0;

    /* renamed from: com.tuochebang.user.WelcomeActivity$1 */
    class C09181 implements OnTouchListener {
        C09181() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (WelcomeActivity.this.preIndex == 4) {
                switch (event.getAction()) {
                    case 0:
                        WelcomeActivity.this.x1 = (int) event.getX();
                        break;
                    case 1:
                        WelcomeActivity.this.x2 = (int) event.getX();
                        if (WelcomeActivity.this.x2 - WelcomeActivity.this.x1 < 0) {
                            WelcomeActivity.this.finish();
                            ActivityUtil.next(WelcomeActivity.this, MainActivity.class);
                            break;
                        }
                        break;
                    case 2:
                        WelcomeActivity.this.x2 = (int) event.getX();
                        if (WelcomeActivity.this.x2 - WelcomeActivity.this.x1 < 0) {
                            WelcomeActivity.this.finish();
                            ActivityUtil.next(WelcomeActivity.this, MainActivity.class);
                            break;
                        }
                        break;
                }
            }
            return true;
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            BitmapDrawable drawable = (BitmapDrawable) ((View) this.mListViews.get(position)).getBackground();
            if (drawable != null) {
                drawable.getBitmap().recycle();
            }
            switch (position) {
            }
            container.removeView((View) this.mListViews.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            View view = (View) this.mListViews.get(position);
            container.addView(view, 0);
            switch (position) {
                case 0:
                    view.setBackgroundDrawable(ImageCompress.getInstance().getCompressFromId(WelcomeActivity.this.context, R.drawable.guide_one_bg, WelcomeActivity.screenW, WelcomeActivity.screenH));
                    break;
                case 1:
                    view.setBackgroundDrawable(ImageCompress.getInstance().getCompressFromId(WelcomeActivity.this.context, R.drawable.guide_two_bg, WelcomeActivity.screenW, WelcomeActivity.screenH));
                    break;
                case 2:
                    view.setBackgroundDrawable(ImageCompress.getInstance().getCompressFromId(WelcomeActivity.this.context, R.drawable.guide_three_bg, WelcomeActivity.screenW, WelcomeActivity.screenH));
                    break;
                case 3:
                    view.setBackgroundDrawable(ImageCompress.getInstance().getCompressFromId(WelcomeActivity.this.context, R.drawable.guide_four_bg, WelcomeActivity.screenW, WelcomeActivity.screenH));
                    break;
                case 4:
                    view.setOnTouchListener(WelcomeActivity.this.mOnTouchListener);
                    view.setBackgroundDrawable(ImageCompress.getInstance().getCompressFromId(WelcomeActivity.this.context, R.drawable.guide_five_bg, WelcomeActivity.screenW, WelcomeActivity.screenH));
                    break;
            }
            return this.mListViews.get(position);
        }

        public int getCount() {
            return this.mListViews.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.context = this;
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        LayoutInflater inflater = LayoutInflater.from(this);
        View view0 = inflater.inflate(R.layout.guide_fragment_main_1, null, false);
        View view1 = inflater.inflate(R.layout.guide_fragment_main_2, null, false);
        View view2 = inflater.inflate(R.layout.guide_fragment_main_3, null, false);
        View view3 = inflater.inflate(R.layout.guide_fragment_main_4, null, false);
        View view4 = inflater.inflate(R.layout.guide_fragment_main_5, null, false);
        this.list.add(view0);
        this.list.add(view1);
        this.list.add(view2);
        this.list.add(view3);
        this.list.add(view4);
        this.mPager = (ViewPager) findViewById(R.id.container);
        this.mPagerAdapter = new MyViewPagerAdapter(this.list);
        this.mPager.setAdapter(this.mPagerAdapter);
        this.mPager.setOnPageChangeListener(this);
        this.mPager.setPageTransformer(true, new StackTransformer());
        animal(0);
        SharedPreferencesUtil.getInstance().putBoolean(SharedPreferencesUtil.KEY_APP_FRIST_RUN, true);
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        animal(position);
    }

    public void onPageScrollStateChanged(int state) {
    }

    private void animal(int position) {
        switch (position) {
        }
        try {
            this.preIndex = position;
        } catch (Exception e) {
            finish();
        }
    }
}
