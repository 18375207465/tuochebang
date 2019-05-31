package com.tuochebang.user.app;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.framework.app.component.utils.DateUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.tuochebang.user.cache.FileUtil;
import com.tuochebang.user.constant.AppConstant;
import com.tuochebang.user.request.entity.UserInfo;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;
    private String mLocationCity;
    private AMapLocationClient mLocationClient;
    private double mLocationLat = 0.0d;
    public AMapLocationListener mLocationListener = new C09281();
    private double mLocationLong = 0.0d;
    private AMapLocationClientOption mLocationOption;
    private String mToken;
    private UserInfo mUserInfo;
    private OSS oss;


    /* renamed from: com.tuochebang.user.app.MyApplication$1 */
    class C09281 implements AMapLocationListener {
        C09281() {
        }

        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null) {
                return;
            }
            if (aMapLocation.getErrorCode() == 0) {
                aMapLocation.getLocationType();
                MyApplication.this.mLocationLat = aMapLocation.getLatitude();
                MyApplication.this.mLocationLong = aMapLocation.getLongitude();
                aMapLocation.getAccuracy();
                new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS).format(new Date(aMapLocation.getTime()));
                aMapLocation.getAddress();
                aMapLocation.getCountry();
                aMapLocation.getProvince();
                MyApplication.this.mLocationCity = aMapLocation.getCity();
                aMapLocation.getDistrict();
                aMapLocation.getStreet();
                aMapLocation.getStreetNum();
                aMapLocation.getCityCode();
                aMapLocation.getAdCode();
                aMapLocation.getAoiName();
                return;
            }
            Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
    }

    private void init() {
        initHttp();
        initImageLoader();
        initLocalData();
        initAliyunOSS();
        initAmap();
        initPush();
    }

    private void initPush() {
        JPushInterface.init(this);
    }

    private void initAliyunOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(AppConstant.OSSaccessKeyId, AppConstant.OSSaccessKeySecret);
        this.oss = new OSSClient(getApplicationContext(), "oss-cn-qingdao.aliyuncs.com", credentialProvider);
    }

    private void initLocalData() {
        this.mUserInfo = (UserInfo) FileUtil.readFile(mInstance, AppConstant.FLAG_USER_INFO);
        this.mToken = (String) FileUtil.readFile(mInstance, AppConstant.FALG_LOGIN_INFO);
    }

    private void initImageLoader() {
        ImageLoaderUtil.initImageLoader(mInstance);
    }

    private void initHttp() {
//        InitializationConfig config = InitializationConfig.newBuilder(this)
//                .connectionTimeout(1000 * 10)
//                .readTimeout(1000 * 10)
//                //.cacheStore(new DBCacheStore(this).setEnable(true))
//                .networkExecutor(new OkHttpNetworkExecutor())
//                .build();
        NoHttp.initialize(this);
        Logger.setDebug(true);
        Logger.setTag("NoHttpSample");
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        FileUtil.saveFile(this, AppConstant.FLAG_USER_INFO, userInfo);
    }

    public void setLoginInfo(String token) {
        this.mToken = token;
        FileUtil.saveFile(this, AppConstant.FALG_LOGIN_INFO, token);
    }

    public String getToken() {
        return this.mToken;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public boolean isUserLogin() {
        return this.mToken != null;
    }

    public OSS getOssClient() {
        return this.oss;
    }

    public void setLocLong(double mLocationLong) {
        this.mLocationLong = mLocationLong;
    }

    public void setLocLat(double mLocationLat) {
        this.mLocationLat = mLocationLat;
    }

    public double getmLocationLong() {
        return this.mLocationLong;
    }

    public double getmLocationLat() {
        return this.mLocationLat;
    }

    public String getmLocationCity() {
        return this.mLocationCity;
    }

    public void setmLocationCity(String mLocationCity) {
        this.mLocationCity = mLocationCity;
    }

    private void initAmap() {
        this.mLocationClient = new AMapLocationClient(getApplicationContext());
        this.mLocationClient.setLocationListener(this.mLocationListener);
        this.mLocationOption = new AMapLocationClientOption();
        this.mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        this.mLocationOption.setNeedAddress(true);
        this.mLocationOption.setOnceLocation(false);
        this.mLocationOption.setWifiActiveScan(false);
        this.mLocationOption.setMockEnable(true);
        this.mLocationOption.setInterval(60000);
        this.mLocationClient.setLocationOption(this.mLocationOption);
        this.mLocationClient.startLocation();
    }

    public void startLocation() {
        if (this.mLocationClient != null) {
            this.mLocationClient.stopLocation();
            this.mLocationClient.startLocation();
        }
    }

    public void onTerminate() {
        super.onTerminate();
        this.mLocationClient.onDestroy();
        this.mLocationClient.stopLocation();
    }
}
