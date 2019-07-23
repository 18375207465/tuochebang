package com.tuochebang.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tuochebang.user.add.HttpDownload;
import com.tuochebang.user.add.HttpGet;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.LoginInfo;
import com.tuochebang.user.request.entity.UserInfo;
import com.tuochebang.user.request.task.UserInfoRequest;
import com.tuochebang.user.ui.CheckPermissionActivity;
import com.tuochebang.user.ui.login.LoginActivity;
import com.tuochebang.user.ui.request.PublishRequestActivity;
import com.tuochebang.user.ui.returns.FindReturnActivity;
import com.tuochebang.user.ui.setting.SettingActivity;
import com.tuochebang.user.ui.user.UserInfoActivity;
import com.tuochebang.user.ui.user.UserMessageActivity;
import com.tuochebang.user.ui.user.UserRequestActivity;
import com.tuochebang.user.ui.user.UserReturnsCarActivity;
import com.tuochebang.user.util.ImageUtil;
import com.tuochebang.user.view.PullToZoomScrollView;
import com.tuochebang.user.widget.RequestRobDialog;
import com.tuochebang.user.widget.RequestRobDialog.DialogButtonInterface;
import com.tuochebang.user.widget.RequestRobDialog.DialogResult;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.json.JSONObject;

import java.util.Set;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends CheckPermissionActivity {
    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 1029;
    private CircleImageView mImgHeader;
    private PullToZoomScrollView mPullView;
    private RelativeLayout mRlHeaderView;
    private View mRlRequest;
    private RelativeLayout mRlUserFindReturn;
    private RelativeLayout mRlUserMessage;
    private RelativeLayout mRlUserRequest;
    private RelativeLayout mRlUserReturns;
    private RelativeLayout mRlUserSetting;
    private TextView mTxtAddress;
    private TextView mTxtCorporate;
    private TextView mTxtMessageCount;
    private TextView mTxtNickName;
    private TextView mTxtUserType;
    private BroadcastReceiver mUserStatuChangeReceiver;

    /* renamed from: com.tuochebang.user.MainActivity$1 */
    class C09071 implements Observer {
        C09071() {
        }

        public void onSubscribe(Disposable d) {
        }

        public void onNext(Object o) {
        }

        public void onError(Throwable e) {
        }

        public void onComplete() {
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$2 */
    class C09082 implements Observer {
        C09082() {
        }

        public void onSubscribe(Disposable d) {
        }

        public void onNext(Object o) {
        }

        public void onError(Throwable e) {
        }

        public void onComplete() {
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$3 */
    class C09093 implements DialogButtonInterface {
        C09093() {
        }

        public void onDialogButtonClick(DialogResult dialogResult) {
            if (dialogResult == DialogResult.Yes) {
                ToastUtil.showMessage(MyApplication.getInstance(), "抢单");
            }
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$4 */
    class C09104 implements ImageLoadingListener {
        C09104() {
        }

        public void onLoadingStarted(String arg0, View arg1) {
        }

        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            MainActivity.this.mImgHeader.setImageResource(R.mipmap.icon_account);
        }

        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
            Bitmap userHeader = bitmap;
            if (userHeader != null) {
                MainActivity.this.mRlHeaderView.setBackgroundDrawable(ImageUtil.BoxBlurFilter(userHeader));
            }
        }

        public void onLoadingCancelled(String arg0, View arg1) {
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$5 */
    class C09115 implements OnResponseListener<LoginInfo> {
        C09115() {
        }

        public void onSucceed(int what, Response<LoginInfo> response) {
            LoginInfo loginInfo = (LoginInfo) response.get();
            if (loginInfo != null) {
                Log.e(MainActivity.this.TAG, loginInfo.toString());
                MyApplication.getInstance().setUserInfo(loginInfo.getUser());
                MainActivity.this.refreshView(loginInfo.getUser());
            }
        }

        public void onFailed(int what, Response<LoginInfo> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
        }

        public void onFinish(int what) {
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$6 */
    class C09126 implements OnClickListener {
        C09126() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, PublishRequestActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$7 */
    class C09137 implements OnClickListener {
        C09137() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, FindReturnActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$8 */
    class C09148 implements OnClickListener {
        C09148() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserInfoActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    /* renamed from: com.tuochebang.user.MainActivity$9 */
    class C09159 implements OnClickListener {
        C09159() {
        }

        public void onClick(View v) {
            if (MyApplication.getInstance().isUserLogin()) {
                ActivityUtil.next(MainActivity.this, UserRequestActivity.class);
            } else {
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!MyApplication.getInstance().isUserLogin()) {
            ActivityUtil.next(this, LoginActivity.class);
        }else {
            jianChaGengXin();//检查更新
        }
        setCouldDoubleBackExit(true);
        initView();
        initPermission();
        initListener();
        registUserStatuChangeBroadCastReciver();
        initData();
    }

    private void initPermission() {
        if (VERSION.SDK_INT >= 23) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new C09071());
        } else {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, new C09082());
        }
    }

    private void textPushDialog() {
        if (MyApplication.getInstance().isUserLogin()) {
            new RequestRobDialog(this.mContext, "", "", new C09093()).show();
        }
    }

    private void initData() {
        if (MyApplication.getInstance().isUserLogin()) {
            refreshView(MyApplication.getInstance().getUserInfo());
            httpGetUserInfo();
            return;
        }
        refreshView(null);
    }

    private void refreshView(UserInfo mUserInfo) {
        if (mUserInfo == null) {
            this.mTxtCorporate.setText("---");
            this.mTxtNickName.setText("未登录");
            this.mTxtAddress.setText("----");
            this.mImgHeader.setImageResource(R.mipmap.icon_bigportrait);
            return;
        }
        if (TextUtils.isEmpty(mUserInfo.getPicture())) {
            this.mImgHeader.setImageResource(R.mipmap.icon_bigportrait);
        } else {
            ImageLoader.getInstance().displayImage(mUserInfo.getPicture(), this.mImgHeader, ImageLoaderUtil.getNotShowDisplayImageOptions(), new C09104());
        }
        this.mTxtCorporate.setText(mUserInfo.getCorporate());
        this.mTxtNickName.setText(mUserInfo.getNickName());
        this.mTxtUserType.setText(mUserInfo.getUserType());
        this.mTxtAddress.setText(mUserInfo.getAddress());
        this.mTxtMessageCount.setText(String.valueOf(mUserInfo.getUnreadMsgCount()));
        if (mUserInfo.getUnreadMsgCount() > 0) {
            this.mTxtMessageCount.setVisibility(View.VISIBLE);
        } else {
            this.mTxtMessageCount.setVisibility(View.GONE);
        }
    }

    private void httpGetUserInfo() {
        RequestQueue queue = NoHttp.newRequestQueue();
        UserInfoRequest request = new UserInfoRequest(ServerUrl.getInst().USER_INFO_URL(), RequestMethod.POST, String.valueOf(MyApplication.getInstance().getUserInfo().getUserId()));
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new C09115());

    }

    private void initListener() {
        this.mRlRequest.setOnClickListener(new C09126());
        this.mRlUserFindReturn.setOnClickListener(new C09137());
        this.mImgHeader.setOnClickListener(new C09148());
        this.mRlUserRequest.setOnClickListener(new C09159());
        this.mRlUserReturns.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserReturnsCarActivity.class);
                } else {
                    ActivityUtil.next(MainActivity.this, LoginActivity.class);
                }
            }
        });
        this.mRlUserMessage.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MyApplication.getInstance().isUserLogin()) {
                    ActivityUtil.next(MainActivity.this, UserMessageActivity.class);
                    MainActivity.this.mTxtMessageCount.setVisibility(View.GONE);
                    return;
                }
                ActivityUtil.next(MainActivity.this, LoginActivity.class);
            }
        });
        this.mRlUserSetting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ActivityUtil.next(MainActivity.this, SettingActivity.class);
            }
        });
    }

    private void initView() {
        this.mRlRequest = findViewById(R.id.tcb_pull_request_rl);
        this.mImgHeader = (CircleImageView) findViewById(R.id.tcb_profile_img);
        this.mTxtNickName = (TextView) findViewById(R.id.tcb_user_nickname_txt);
        this.mTxtCorporate = (TextView) findViewById(R.id.tcb_user_corporate_txt);
        this.mTxtUserType = (TextView) findViewById(R.id.tcb_user_type_txt);
        this.mTxtAddress = (TextView) findViewById(R.id.tcb_user_address_txt);
        this.mTxtMessageCount = (TextView) findViewById(R.id.txt_message_count);
        this.mPullView = (PullToZoomScrollView) findViewById(R.id.tcb_main_root_scroll);
        this.mRlHeaderView = (RelativeLayout) findViewById(R.id.rl_header_view);
        this.mPullView.setZoomView(this.mRlHeaderView);
        this.mRlUserRequest = (RelativeLayout) findViewById(R.id.tcb_user_request_rl);
        this.mRlUserFindReturn = (RelativeLayout) findViewById(R.id.tcb_pull_find_return_rl);
        this.mRlUserReturns = (RelativeLayout) findViewById(R.id.tcb_returns_car_rl);
        this.mRlUserMessage = (RelativeLayout) findViewById(R.id.tcb_message_rl);
        this.mRlUserSetting = (RelativeLayout) findViewById(R.id.tcb_setting_rl);
    }

    private void registUserStatuChangeBroadCastReciver() {
        this.mUserStatuChangeReceiver = new BroadcastReceiver() {

            /* renamed from: com.tuochebang.user.MainActivity$13$1 */
            class C09061 implements TagAliasCallback {
                C09061() {
                }

                public void gotResult(int i, String s, Set<String> set) {
                }
            }

            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String action = intent.getAction();
                    if (BroadCastAction.USER_LOGIN_SUCCESS.equals(action)) {
                        JPushInterface.setAlias(MainActivity.this.mContext, String.valueOf(MyApplication.getInstance().getUserInfo().getMobile()), new C09061());
                        MainActivity.this.refreshView(MyApplication.getInstance().getUserInfo());
                    } else if (BroadCastAction.USER_LOGOUT_SUCCESS.equals(action)) {
                        ActivityUtil.next(MainActivity.this, LoginActivity.class);
                        MainActivity.this.refreshView(null);
                    } else if (BroadCastAction.VALIDATA_TOKE.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), "该账号已在其它设备请重新登录");
                        MyApplication.getInstance().setUserInfo(null);
                        MyApplication.getInstance().setLoginInfo(null);
                        BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.USER_LOGOUT_SUCCESS);
                    } else if (BroadCastAction.TOKE_EXPIRE.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), "登录信息已过期请重新登录");
                        MyApplication.getInstance().setUserInfo(null);
                        MyApplication.getInstance().setLoginInfo(null);
                        BroadCastUtil.sendBroadCast(MyApplication.getInstance(), BroadCastAction.USER_LOGOUT_SUCCESS);
                    } else if (BroadCastAction.REQUEST_ERROR.equals(action)) {
                        ToastUtil.showMessage(MyApplication.getInstance(), intent.getStringExtra("msg"));
                    } else if (BroadCastAction.USER_INFO_CHANGE.equals(action)) {
                        MainActivity.this.httpGetUserInfo();
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(BroadCastAction.USER_LOGIN_SUCCESS);
        filter.addAction(BroadCastAction.USER_LOGOUT_SUCCESS);
        filter.addAction(BroadCastAction.VALIDATA_TOKE);
        filter.addAction(BroadCastAction.REQUEST_ERROR);
        filter.addAction(BroadCastAction.TOKE_EXPIRE);
        filter.addAction(BroadCastAction.USER_INFO_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mUserStatuChangeReceiver, filter);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mUserStatuChangeReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mUserStatuChangeReceiver);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 97:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.i("info", "6,权限被拒绝==读取");
                    showMissingPermissionDialog("读写权限");
                    return;
                }
                Log.i("info", "5,权限被同意==读取");
                checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 129);
                return;
            case 113:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.i("info", "6,权限被拒绝==写入");
                    showMissingPermissionDialog("读写权限");
                    return;
                }
                Log.i("info", "5,权限被同意==写入");
                return;
            case 129:
                if (grantResults.length <= 0 || grantResults[0] != 0) {
                    Log.i("info", "6,权限被拒绝==定位");
                    showMissingPermissionDialog("定位权限");
                    return;
                }
                Log.i("info", "5,权限被同意==定位");
                return;
            default:
                return;
        }
    }

    protected void permissionHasGranted(String permission) {
        if (permission.equals(ACCESS_COARSE_LOCATION)) {
        } else if (permission.equals(READ_EXTERNAL_STORAGE)) {
            checkPermission(ACCESS_COARSE_LOCATION, 129);
        }
    }

    protected void onResume() {
        super.onResume();
        initPermission();
        MobclickAgent.onResume(this);
    }



    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    String apkurl = "";//下载apk路径
    private void jianChaGengXin(){
        new HttpGet() {
            @Override
            public void startHttp(int what) {

            }

            @Override
            public void succeedHttp(int what, Response<String> response) {
                int bbh=0;
                String bbmc = "";
                int code = 0;//当前版本号
                String name = "";
                PackageManager manager = getPackageManager();

                try {
                    //获取当前版本信息
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    code = info.versionCode;
                    name = info.versionName;

                    //解析数据
                    String s=""+response.get();
                    JSONObject jsonObject=new JSONObject(s);
                    String c=""+jsonObject.get("code");
                    if (c.equals("0")){
                        JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        bbmc=jsonObject1.getString("bbmc");
                        bbh=jsonObject1.getInt("bbh");
                        apkurl=jsonObject1.getString("apkurl");

//                        bbh=1611;//返回的版本号
//                        bbmc="1.7.0";//版本名称

                        if (bbh>code){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("更新版本："+bbmc)
                                    .setMessage("当前版本"+name+"  即将更新:"+bbmc)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new HttpDownload(MainActivity.this).download(apkurl);//下载更新
                                        }
                                    }).setCancelable(true)
                                    .create()
                                    .show();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failedHttp(int what, Response<String> response) {

            }

            @Override
            public void finishHttp(int what) {

            }
        }.get("http://api.tuocb.com/tuochebang/rest/client/v1.0/user/apk/1");
    }


}
