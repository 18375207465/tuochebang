package com.tuochebang.user.ui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.entity.TabEntity;
import com.tuochebang.user.view.component.RequestItemView;
import java.util.ArrayList;
import java.util.List;

public class UserRequestActivity extends BaseActivity {
    private CommonPagerAdapter commonPagerAdapter;
    private RequestItemView completeItemView;
    private RequestItemView doingItemView;
    private BroadcastReceiver mRequestStatuChangeReceiver;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList();
    private CommonTabLayout mTabLayout;
    private String[] mTitles = new String[]{"待抢单", "进行中", "已完成"};
    private Toolbar mToolBar;
    private ViewPager mViewPager;
    private RequestItemView waitItemView;

    /* renamed from: com.tuochebang.user.ui.user.UserRequestActivity$1 */
    class C10251 extends BroadcastReceiver {
        C10251() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (BroadCastAction.TUOCHE_REQUEST_CHANGE.equals(intent.getAction())) {
                    UserRequestActivity.this.waitItemView.setData(RequestItemView.TUOCHE_REQUEST_WAIT_STATUS);
                    UserRequestActivity.this.doingItemView.setData(RequestItemView.TUOCHE_REQUEST_DOING_STATUS);
                }
            }
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserRequestActivity$2 */
    class C10262 implements OnTabSelectListener {
        C10262() {
        }

        public void onTabSelect(int position) {
            UserRequestActivity.this.mViewPager.setCurrentItem(position);
        }

        public void onTabReselect(int position) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserRequestActivity$3 */
    class C10273 implements OnPageChangeListener {
        C10273() {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            UserRequestActivity.this.mTabLayout.setCurrentTab(position);
        }

        public void onPageScrollStateChanged(int state) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.user.UserRequestActivity$4 */
    class C10284 implements OnClickListener {
        C10284() {
        }

        public void onClick(View v) {
            UserRequestActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_request);
        initToolBar();
        registUserStatuChangeBroadCastReciver();
        initView();
    }

    private void registUserStatuChangeBroadCastReciver() {
        this.mRequestStatuChangeReceiver = new C10251();
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mRequestStatuChangeReceiver, new IntentFilter(BroadCastAction.TUOCHE_REQUEST_CHANGE));
    }

    private void initView() {
        for (String tabEntity : this.mTitles) {
            this.mTabEntities.add(new TabEntity(tabEntity));
        }
        this.mTabLayout = (CommonTabLayout) findViewById(R.id.tablayout);
        this.mViewPager = (ViewPager) findViewById(R.id.view_pager);
        this.mTabLayout.setTabData(this.mTabEntities);
        this.mTabLayout.setOnTabSelectListener(new C10262());
        List<View> listView = new ArrayList();
        this.waitItemView = new RequestItemView(this.mContext);
        this.waitItemView.setData(RequestItemView.TUOCHE_REQUEST_WAIT_STATUS);
        this.doingItemView = new RequestItemView(this.mContext);
        this.doingItemView.setData(RequestItemView.TUOCHE_REQUEST_DOING_STATUS);
        this.completeItemView = new RequestItemView(this.mContext);
        this.completeItemView.setData(RequestItemView.TUOCHE_REQUEST_COMPLETE_STATUS);
        listView.add(this.waitItemView);
        listView.add(this.doingItemView);
        listView.add(this.completeItemView);
        this.commonPagerAdapter = new CommonPagerAdapter();
        this.commonPagerAdapter.setViewList(listView);
        this.mViewPager.setAdapter(this.commonPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new C10273());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C10284());
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mRequestStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mRequestStatuChangeReceiver);
        }
    }
}
