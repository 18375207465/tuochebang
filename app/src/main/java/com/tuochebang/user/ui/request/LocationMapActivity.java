package com.tuochebang.user.ui.request;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.ui.CheckPermissionActivity;
import com.tuochebang.user.util.SensorEventHelper;
import com.tuochebang.user.view.citypicker.CityPickerActivity;
import java.util.List;

public class LocationMapActivity extends CheckPermissionActivity implements OnCameraChangeListener, OnGeocodeSearchListener, InputtipsListener {
    public static final String EXTRAS_ADDRESS = "extras_address";
    public static final String EXTRAS_LOC = "extras_loc";
    public static final String EXTRAS_TYPE = "extras_type";
    public static final int LOCATION_FROM = 0;
    public static final int LOCATION_TO = 1;
    private AMap aMap;
    private String addressName;
    private String city = "贵阳";
    private GeocodeSearch geocoderSearch;
    private boolean isReturn;
    private LatLng latlng;
    private String mAddress = "";
    private ImageView mImgLocation;
    private Marker mLocMarker;
    private double mLocationLat;
    private double mLocationLong;
    private AMapLocationClientOption mLocationOption;
    private SensorEventHelper mSensorHelper;
    private Toolbar mToolBar;
    private TextView mTxtLocText;
    private TextView mTxtSelectCity;
    private TextView mTxtSure;
    private int mType = 0;
    private MapView mapView;
    private MarkerOptions markerOption;
    private AMapLocationClient mlocationClient;

    /* renamed from: com.tuochebang.user.ui.request.LocationMapActivity$1 */
    class C09641 implements OnClickListener {
        C09641() {
        }

        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(DistrictSearchQuery.KEYWORDS_CITY, LocationMapActivity.this.city);
            ActivityUtil.next(LocationMapActivity.this, SelectLocationActivity.class, bundle, false, 100);
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.LocationMapActivity$2 */
    class C09652 implements OnClickListener {
        C09652() {
        }

        public void onClick(View v) {
            LocationMapActivity.this.onBtnBack();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.LocationMapActivity$3 */
    class C09663 implements OnClickListener {
        C09663() {
        }

        public void onClick(View v) {
            LocationMapActivity.this.startActivityForResult(new Intent(LocationMapActivity.this, CityPickerActivity.class), 0);
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.LocationMapActivity$4 */
    class C09674 implements OnClickListener {
        C09674() {
        }

        public void onClick(View v) {
            LocationMapActivity.this.onBtnBack();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getIntentExtras();
        initToolBar();
        initView(savedInstanceState);
    }

    private void getIntentExtras() {
        this.mType = getIntent().getIntExtra("extras_type", 0);
    }

    private void initView(Bundle savedInstanceState) {
        this.mapView = (MapView) findViewById(R.id.map);
        this.mapView.onCreate(savedInstanceState);
        this.mTxtLocText = (TextView) findViewById(R.id.input_text);
        this.mTxtSelectCity = (TextView) findViewById(R.id.txt_select_city);
        this.mTxtSure = (TextView) findViewById(R.id.tcb_sure_txt);
        this.mImgLocation = (ImageView) findViewById(R.id.img_location);
        if (this.aMap == null) {
            this.aMap = this.mapView.getMap();
        }
        this.aMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.aMap.setMyLocationEnabled(true);
        this.aMap.setOnCameraChangeListener(this);
        this.mTxtLocText.setOnClickListener(new C09641());
        this.mTxtSure.setOnClickListener(new C09652());
        this.mTxtSelectCity.setText(MyApplication.getInstance().getmLocationCity());
        this.mTxtSelectCity.setOnClickListener(new C09663());
        this.geocoderSearch = new GeocodeSearch(this);
        this.geocoderSearch.setOnGeocodeSearchListener(this);
        this.mSensorHelper = new SensorEventHelper(this);
        if (this.mSensorHelper != null) {
            this.mSensorHelper.registerSensorListener();
        }
        addMarkersToMap();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (this.mType == 1) {
            this.mToolBar.setTitle((CharSequence) "输入终点");
        }
        this.mToolBar.setNavigationOnClickListener(new C09674());
    }

    private void onBtnBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("extras_type", this.mType);
        bundle.putString(EXTRAS_ADDRESS, this.mAddress);
        bundle.putParcelable("latlng", this.latlng);
        intent.putExtras(bundle);
        setResult(4, intent);
        finish();
    }

    private void addMarkersToMap() {
        if (this.aMap != null) {
            this.aMap.clear();
        }
        this.markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_point)).anchor(0.8f, 0.8f).position(new LatLng(MyApplication.getInstance().getmLocationLat(), MyApplication.getInstance().getmLocationLong()));
        this.mLocMarker = this.aMap.addMarker(this.markerOption);
        this.mSensorHelper.setCurrentMarker(this.mLocMarker);
        this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MyApplication.getInstance().getmLocationLat(), MyApplication.getInstance().getmLocationLong()), 15.0f));
    }

