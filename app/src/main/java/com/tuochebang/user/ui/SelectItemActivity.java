package com.tuochebang.user.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.FilterTypeListAdapter;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.request.entity.ModelType;
import com.tuochebang.user.ui.request.PublishRequestActivity;
import java.util.ArrayList;

public class SelectItemActivity extends BaseActivity {
    public static String ACCOUNT_TYPE = "账户类型";
    public static String BIANSU_TYPE = "变速箱";
    public static String CAR_TYPE = "车型";
    public static String EXTRAS_CHILDTYPE = "extras_type";
    public static String EXTRAS_LIST_DATA = "extras_list_data";
    public static String EXTRAS_SELECT_NAME = "extras_select_name";
    public static String PAY_TYPE = "支付";
    public static String QUDONG_TYPE = "驱动";
    public static String REQUEST_TYPE = "请求类型";
    public static int SELECT_ITEM_CODE = 1;
    private FilterTypeListAdapter mAdapter;
    private ArrayList<ModelType> mChilds;
    private ListView mListView;
    private ModelType mSelectName;
    private TextView mTxtSelectTitle;
    private TextView mTxtSure;
    private String mType;

    /* renamed from: com.tuochebang.user.ui.SelectItemActivity$1 */
    class C09371 implements OnItemClickListener {
        C09371() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            ModelType item = (ModelType) SelectItemActivity.this.mAdapter.getItem(position);
            SelectItemActivity.this.mSelectName = item;
            SelectItemActivity.this.mAdapter.setSelectFilterName(item);
            SelectItemActivity.this.mTxtSure.setVisibility(View.VISIBLE);
        }
    }

    /* renamed from: com.tuochebang.user.ui.SelectItemActivity$2 */
    class C09382 implements OnClickListener {
        C09382() {
        }

        public void onClick(View v) {
            SelectItemActivity.this.onBtnSureClick();
        }
    }

    /* renamed from: com.tuochebang.user.ui.SelectItemActivity$3 */
    class C09393 implements OnClickListener {
        C09393() {
        }

        public void onClick(View v) {
            SelectItemActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.SelectItemActivity$4 */
    class C09404 implements OnClickListener {
        C09404() {
        }

        public void onClick(View v) {
            SelectItemActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivty_select_item);
        getIntentExtras();
        initView();
    }

    private void getIntentExtras() {
        this.mChilds = (ArrayList) getIntent().getSerializableExtra(EXTRAS_LIST_DATA);
        this.mType = getIntent().getStringExtra(EXTRAS_CHILDTYPE);
    }

    private void initView() {
        this.mListView = (ListView) findViewById(R.id.lv_list);
        this.mAdapter = new FilterTypeListAdapter(this.mContext);
        this.mAdapter.setType(this.mType);
        this.mListView.setAdapter(this.mAdapter);
        if (this.mChilds != null) {
            this.mAdapter.setList(this.mChilds);
        }
        this.mListView.setOnItemClickListener(new C09371());
        this.mTxtSure = (TextView) findViewById(R.id.tcb_select_sure_txt);
        this.mTxtSure.setOnClickListener(new C09382());
        this.mTxtSelectTitle = (TextView) findViewById(R.id.tcb_select_title);
        this.mTxtSelectTitle.setText(this.mType);
        findViewById(R.id.tcb_close_img).setOnClickListener(new C09393());
        findViewById(R.id.ll_container).setOnClickListener(new C09404());
    }

    private void onBtnSureClick() {
        Intent intent = new Intent();
        intent.setClass(this, PublishRequestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRAS_SELECT_NAME, this.mSelectName);
        bundle.putString(EXTRAS_CHILDTYPE, this.mType);
        intent.putExtras(bundle);
        setResult(2, intent);
        finish();
    }
}
