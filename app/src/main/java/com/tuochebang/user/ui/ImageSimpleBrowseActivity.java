package com.tuochebang.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.framework.app.component.widget.HackyViewPager;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.ImageSimpleBrowsePagerAdapter;
import com.tuochebang.user.base.BaseActivity;
import java.util.List;

public class ImageSimpleBrowseActivity extends BaseActivity {
    public static final String EXTAR_CAN_DEL = "can_del";
    public static final String EXTAR_DEL_URL = "del_url";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_POSITION = "position";
    private int mCurPostion = 0;
    private ImageSimpleBrowsePagerAdapter mImageSimpleBrowsePagerAdapter;
    private List<String> mImageUrlList;
    private LinearLayout mIndicatorViewGroup;
    private boolean mIsImageCanDel;
    private int mLastIndicator = 0;
    private Toolbar mToolBar;
    private TextView mTxtDelete;
    private ViewPager mViewPager;

    /* renamed from: com.tuochebang.user.ui.ImageSimpleBrowseActivity$1 */
    class C09341 implements OnClickListener {
        C09341() {
        }

        public void onClick(View v) {
            ImageSimpleBrowseActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.ImageSimpleBrowseActivity$2 */
    class C09352 implements OnClickListener {
        C09352() {
        }

        public void onClick(View v) {
            String selectUrl = (String) ImageSimpleBrowseActivity.this.mImageUrlList.get(ImageSimpleBrowseActivity.this.mViewPager.getCurrentItem());
            Intent intent = new Intent();
            intent.putExtra(ImageSimpleBrowseActivity.EXTAR_DEL_URL, selectUrl);
            intent.putExtra("position", ImageSimpleBrowseActivity.this.mCurPostion);
            ImageSimpleBrowseActivity.this.setResult(8, intent);
            ImageSimpleBrowseActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.ImageSimpleBrowseActivity$3 */
    class C09363 implements OnPageChangeListener {
        C09363() {
        }

        public void onPageSelected(int position) {
            ImageSimpleBrowseActivity.this.mIndicatorViewGroup.getChildAt(position).setEnabled(true);
            ImageSimpleBrowseActivity.this.mIndicatorViewGroup.getChildAt(ImageSimpleBrowseActivity.this.mLastIndicator).setEnabled(false);
            ImageSimpleBrowseActivity.this.mLastIndicator = position;
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_simple_browse);
        initToolBar();
        getIntentExtras();
        initActionBar();
        initViews();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mTxtDelete = (TextView) findViewById(R.id.tcb_delete_txt);
        this.mToolBar.setNavigationOnClickListener(new C09341());
        this.mTxtDelete.setOnClickListener(new C09352());
    }

    private void initActionBar() {
    }

    private void getIntentExtras() {
        this.mImageUrlList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        if (this.mImageUrlList != null && this.mImageUrlList.size() == 0) {
            this.mImageUrlList.add("This is a default image");
        }
        this.mCurPostion = getIntent().getExtras().getInt("position");
        this.mIsImageCanDel = getIntent().getBooleanExtra(EXTAR_CAN_DEL, false);
    }

    private void initViews() {
        this.mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        this.mImageSimpleBrowsePagerAdapter = new ImageSimpleBrowsePagerAdapter(this, this.mImageUrlList);
        this.mImageSimpleBrowsePagerAdapter.setCurrentActivity(this);
        this.mViewPager.setAdapter(this.mImageSimpleBrowsePagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C09363());
        initIndicator(this.mImageUrlList.size());
        this.mViewPager.setCurrentItem(this.mCurPostion);
    }

    private void initIndicator(int pageTotalCount) {
        this.mIndicatorViewGroup = (LinearLayout) findViewById(R.id.layout_indicator);
        if (pageTotalCount > 0) {
            for (int i = 0; i < pageTotalCount; i++) {
                ImageView imageView = new ImageView(this);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setLayoutParams(new LayoutParams(-2, -2));
                imageView.setImageResource(R.drawable.btn_viewpager_dot);
                imageView.setEnabled(false);
                this.mIndicatorViewGroup.addView(imageView);
            }
            this.mIndicatorViewGroup.getChildAt(this.mCurPostion).setEnabled(true);
        }
        if (this.mIndicatorViewGroup.getChildCount() > 0) {
            this.mIndicatorViewGroup.setVisibility(View.VISIBLE);
        }
    }
}