    protected void onResume() {
        super.onResume();
        this.mapView.onResume();
    }

    protected void onPause() {
        super.onPause();
        if (this.mSensorHelper != null) {
            this.mSensorHelper.unRegisterSensorListener();
            this.mSensorHelper.setCurrentMarker(null);
            this.mSensorHelper = null;
        }
        this.mapView.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mapView.onSaveInstanceState(outState);
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mapView.onDestroy();
    }

    private void changeCamera(CameraUpdate update) {
        this.aMap.moveCamera(update);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode != 0) {
            this.mAddress = data.getStringExtra(EXTRAS_ADDRESS);
            this.latlng = (LatLng) data.getParcelableExtra(EXTRAS_LOC);
            this.mTxtLocText.setText(this.mAddress);
            this.isReturn = true;
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(this.latlng, 18.0f, 30.0f, 30.0f)));
        } else if (data != null) {
            this.city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            this.mTxtSelectCity.setText(this.city);
        }
    }

    public void jumpPoint() {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = this.aMap.getProjection();
        Point markerPoint = proj.toScreenLocation(this.latlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            public void run() {
                float t = interpolator.getInterpolation(((float) (SystemClock.uptimeMillis() - start)) / 1500.0f);
                double lng = (((double) t) * LocationMapActivity.this.latlng.longitude) + (((double) (1.0f - t)) * startLatLng.longitude);
                double lat = (((double) t) * LocationMapActivity.this.latlng.latitude) + (((double) (1.0f - t)) * startLatLng.latitude);
                if (((double) t) < 1.0d) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    public void onCameraChange(CameraPosition cameraPosition) {
        if (!this.isReturn) {
            this.mTxtLocText.setText("正在获取地点...");
        }
    }

    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        this.latlng = cameraPosition.target;
        getAddressName(new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));
    }

    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode != 1000) {
        } else if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
            if (this.isReturn) {
                this.isReturn = false;
                return;
            }
            this.mAddress = result.getRegeocodeAddress().getFormatAddress() + "附近";
            this.mAddress = this.mAddress.split(result.getRegeocodeAddress().getProvince() + result.getRegeocodeAddress().getCity() + result.getRegeocodeAddress().getDistrict())[1];
            this.mTxtLocText.setText(this.mAddress);
        }
    }

    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
    }

    private void getAddressName(final LatLonPoint latLonPoint) {
        new Thread(new Runnable() {
            public void run() {
                LocationMapActivity.this.geocoderSearch.getFromLocationAsyn(new RegeocodeQuery(latLonPoint, 10.0f, GeocodeSearch.AMAP));
            }
        }).start();
    }

    private void queryTips(String newText) {
        InputtipsQuery inputquery = new InputtipsQuery(newText, this.city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips((Context) this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    public void onGetInputtips(List<Tip> list, int i) {
        if (list != null) {
            this.mAddress = ((Tip) list.get(0)).getName();
            this.mTxtLocText.setText(this.mAddress);
        }
    }

    protected void permissionHasGranted(String permission) {
    }
}
