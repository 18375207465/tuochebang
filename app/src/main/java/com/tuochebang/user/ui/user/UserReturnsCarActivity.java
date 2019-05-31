package com.tuochebang.user.ui.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import com.framework.app.component.adapter.CommonPagerAdapter;
import com.framework.app.component.view.CommonTabLayout;
import com.framework.app.component.view.listener.CustomTabEntity;
import com.framework.app.component.view.listener.OnTabSelectListener;
import com.tuochebang.user.R;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.entity.TabEntity;
import com.tuochebang.user.view.component.ReturnsItemView;
import java.util.ArrayList;
import java.util.List;

public class UserReturnsCarActivity extends BaseActivity {
    private CommonPagerAdapter commonPagerAdapter;
    private ReturnsItemView completeItemView;
    private ReturnsItemView doingItemView;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"进行中", "已完成"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private ReturnsItemView watingItemView;

    /* renamed from: com.tuochebang.user.ui.user.UserReturnsCarActivity$1 */
    class C10291 implements OnTabSelectListener {
        C10291() {
        }

        public void onTabSelect(int position) {
            UserReturnsCarActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserReturnsCarActivity$2 */
    class C10302 implements OnPageChangeListener {
        C10302() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            UserReturnsCarActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserReturnsCarActivity$3 */
    class C10313 implements OnClickListener {
        C10313() {
        }

        public void onClick(View v) {
            UserReturnsCarActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_returns);
        initToolBar();
        initView();
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tl_1);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C10291());
        List<View> listView = new ArrayList();
        this.doingItemView = new ReturnsItemView(this.mContext);
        this.doingItemView.setData(ReturnsItemView.TUOCHE_RETURN_DOING_STATUS);
        this.completeItemView = new ReturnsItemView(this.mContext);
        this.completeItemView.setData(ReturnsItemView.TUOCHE_RETURN_COMPLETE_STATUS);
        listView.add(this.doingItemView);
        listView.add(this.completeItemView);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C10302());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C10313());
    }
}
