package com.tuochebang.user.view.citypicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.district.DistrictSearchQuery;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.ui.request.LocationMapActivity;
import com.tuochebang.user.ui.request.SelectLocationActivity;
import com.tuochebang.user.view.citypicker.adapter.CityListAdapter;
import com.tuochebang.user.view.citypicker.adapter.CityListAdapter.OnCityClickListener;
import com.tuochebang.user.view.citypicker.adapter.ResultListAdapter;
import com.tuochebang.user.view.citypicker.db.DBManager;
import com.tuochebang.user.view.citypicker.model.City;
import com.tuochebang.user.view.citypicker.model.LocateState;
import com.tuochebang.user.view.citypicker.utils.StringUtils;
import com.tuochebang.user.view.citypicker.view.SideLetterBar;
import com.tuochebang.user.view.citypicker.view.SideLetterBar.OnLetterChangedListener;

import java.util.List;

/**
 * 地图搜索选择
 */
public class CityPickerActivity extends AppCompatActivity implements OnClickListener {
    public static final String KEY_PICKED_CITY = "picked_city";
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    private ImageView backBtn;
    private ImageView clearBtn;
    private DBManager dbManager;
    private ViewGroup emptyView;
    private List<City> mAllCities;
    private CityListAdapter mCityAdapter;
    private SideLetterBar mLetterBar;
    private ListView mListView;
    private AMapLocationClient mLocationClient;
    private ResultListAdapter mResultAdapter;
    private ListView mResultListView;
    private int mType;
    private EditText searchBox;

    /* renamed from: com.tuochebang.user.view.citypicker.CityPickerActivity$1 */
    class C10331 implements AMapLocationListener {
        C10331() {
        }

        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null) {
                return;
            }
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                String district = aMapLocation.getDistrict();
                Log.e("onLocationChanged", "city: " + city);
                Log.e("onLocationChanged", "district: " + district);
                CityPickerActivity.this.mCityAdapter.updateLocateState(LocateState.SUCCESS, StringUtils.extractLocation(city, district));
                return;
            }
            CityPickerActivity.this.mCityAdapter.updateLocateState(LocateState.FAILED, null);
        }
    }

    /* renamed from: com.tuochebang.user.view.citypicker.CityPickerActivity$2 */
    class C10342 implements OnCityClickListener {
        C10342() {
        }

        public void onCityClick(String name) {
            CityPickerActivity.this.back(name);
        }

        public void onLocateClick() {
            Log.e("onLocateClick", "重新定位...");
            CityPickerActivity.this.mCityAdapter.updateLocateState(111, null);
            CityPickerActivity.this.mLocationClient.startLocation();
        }
    }

    /* renamed from: com.tuochebang.user.view.citypicker.CityPickerActivity$3 */
    class C10353 implements OnLetterChangedListener {
        C10353() {
        }

        public void onLetterChanged(String letter) {
            CityPickerActivity.this.mListView.setSelection(CityPickerActivity.this.mCityAdapter.getLetterPosition(letter));
        }
    }

    /* renamed from: com.tuochebang.user.view.citypicker.CityPickerActivity$4 */
    class C10364 implements TextWatcher {
        C10364() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            String keyword = s.toString();
            if (TextUtils.isEmpty(keyword)) {
                CityPickerActivity.this.clearBtn.setVisibility(View.GONE);
                CityPickerActivity.this.emptyView.setVisibility(View.GONE);
                CityPickerActivity.this.mResultListView.setVisibility(View.GONE);
                return;
            }
            CityPickerActivity.this.clearBtn.setVisibility(View.VISIBLE);
            CityPickerActivity.this.mResultListView.setVisibility(View.VISIBLE);
            List<City> result = CityPickerActivity.this.dbManager.searchCity(keyword);
            if (result == null || result.size() == 0) {
                CityPickerActivity.this.emptyView.setVisibility(View.VISIBLE);
                return;
            }
            CityPickerActivity.this.emptyView.setVisibility(View.GONE);
            CityPickerActivity.this.mResultAdapter.changeData(result);
        }
    }

    /* renamed from: com.tuochebang.user.view.citypicker.CityPickerActivity$5 */
    class C10375 implements OnItemClickListener {
        C10375() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            CityPickerActivity.this.back(CityPickerActivity.this.mResultAdapter.getItem(position).getName());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);
        getIntentExtras();
        initData();
        initView();
        initLocation();
    }

    private void getIntentExtras() {
        this.mType = getIntent().getIntExtra("extras_type", 0);
    }

    private void initLocation() {
        this.mLocationClient = new AMapLocationClient(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        this.mLocationClient.setLocationOption(option);
        this.mLocationClient.setLocationListener(new C10331());
        this.mLocationClient.startLocation();
    }

    private void initData() {
        this.dbManager = new DBManager(this);
        this.dbManager.copyDBFile();
        this.mAllCities = this.dbManager.getAllCities();
        this.mCityAdapter = new CityListAdapter(this, this.mAllCities);
        this.mCityAdapter.setOnCityClickListener(new C10342());
        this.mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        this.mListView = (ListView) findViewById(R.id.listview_all_city);
        this.mListView.setAdapter(this.mCityAdapter);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        this.mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        this.mLetterBar.setOverlay(overlay);
        this.mLetterBar.setOnLetterChangedListener(new C10353());
        this.searchBox = (EditText) findViewById(R.id.et_search);
        this.searchBox.addTextChangedListener(new C10364());
        this.emptyView = (ViewGroup) findViewById(R.id.empty_view);
        this.mResultListView = (ListView) findViewById(R.id.listview_search_result);
        this.mResultListView.setAdapter(this.mResultAdapter);
        this.mResultListView.setOnItemClickListener(new C10375());
        this.clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        this.backBtn = (ImageView) findViewById(R.id.back);
        this.clearBtn.setOnClickListener(this);
        this.backBtn.setOnClickListener(this);
    }

    private void back(String city) {
        Bundle bundle = new Bundle();
        bundle.putString(DistrictSearchQuery.KEYWORDS_CITY, city);
        bundle.putInt("extras_type", this.mType);
        ActivityUtil.next(this, SelectLocationActivity.class, bundle, false, 100);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                return;
            case R.id.iv_search_clear:
                this.searchBox.setText("");
                this.clearBtn.setVisibility(8);
                this.emptyView.setVisibility(8);
                this.mResultListView.setVisibility(8);
                return;
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 4) {
            String value = data.getStringExtra(LocationMapActivity.EXTRAS_ADDRESS);
            int type = data.getIntExtra("extras_type", 0);
            LatLng latLng = (LatLng) data.getParcelableExtra("latlng");
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("extras_type", type);
            bundle.putString(LocationMapActivity.EXTRAS_ADDRESS, value);
            bundle.putParcelable("latlng", latLng);
            intent.putExtras(bundle);
            setResult(4, intent);
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mLocationClient.stopLocation();
    }
}
