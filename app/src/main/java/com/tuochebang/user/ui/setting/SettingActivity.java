package com.tuochebang.user.ui.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.cache.FileUtil;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.ui.WebActivity;
import com.tuochebang.user.util.AndroidUtil;

import java.text.DecimalFormat;

public class SettingActivity extends BaseActivity {
    private Button mBtnLogout;
    private RelativeLayout mRlAbout;
    private RelativeLayout mRlClearCache;
    private RelativeLayout mRlFeedBack;
    private RelativeLayout mRlServiceLicenc;
    private Toolbar mToolBar;
    private TextView mTxtClearCache;

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$1 */
    class C10031 implements OnClickListener {
        C10031() {
        }

        public void onClick(View v) {
            new ClearCacheAsyncTask().execute(new Void[0]);
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$2 */
    class C10042 implements OnClickListener {
        C10042() {
        }

        public void onClick(View v) {
            SettingActivity.this.onBtnLogout();
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$3 */
    class C10053 implements OnClickListener {
        C10053() {
        }

        public void onClick(View v) {
            ActivityUtil.next(SettingActivity.this, FeedBackActivity.class);
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$4 */
    class C10064 implements OnClickListener {
        C10064() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.FLAG_URL, ServerUrl.getInst().ABOUT_URL());
            bundle.putString(WebActivity.FLAG_TITLE, "关于我们");
            ActivityUtil.next(SettingActivity.this, WebActivity.class, bundle);
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$5 */
    class C10075 implements OnClickListener {
        C10075() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(WebActivity.FLAG_URL, ServerUrl.getInst().SERVICE_LIENCE());
            bundle.putString(WebActivity.FLAG_TITLE, "服务协议");
            ActivityUtil.next(SettingActivity.this, WebActivity.class, bundle);
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.SettingActivity$6 */
    class C10086 implements OnClickListener {
        C10086() {
        }

        public void onClick(View v) {
            SettingActivity.this.finish();
        }
    }

    private class ClearCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        private ClearCacheAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            SettingActivity.this.showCommonProgreessDialog("正在清除中");
            SettingActivity.this.mTxtClearCache.setEnabled(false);
        }

        protected Void doInBackground(Void... params) {
            ImageLoader.getInstance().clearDiskCache();
            AndroidUtil.deleteCacheImages();
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            SettingActivity.this.dismissCommonProgressDialog();
            ToastUtil.showMessage(MyApplication.getInstance(), "清除缓存成功");
            SettingActivity.this.mTxtClearCache.setText("0.0 M");
            SettingActivity.this.mTxtClearCache.setEnabled(true);
        }
    }

    private class DiskCacheSizeAsyncTask extends AsyncTask<Void, Void, String> {
        private DiskCacheSizeAsyncTask() {
        }

        protected String doInBackground(Void... params) {
            String size = "0";
            if (!ImageLoader.getInstance().getDiskCache().getDirectory().isDirectory()) {
                return size;
            }
            size = new DecimalFormat(".##").format(FileUtil.getDirSize(ImageLoader.getInstance().getDiskCache().getDirectory()));
            if (size.startsWith(".")) {
                return "0" + size;
            }
            return size;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            SettingActivity.this.mTxtClearCache.setText(result + "M");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        getDiskCacheSizeByAsyncTask();
    }

    private void initListener() {
        this.mRlClearCache.setOnClickListener(new C10031());
        this.mBtnLogout.setOnClickListener(new C10042());
        this.mRlFeedBack.setOnClickListener(new C10053());
        this.mRlAbout.setOnClickListener(new C10064());
        this.mRlServiceLicenc.setOnClickListener(new C10075());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C10086());
    }

    private void initView() {
        this.mRlAbout = (RelativeLayout) findViewById(R.id.tcb_about_us_rl);
        this.mRlClearCache = (RelativeLayout) findViewById(R.id.tcb_clear_cache_rl);
        this.mRlFeedBack = (RelativeLayout) findViewById(R.id.tcb_feed_back_rl);
        this.mRlServiceLicenc = (RelativeLayout) findViewById(R.id.tcb_setting_service_rl);
        this.mTxtClearCache = (TextView) findViewById(R.id.tcb_clear_cache_txt);
        this.mBtnLogout = (Button) findViewById(R.id.bt_log_out);
    }

    private void onBtnLogout() {
        ToastUtil.showMessage(MyApplication.getInstance(), "退出成功");
        MyApplication.getInstance().setUserInfo(null);
        MyApplication.getInstance().setLoginInfo(null);
        BroadCastUtil.sendBroadCast(this.mContext, BroadCastAction.USER_LOGOUT_SUCCESS);
        finish();
    }

    private void getDiskCacheSizeByAsyncTask() {
        new DiskCacheSizeAsyncTask().execute(new Void[0]);
    }
}
