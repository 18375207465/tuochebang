package com.tuochebang.user.ui.request;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.tuochebang.user.R;
import com.tuochebang.user.base.BaseActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectLocationActivity extends BaseActivity implements TextWatcher, InputtipsListener {
    public static final String EXTRAS_TYPE = "extras_type";
    private SimpleAdapter aAdapter;
    private LatLng latlng;
    private String mAddress;
    private String mCity;
    private AutoCompleteTextView mInputText;
    private ListView mLvInput;
    private LatLonPoint mPoint;
    private Toolbar mToolBar;
    private int mType = 0;

    /* renamed from: com.tuochebang.user.ui.request.SelectLocationActivity$1 */
    class C09871 implements OnItemClickListener {
        C09871() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Map<String, Object> mMap = (Map) SelectLocationActivity.this.aAdapter.getItem(position);
            SelectLocationActivity.this.mPoint = (LatLonPoint) mMap.get("point");
            SelectLocationActivity.this.mAddress = ((String) mMap.get("address")) + ((String) mMap.get("name"));
            SelectLocationActivity.this.latlng = new LatLng(SelectLocationActivity.this.mPoint.getLatitude(), SelectLocationActivity.this.mPoint.getLongitude());
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt("extras_type", SelectLocationActivity.this.mType);
            bundle.putString(LocationMapActivity.EXTRAS_ADDRESS, SelectLocationActivity.this.mAddress);
            bundle.putParcelable("latlng", SelectLocationActivity.this.latlng);
            intent.putExtras(bundle);
            SelectLocationActivity.this.setResult(4, intent);
            SelectLocationActivity.this.hideSoftInput(SelectLocationActivity.this.mInputText);
            SelectLocationActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.SelectLocationActivity$2 */
    class C09882 implements OnClickListener {
        C09882() {
        }

        public void onClick(View v) {
            SelectLocationActivity.this.onBtnBack();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        getIntentExtras();
        initToolBar();
        initView();
    }

    private void getIntentExtras() {
        this.mType = getIntent().getIntExtra("extras_type", 0);
        this.mCity = getIntent().getStringExtra(DistrictSearchQuery.KEYWORDS_CITY);
    }

    private void initView() {
        this.mLvInput = (ListView) findViewById(R.id.inputlist);
        this.mInputText = (AutoCompleteTextView) findViewById(R.id.input_edit);
        this.mInputText.addTextChangedListener(this);
        queryTips(this.mCity);
        this.mLvInput.setOnItemClickListener(new C09871());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C09882());
    }

    private void onBtnBack() {
    }

    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {
            List<HashMap<String, Object>> listString = new ArrayList();
            for (int i = 1; i < tipList.size(); i++) {
                HashMap<String, Object> map = new HashMap();
                map.put("name", ((Tip) tipList.get(i)).getName());
                map.put("address", ((Tip) tipList.get(i)).getDistrict());
                map.put("point", ((Tip) tipList.get(i)).getPoint());
                listString.add(map);
            }
            this.aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.adapter_view_location_tip, new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
            this.mLvInput.setAdapter(this.aAdapter);
            this.aAdapter.notifyDataSetChanged();
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        queryTips(s.toString().trim());
    }

    public void afterTextChanged(Editable s) {
    }

    private void queryTips(String newText) {
        InputtipsQuery inputquery = new InputtipsQuery(newText, this.mCity);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips((Context) this, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }
}
