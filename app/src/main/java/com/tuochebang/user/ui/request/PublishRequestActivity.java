package com.tuochebang.user.ui.request;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.framework.app.component.optimize.GridViewForScrollView;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.DateUtil;
import com.framework.app.component.utils.ToastUtil;
import com.framework.app.component.widget.DataLoadingView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.SelectPicAdapter;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.constant.AppConstant;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.ModelType;
import com.tuochebang.user.request.entity.TuoCheRequestModel;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.tuochebang.user.request.task.EditTuocheRequest;
import com.tuochebang.user.request.task.GetCarTypeRequest;
import com.tuochebang.user.request.task.GetPayAndSortMethodRequest;
import com.tuochebang.user.request.task.SubmitTuocheRequest;
import com.tuochebang.user.ui.CommonEditActivity;
import com.tuochebang.user.ui.ImageSimpleBrowseActivity;
import com.tuochebang.user.ui.SelectItemActivity;
import com.tuochebang.user.ui.user.UserRequestActivity;
import com.tuochebang.user.util.ChString;
import com.tuochebang.user.util.NAImageUtils;
import com.tuochebang.user.util.PositionEntity;
import com.tuochebang.user.util.RouteTask;
import com.tuochebang.user.util.RouteTask.OnRouteCalculateListener;
import com.tuochebang.user.view.CommonSelectDailog;
import com.tuochebang.user.view.CommonSelectDailog.OnItemClickedListener;
import com.tuochebang.user.view.citypicker.CityPickerActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.yalantis.ucrop.view.CropImageView;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PublishRequestActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, OnRouteCalculateListener {
    public static final String EXTRAS_REQUEST_INFO = "extras_request_info";
    public static final String EXTRAS_REQUEST_TYPE = "extras_request_type";
    public static int TYPE_EDIT = 1;
    public static int TYPE_SUBIT = 0;
    public static List<LocalMedia> selectPics = new ArrayList();
    public static int urlCount = 0;
    public static String[] urlUploads = new String[4];
    private SelectPicAdapter adapter;
    private String date;
    private List<ModelType> driveTypes = new ArrayList();
    private List<ModelType> gearboxTypes = new ArrayList();
    private Handler handler = new Handler(new C09701());
    private Integer mBiansuId = Integer.valueOf(0);
    private Integer mCarTypeId = Integer.valueOf(0);
    private CheckBox mCbIsCrane;
    private CheckBox mCbIsReturn;
    private DataLoadingView mDataLoadingView;
    private EditText mEdHeight;
    private EditText mEdLong;
    private EditText mEdWidhth;
    private EditText mEdZizhong;
    private LatLng mEndLatlng;
    private LatLng mLatlng;
    private LinearLayout mLlOthers;
    private Integer mPayId = Integer.valueOf(0);
    private Integer mQudongId = Integer.valueOf(0);
    private TuocheRequestInfo mRequestInfo;
    private int mRequestType;
    private RelativeLayout mRlAccount;
    private RelativeLayout mRlBiansu;
    private RelativeLayout mRlCarInfo;
    private RelativeLayout mRlCarType;
    private RelativeLayout mRlFromLoc;
    private RelativeLayout mRlPay;
    private RelativeLayout mRlPhone;
    private RelativeLayout mRlQudong;
    private RelativeLayout mRlSort;
    private RelativeLayout mRlTime;
    private RelativeLayout mRlToLoc;
    private CommonSelectDailog mShowSelectDialog;
    private Integer mSortId = Integer.valueOf(0);
    private Toolbar mToolBar;
    private TextView mTxtAccount;
    private TextView mTxtBiansu;
    private TextView mTxtCarName;
    private TextView mTxtCarType;
    private TextView mTxtDistance;
    private TextView mTxtLocFrom;
    private TextView mTxtLocTo;
    private TextView mTxtPayType;
    private TextView mTxtPhone;
    private TextView mTxtPrice;
    private TextView mTxtQudong;
    private TextView mTxtSave;
    private TextView mTxtSort;
    private TextView mTxtTime;
    private int mTypeLoc;
    private List<ModelType> modelTypes = new ArrayList();
    private GridViewForScrollView noScrollgridview;
    private double orignPrice;
    private List<ModelType> payList = new ArrayList();
    double resultMoney;
    private List<ModelType> sortList = new ArrayList();
    private int uploadNums;
    private boolean uploadSuccess;

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        this.mTxtTime.setText(this.date + " " + hourOfDay + ":" + minute + ":" + second);

    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$1 */
    class C09701 implements Callback {
        C09701() {
        }

        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                PublishRequestActivity.this.mDataLoadingView.showDataLoadSuccess();
            } else {
                PublishRequestActivity.this.dismissCommonProgressDialog();
                PublishRequestActivity.this.adapter.notifyDataSetChanged();
            }
            return false;
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$2 */
    class C09712 implements OnClickListener {
        C09712() {
        }

        public void onClick(View v) {
            PublishRequestActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$3 */
    class C09723 implements Observer<Boolean> {
        C09723() {
        }

        public void onSubscribe(Disposable d) {
        }

        public void onNext(Boolean aBoolean) {
            if (aBoolean.booleanValue()) {
                PictureFileUtils.deleteCacheDirFile(PublishRequestActivity.this);
                PublishRequestActivity.this.onBtnSelectClick();
                return;
            }
            Toast.makeText(PublishRequestActivity.this, PublishRequestActivity.this.getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
        }

        public void onError(Throwable e) {
        }

        public void onComplete() {
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$4 */
    class C09734 implements OnItemClickedListener {
        C09734() {
        }

        public void onItemClick(int id, String data) {
            if (id == 0) {
                PictureSelector.create(PublishRequestActivity.this).openGallery(PictureMimeType.ofImage()).theme(R.style.picture_default_style).maxSelectNum(4 - PublishRequestActivity.urlCount).minSelectNum(1).imageSpanCount(4).selectionMode(2).previewImage(true).isCamera(true).enableCrop(true).compress(true).forResult(PictureConfig.CHOOSE_REQUEST);
            } else {
                PictureSelector.create(PublishRequestActivity.this).openCamera(PictureMimeType.ofImage()).theme(R.style.picture_default_style).maxSelectNum(1).minSelectNum(1).selectionMode(1).previewImage(true).enableCrop(true).compress(true).isCamera(true).forResult(PictureConfig.CHOOSE_REQUEST);
            }
            PublishRequestActivity.this.mShowSelectDialog.dismiss();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$5 */
    class C09745 implements OnItemClickListener {
        C09745() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
            if (arg2 == PublishRequestActivity.urlCount) {
                PublishRequestActivity.this.requestWritePermission();
                return;
            }
            Bundle bundle = new Bundle();
            ArrayList<String> urls = new ArrayList();
            for (String url : PublishRequestActivity.urlUploads) {
                if (!TextUtils.isEmpty(url)) {
                    urls.add(url);
                }
            }
            bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, urls);
            bundle.putInt("position", arg2);
            ActivityUtil.next((Activity) PublishRequestActivity.this.mContext, ImageSimpleBrowseActivity.class, bundle, false, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$6 */
    class C09756 implements OnClickListener {
        C09756() {
        }

        public void onClick(View v) {
            PublishRequestActivity.this.onBtnQudongClick();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$7 */
    class C09767 implements OnClickListener {
        C09767() {
        }

        public void onClick(View v) {
            PublishRequestActivity.this.onBtnCarTypeClick();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$8 */
    class C09778 implements OnClickListener {
        C09778() {
        }

        public void onClick(View v) {
            PublishRequestActivity.this.onBtnBiansuClick();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.PublishRequestActivity$9 */
    class C09789 implements OnClickListener {
        C09789() {
        }

        public void onClick(View v) {
            PublishRequestActivity.this.onBtnPayClick();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        selectPics.clear();
        urlCount = 0;
        RouteTask.getInstance(getApplicationContext()).removeRouteCalculateListener(this);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pullcar_request);
        getIntentExtras();
        initToolBar();
        initView();
        initListener();
        initData();
    }

    private void getIntentExtras() {
        this.mRequestType = getIntent().getIntExtra(EXTRAS_REQUEST_TYPE, 0);
        this.mRequestInfo = (TuocheRequestInfo) getIntent().getSerializableExtra(EXTRAS_REQUEST_INFO);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C09712());
    }

    private void requestWritePermission() {
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, new C09723());
    }

    private void onBtnSelectClick() {
        if (this.mShowSelectDialog == null) {
            List<String> list = new ArrayList();
            list.add(getString(R.string.txt_select_from_album));
            list.add(getString(R.string.txt_camera));
            this.mShowSelectDialog = new CommonSelectDailog(this.mContext, list);
            this.mShowSelectDialog.setMessage(getString(R.string.txt_upload_message));
            this.mShowSelectDialog.setOnItemClickedListener(new C09734());
            return;
        }
        this.mShowSelectDialog.show();
    }

    private void initListener() {
        this.noScrollgridview.setOnItemClickListener(new C09745());
        this.mRlQudong.setOnClickListener(new C09756());
        this.mRlCarType.setOnClickListener(new C09767());
        this.mRlBiansu.setOnClickListener(new C09778());
        this.mRlPay.setOnClickListener(new C09789());
        this.mRlAccount.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.onBtnAccount();
            }
        });
        this.mRlSort.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.onBtnSort();
            }
        });
        this.mRlPhone.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.onBtnPhone();
            }
        });
        this.mRlFromLoc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.mTypeLoc = 0;
                PublishRequestActivity.this.requestLocationPermission();
            }
        });
        this.mRlToLoc.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.mTypeLoc = 1;
                PublishRequestActivity.this.requestLocationPermission();
            }
        });
        this.mRlTime.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.showDatePickDialog();
            }
        });
        this.mTxtSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.onBtnSubmit();
            }
        });
        this.mRlCarInfo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PublishRequestActivity.this.onBtnCarInfo();
            }
        });
        this.mCbIsReturn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PublishRequestActivity.this.resultMoney -= PublishRequestActivity.this.resultMoney * 0.4000000059604645d;
                    PublishRequestActivity.this.mTxtPrice.setText("¥" + new DecimalFormat("##0.0").format(PublishRequestActivity.this.resultMoney));
                    return;
                }
                PublishRequestActivity.this.resultMoney = PublishRequestActivity.this.orignPrice;
                PublishRequestActivity.this.mTxtPrice.setText("¥" + new DecimalFormat("##0.0").format(PublishRequestActivity.this.resultMoney));
            }
        });
    }

    private String getTime(Date date) {
        return new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS).format(date);
    }

    private void showDatePickDialog() {
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            public void onTimeSelect(Date date, View v) {
                PublishRequestActivity.this.mTxtTime.setText(PublishRequestActivity.this.getTime(date));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void requestLocationPermission() {
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, new Observer<Boolean>() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(Boolean aBoolean) {
                if (aBoolean.booleanValue()) {
                    PublishRequestActivity.this.onBtnSelectLocation();
                } else {
                    Toast.makeText(PublishRequestActivity.this, "请开启定位权限", Toast.LENGTH_SHORT).show();
                }
            }

            public void onError(Throwable e) {
            }

            public void onComplete() {
            }
        });
    }

    private void onBtnSelectLocation() {
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setClass(this, CityPickerActivity.class);
        bundle.putInt("extras_type", this.mTypeLoc);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnPhone() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "number");
        bundle.putString(CommonEditActivity.EXTRAS_TITLE, "电话");
        bundle.putString(CommonEditActivity.EXTRAS_TYPE, "phone");
        Intent intent = new Intent();
        intent.setClass(this, CommonEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnCarInfo() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
        bundle.putString(CommonEditActivity.EXTRAS_TITLE, "车辆信息");
        bundle.putString(CommonEditActivity.EXTRAS_TYPE, "carInfo");
        Intent intent = new Intent();
        intent.setClass(this, CommonEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnAccount() {
        Bundle bundle = new Bundle();
        bundle.putString(CommonEditActivity.EXTRAS_INPUT_TYPE, "text");
        bundle.putString(CommonEditActivity.EXTRAS_TITLE, "公司名称");
        bundle.putString(CommonEditActivity.EXTRAS_TYPE, "companyName");
        Intent intent = new Intent();
        intent.setClass(this, CommonEditActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 100);
    }

    private void onBtnSort() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.REQUEST_TYPE);
        bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) this.sortList);
        Intent intent = new Intent();
        intent.setClass(this, SelectItemActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
    }

    private void onBtnQudongClick() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.QUDONG_TYPE);
        bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) this.driveTypes);
        Intent intent = new Intent();
        intent.setClass(this, SelectItemActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
    }

    private void onBtnPayClick() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.PAY_TYPE);
        bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) this.payList);
        Intent intent = new Intent();
        intent.setClass(this, SelectItemActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
    }

    private void onBtnBiansuClick() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.BIANSU_TYPE);
        bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) this.gearboxTypes);
        Intent intent = new Intent();
        intent.setClass(this, SelectItemActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
    }

    private void onBtnCarTypeClick() {
        Bundle bundle = new Bundle();
        bundle.putString(SelectItemActivity.EXTRAS_CHILDTYPE, SelectItemActivity.CAR_TYPE);
        bundle.putSerializable(SelectItemActivity.EXTRAS_LIST_DATA, (Serializable) this.modelTypes);
        Intent intent = new Intent();
        intent.setClass(this, SelectItemActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, SelectItemActivity.SELECT_ITEM_CODE);
    }

    private void initData() {
        httpGetConfig();
    }

    private void initView() {
        RouteTask.getInstance(getApplicationContext()).addRouteCalculateListener(this);
        this.mDataLoadingView = (DataLoadingView) findViewById(R.id.data_loadingview);
        this.noScrollgridview = (GridViewForScrollView) findViewById(R.id.noScrollgridview);
        this.noScrollgridview.setSelector(new ColorDrawable(0));
        this.adapter = new SelectPicAdapter(this);
        this.noScrollgridview.setAdapter(this.adapter);
        this.mRlQudong = (RelativeLayout) findViewById(R.id.tcb_qudong_rl);
        this.mRlBiansu = (RelativeLayout) findViewById(R.id.tcb_biansu_rl);
        this.mRlCarType = (RelativeLayout) findViewById(R.id.tcb_car_type_rl);
        this.mRlPay = (RelativeLayout) findViewById(R.id.tcb_pay_rl);
        this.mTxtQudong = (TextView) findViewById(R.id.tcb_qudong_txt);
        this.mTxtBiansu = (TextView) findViewById(R.id.tcb_biansu_txt);
        this.mTxtCarType = (TextView) findViewById(R.id.tcb_car_type_txt);
        this.mTxtPayType = (TextView) findViewById(R.id.tcb_request_pay_txt);
        this.mRlAccount = (RelativeLayout) findViewById(R.id.tcb_request_account_rl);
        this.mRlPhone = (RelativeLayout) findViewById(R.id.tcb_request_phone_rl);
        this.mRlTime = (RelativeLayout) findViewById(R.id.tcb_request_time_rl);
        this.mRlFromLoc = (RelativeLayout) findViewById(R.id.tcb_request_from_loc_rl);
        this.mRlToLoc = (RelativeLayout) findViewById(R.id.tcb_request_to_loc_rl);
        this.mRlSort = (RelativeLayout) findViewById(R.id.tcb_request_sort_rl);
        this.mRlCarInfo = (RelativeLayout) findViewById(R.id.tcb_car_info_rl);
        this.mTxtPhone = (TextView) findViewById(R.id.tcb_request_phone_txt);
        this.mTxtAccount = (TextView) findViewById(R.id.tcb_request_account_txt);
        this.mTxtTime = (TextView) findViewById(R.id.tcb_request_time_txt);
        this.mTxtLocTo = (TextView) findViewById(R.id.tcb_request_to_loc_txt);
        this.mTxtLocFrom = (TextView) findViewById(R.id.tcb_request_from_loc_txt);
        this.mTxtSort = (TextView) findViewById(R.id.tcb_request_sort_txt);
        this.mTxtCarName = (TextView) findViewById(R.id.tcb_request_carname_txt);
        this.mTxtDistance = (TextView) findViewById(R.id.txt_distance);
        this.mTxtPrice = (TextView) findViewById(R.id.txt_price);
        this.mLlOthers = (LinearLayout) findViewById(R.id.ll_tcb_others);
        this.mEdZizhong = (EditText) findViewById(R.id.tcb_zizhong_txt);
        this.mEdLong = (EditText) findViewById(R.id.tcb_long_txt);
        this.mEdWidhth = (EditText) findViewById(R.id.tcb_width_txt);
        this.mEdHeight = (EditText) findViewById(R.id.tcb_height_txt);
        this.mCbIsReturn = (CheckBox) findViewById(R.id.tcb_is_return_cb);
        this.mCbIsCrane = (CheckBox) findViewById(R.id.tcb_is_crane);
        this.mTxtSave = (TextView) findViewById(R.id.tcb_save_txt);
        if (this.mRequestType == TYPE_EDIT) {
            this.mTxtSave.setText("修改");
            refreshView();
        } else {
            this.mTxtAccount.setText(MyApplication.getInstance().getUserInfo().getCorporate());
            this.mTxtPhone.setText(MyApplication.getInstance().getUserInfo().getMobile());
            this.mTxtTime.setText(new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss").format(new Date(System.currentTimeMillis())));
            this.mTxtSave.setText("提交");
        }
        this.mDataLoadingView.showDataLoading();
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (!(PublishRequestActivity.this.payList.size() == 0 || PublishRequestActivity.this.sortList.size() == 0 || PublishRequestActivity.this.modelTypes.size() == 0)) {
                        if (((PublishRequestActivity.this.driveTypes.size() != 0 ? 1 : 0) & (PublishRequestActivity.this.gearboxTypes.size() != 0 ? 1 : 0)) != 0) {
                            PublishRequestActivity.this.handler.sendEmptyMessage(1);
                            return;
                        }
                    }
                }
            }
        }).start();
    }

    private void refreshView() {
        this.mTxtAccount.setText(this.mRequestInfo.getCorporate());
        this.mTxtPhone.setText(this.mRequestInfo.getMobile());
        this.mTxtTime.setText(this.mRequestInfo.getTime());
        this.mTxtLocFrom.setText(this.mRequestInfo.getBegin());
        this.mTxtLocTo.setText(this.mRequestInfo.getEnd());
        this.mTxtSort.setText(this.mRequestInfo.getTypeName());
        this.mTxtPayType.setText(this.mRequestInfo.getPayName());
        this.mTxtCarName.setText(this.mRequestInfo.getCar().getInfo());
        this.mTxtCarType.setText(this.mRequestInfo.getCar().getModelName());
        this.mTxtQudong.setText(this.mRequestInfo.getCar().getDriveName());
        this.mTxtBiansu.setText(this.mRequestInfo.getCar().getGearName());
        for (int i = 0; i < this.mRequestInfo.getPicture().size(); i++) {
            if (!TextUtils.isEmpty((CharSequence) this.mRequestInfo.getPicture().get(i))) {
                urlUploads[i] = (String) this.mRequestInfo.getPicture().get(i);
                urlCount++;
            }
        }
        if (this.mTxtCarType.getText().toString().equals("其他")) {
            this.mLlOthers.setVisibility(View.VISIBLE);
        } else {
            this.mLlOthers.setVisibility(View.GONE);
        }
        this.mCbIsReturn.setVisibility(View.VISIBLE);
        if (this.mCbIsReturn.isChecked()) {
            this.resultMoney = this.mRequestInfo.getMoney() - (this.mRequestInfo.getMoney() * 0.4000000059604645d);
        } else {
            this.resultMoney = this.mRequestInfo.getMoney();
            this.orignPrice = this.resultMoney;
        }
        this.mTxtPrice.setText("¥" + new DecimalFormat("##0.0").format(this.resultMoney));
        this.mTxtPrice.setVisibility(View.VISIBLE);
        this.mLatlng = new LatLng(this.mRequestInfo.getLatitude(), this.mRequestInfo.getLongitude());
        this.mEndLatlng = new LatLng(this.mRequestInfo.getE_latitude(), this.mRequestInfo.getE_longitude());
        caculateDistance();
        this.adapter.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    private void showTimePickDialog() {
        Calendar now = Calendar.getInstance();
        @SuppressLint("WrongConstant") TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, now.get(11), now.get(12), now.get(13), true);
        timePickerDialog.setThemeDark(true);
        timePickerDialog.vibrate(false);
        timePickerDialog.dismissOnPause(true);
        timePickerDialog.setAccentColor(Color.parseColor("#323334"));
        timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String value;
        if (resultCode == -1) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                if (selectList != null && selectList.size() > 0) {
                    selectPics.clear();
                    selectPics.addAll(selectList);
                    showCommonProgreessDialog("请稍后...");
                    this.uploadSuccess = false;
                    String path = "";
                    if (((LocalMedia) selectPics.get(this.uploadNums)).isCut() && !((LocalMedia) selectList.get(this.uploadNums)).isCompressed()) {
                        path = ((LocalMedia) selectPics.get(this.uploadNums)).getCutPath();
                    } else if (((LocalMedia) selectPics.get(this.uploadNums)).isCompressed() || (((LocalMedia) selectList.get(this.uploadNums)).isCut() && ((LocalMedia) selectList.get(this.uploadNums)).isCompressed())) {
                        path = ((LocalMedia) selectPics.get(this.uploadNums)).getCompressPath();
                    } else {
                        path = ((LocalMedia) selectList.get(this.uploadNums)).getPath();
                    }
                    uploadImage(path);
                    new Thread(new Runnable() {
                        public void run() {
                            do {
                            } while (!PublishRequestActivity.this.uploadSuccess);
                            PublishRequestActivity.this.handler.sendEmptyMessage(0);
                        }
                    }).start();
                }
            }
        } else if (resultCode == 2) {
            if (requestCode == SelectItemActivity.SELECT_ITEM_CODE) {
                String childType = data.getStringExtra(SelectItemActivity.EXTRAS_CHILDTYPE);
                ModelType selectModel = (ModelType) data.getSerializableExtra(SelectItemActivity.EXTRAS_SELECT_NAME);
                if (childType.equals(SelectItemActivity.BIANSU_TYPE)) {
                    this.mTxtBiansu.setText(selectModel.getTypeName());
                    this.mBiansuId = Integer.valueOf(selectModel.getTypeId());
                } else if (childType.equals(SelectItemActivity.QUDONG_TYPE)) {
                    this.mTxtQudong.setText(selectModel.getTypeName());
                    this.mQudongId = Integer.valueOf(selectModel.getTypeId());
                } else if (childType.equals(SelectItemActivity.CAR_TYPE)) {
                    this.mTxtCarType.setText(selectModel.getTypeName());
                    this.mCarTypeId = Integer.valueOf(selectModel.getTypeId());
                    if (selectModel.getTypeName().equals("其他")) {
                        this.mLlOthers.setVisibility(View.VISIBLE);
                    } else {
                        this.mLlOthers.setVisibility(View.GONE);
                    }
                } else if (childType.equals(SelectItemActivity.PAY_TYPE)) {
                    this.mTxtPayType.setText(selectModel.getTypeName());
                    this.mPayId = Integer.valueOf(selectModel.getTypeId());
                } else if (childType.equals(SelectItemActivity.ACCOUNT_TYPE)) {
                    this.mTxtAccount.setText(selectModel.getTypeName());
                } else if (childType.equals(SelectItemActivity.REQUEST_TYPE)) {
                    this.mTxtSort.setText(selectModel.getTypeName());
                    this.mSortId = Integer.valueOf(selectModel.getTypeId());
                }
            }
        } else if (resultCode == 3) {
            if (requestCode == 100) {
                value = data.getStringExtra(CommonEditActivity.EXTRAS_VALUE);
                String type = data.getStringExtra(CommonEditActivity.EXTRAS_TYPE);
                if (type.equals("phone")) {
                    this.mTxtPhone.setText(value);
                } else if (type.equals("companyName")) {
                    this.mTxtAccount.setText(value);
                } else if (type.equals("carInfo")) {
                    this.mTxtCarName.setText(value);
                }
            }
        } else if (resultCode == 4) {
            value = data.getStringExtra(LocationMapActivity.EXTRAS_ADDRESS);
            int type2 = data.getIntExtra("extras_type", 0);
            LatLng latLng = (LatLng) data.getParcelableExtra("latlng");
            if (type2 == 0) {
                this.mTxtLocFrom.setText(value);
                this.mLatlng = latLng;
            } else if (type2 == 1) {
                this.mTxtLocTo.setText(value);
                this.mEndLatlng = latLng;
            }
            caculateDistance();
        } else if (resultCode == 8) {
            urlUploads[data.getIntExtra("position", 0)] = "";
            urlCount--;
            this.adapter.notifyDataSetChanged();
        }
    }

    private void caculateDistance() {
        if (this.mLatlng != null && this.mEndLatlng != null) {
            float v = AMapUtils.calculateLineDistance(this.mLatlng, this.mEndLatlng);
            RouteTask.getInstance(this).setStartPoint(new PositionEntity(this.mLatlng.latitude, this.mLatlng.longitude, "", ""));
            RouteTask.getInstance(this).setEndPoint(new PositionEntity(this.mEndLatlng.latitude, this.mEndLatlng.longitude, "", ""));
            RouteTask.getInstance(this).search();
        }
    }

    @SuppressLint("WrongConstant")
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        this.date = year + "-" + monthOfYear + "-" + dayOfMonth;
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, year);
        calendar.set(2, monthOfYear);
        calendar.set(5, dayOfMonth);
        showTimePickDialog();
    }


    private void uploadImage(String filePath) {
        final String path = NAImageUtils.compressAndRotateImage(MyApplication.getInstance(), filePath);
        PutObjectRequest put = new PutObjectRequest(AppConstant.ALIYUN_OSS_BUCKET, AppConstant.ALIYUN_OSS_KEY + System.currentTimeMillis(), path);
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
            }
        });
        OSSAsyncTask task = MyApplication.getInstance().getOssClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String url =
                        MyApplication.getInstance().getOssClient()
                                .presignPublicObjectURL(AppConstant.ALIYUN_OSS_BUCKET, request.getObjectKey());
                PublishRequestActivity.urlCount++;
                PublishRequestActivity.urlUploads[PublishRequestActivity.urlCount - 1] = url;
                PublishRequestActivity.this.uploadNums = PublishRequestActivity.this.uploadNums + 1;
                if (PublishRequestActivity.this.uploadNums == PublishRequestActivity.selectPics.size()) {
                    PublishRequestActivity.this.uploadSuccess = true;
                    PublishRequestActivity.this.uploadNums = 0;
                } else {
                    PublishRequestActivity.this.uploadImage(((LocalMedia) PublishRequestActivity.selectPics.get(PublishRequestActivity.this.uploadNums)).getPath());
                }
            }

            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                }
            }
        });
    }

    private void onBtnSubmit() {
        if (TextUtils.isEmpty(this.mTxtAccount.getText().toString())) {
            showToast("请公司名称");
        } else if (TextUtils.isEmpty(this.mTxtPhone.getText().toString())) {
            showToast("请填写电话号码");
        } else if (TextUtils.isEmpty(this.mTxtTime.getText().toString())) {
            showToast("请填写时间");
        } else if (this.mTxtLocFrom.getText().toString().equals("输入起点")) {
            showToast("请填写起点位置");
        } else if (this.mTxtLocTo.getText().toString().equals("输入终点")) {
            showToast("请填写终点位置");
        } else if (TextUtils.isEmpty(this.mTxtPayType.getText().toString())) {
            showToast("请选择支付方式");
        } else if (TextUtils.isEmpty(this.mTxtSort.getText().toString())) {
            showToast("请选择故障类型");
        } else if (this.mTxtCarName.getText().toString().equals("输入车辆信息")) {
            showToast("请填写车信息");
        } else if (TextUtils.isEmpty(this.mTxtCarType.getText().toString())) {
            showToast("请选择车型");
        } else if (TextUtils.isEmpty(this.mTxtBiansu.getText().toString())) {
            showToast("请选择变速箱类型");
        } else if (TextUtils.isEmpty(this.mTxtQudong.getText().toString())) {
            showToast("请选择驱动类型");
        } else if (this.mTxtCarType.getText().toString().equals("其他") && (TextUtils.isEmpty(this.mEdZizhong.getText().toString()) || TextUtils.isEmpty(this.mEdLong.getText().toString()) || TextUtils.isEmpty(this.mEdWidhth.getText().toString()) || TextUtils.isEmpty(this.mEdHeight.getText().toString()))) {
            showToast("请将车型填写完整");
        } else {
            showCommonProgreessDialog("请稍后...");
            if (this.mRequestType == TYPE_EDIT) {
                httpEditRequest();
            } else {
                httpSubmitRequest();
            }
        }
    }

    private void httpEditRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new EditTuocheRequest(ServerUrl.getInst().EDIT_TUOCHE_REQUEST(), RequestMethod.POST, setRequestModel(), this.mRequestInfo.getStatus()), new OnResponseListener<Object>() {
            public void onSucceed(int what, Response<Object> response) {
                if (response.get() != null) {
                    ToastUtil.showMessage(MyApplication.getInstance(), "提交成功");
                    BroadCastUtil.sendBroadCast(PublishRequestActivity.this.mContext, BroadCastAction.TUOCHE_REQUEST_CHANGE);
                    PublishRequestActivity.this.finish();
                }
            }

            public void onFailed(int what, Response<Object> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                PublishRequestActivity.this.dismissCommonProgressDialog();
            }
        });

    }

    private TuoCheRequestModel setRequestModel() {
        TuoCheRequestModel model = new TuoCheRequestModel();
        model.setCorporate(this.mTxtAccount.getText().toString());
        model.setBegin(this.mTxtLocFrom.getText().toString());
        model.setMobile(this.mTxtPhone.getText().toString());
        model.setTime(this.mTxtTime.getText().toString());
        model.setEnd(this.mTxtLocTo.getText().toString());
        model.setTypeId(String.valueOf(this.mSortId));
        if (this.mCbIsCrane.isChecked()) {
            model.setIsCrane(1);
        } else {
            model.setIsCrane(0);
        }
        if (this.mTxtCarType.getText().toString().equals("其他")) {
            model.setOtherCar("自重" + this.mEdZizhong.getText().toString() + "吨 长" + this.mEdLong.getText().toString() + "米 宽" + this.mEdWidhth.getText().toString() + "米 高" + this.mEdHeight.getText().toString() + ChString.Meter);
        }
        model.setPayId(String.valueOf(this.mPayId));
        if (this.mCbIsReturn.isChecked()) {
            model.setIsReturn(1);
        } else {
            model.setIsReturn(0);
        }
        model.setMoney(this.resultMoney);
        model.setCar(this.mTxtCarName.getText().toString());
        model.setModelId(String.valueOf(this.mCarTypeId));
        model.setGearboxId(String.valueOf(this.mBiansuId));
        model.setDriveId(String.valueOf(this.mQudongId));
        model.setPicture0(urlUploads[0]);
        model.setPicture1(urlUploads[1]);
        model.setPicture2(urlUploads[2]);
        model.setPicture3(urlUploads[3]);
        model.setLatitude(MyApplication.getInstance().getmLocationLat());
        model.setLongitude(MyApplication.getInstance().getmLocationLong());
        if (this.mRequestType == TYPE_EDIT) {
            model.setRequestId(this.mRequestInfo.getRequestId());
            model.setLatitude(this.mLatlng.latitude);
            model.setLongitude(this.mLatlng.longitude);
            model.setE_latitude(this.mEndLatlng.latitude);
            model.setE_longitude(this.mEndLatlng.longitude);
        } else {
            model.setLatitude(this.mLatlng.latitude);
            model.setLongitude(this.mLatlng.longitude);
            model.setE_latitude(this.mEndLatlng.latitude);
            model.setE_longitude(this.mEndLatlng.longitude);
        }
        return model;
    }

    private void httpSubmitRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new SubmitTuocheRequest(ServerUrl.getInst().SUBMIT_TUOCHE_REQUEST(), RequestMethod.POST, setRequestModel()), new OnResponseListener<String>() {
            public void onSucceed(int what, Response<String> response) {
                ToastUtil.showMessage(MyApplication.getInstance(), "提交成功");
                ActivityUtil.next(PublishRequestActivity.this, UserRequestActivity.class);
                PublishRequestActivity.this.finish();
            }

            public void onFailed(int what, Response<String> response) {
                Exception exception = response.getException();
                exception.printStackTrace();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
                PublishRequestActivity.this.dismissCommonProgressDialog();
            }
        });

    }

    private void httpGetConfig() {
        RequestQueue queue = NoHttp.newRequestQueue();
        GetPayAndSortMethodRequest payrequest = new GetPayAndSortMethodRequest(ServerUrl.getInst().GET_PAY_METHOD(), RequestMethod.POST);
        GetPayAndSortMethodRequest sortrequest = new GetPayAndSortMethodRequest(ServerUrl.getInst().GET_REQUEST_TYPE(), RequestMethod.POST);
        GetCarTypeRequest CarTyperequest = new GetCarTypeRequest(ServerUrl.getInst().GET_CAR_TYPE_METHOD(), RequestMethod.POST);
        queue.add(0, payrequest, new OnResponseListener<List<ModelType>>() {
            public void onSucceed(int what, Response<List<ModelType>> response) {
                PublishRequestActivity.this.payList = (List) response.get();
                PublishRequestActivity.this.mTxtPayType.setText(((ModelType) PublishRequestActivity.this.payList.get(0)).getTypeName());
                PublishRequestActivity.this.mPayId = Integer.valueOf(((ModelType) PublishRequestActivity.this.payList.get(0)).getTypeId());
            }

            public void onFailed(int what, Response<List<ModelType>> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
            }
        });
        queue.add(2, sortrequest, new OnResponseListener<List<ModelType>>() {
            public void onSucceed(int what, Response<List<ModelType>> response) {
                PublishRequestActivity.this.sortList = (List) response.get();
                PublishRequestActivity.this.mTxtSort.setText(((ModelType) PublishRequestActivity.this.sortList.get(0)).getTypeName());
                PublishRequestActivity.this.mSortId = Integer.valueOf(((ModelType) PublishRequestActivity.this.sortList.get(0)).getTypeId());
            }

            public void onFailed(int what, Response<List<ModelType>> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
            }
        });
        queue.add(1, CarTyperequest, new OnResponseListener<JSONObject>() {
            public void onSucceed(int what, Response<JSONObject> response) {
                JSONObject jsonObject = (JSONObject) response.get();
                PublishRequestActivity.this.modelTypes = JSON.parseArray(jsonObject.getJSONArray("model").toJSONString(), ModelType.class);
                PublishRequestActivity.this.mTxtCarType.setText(((ModelType) PublishRequestActivity.this.modelTypes.get(0)).getTypeName());
                PublishRequestActivity.this.mCarTypeId = Integer.valueOf(((ModelType) PublishRequestActivity.this.modelTypes.get(0)).getTypeId());
                PublishRequestActivity.this.driveTypes = JSON.parseArray(jsonObject.getJSONArray("drive").toJSONString(), ModelType.class);
                PublishRequestActivity.this.mTxtQudong.setText(((ModelType) PublishRequestActivity.this.driveTypes.get(0)).getTypeName());
                PublishRequestActivity.this.mQudongId = Integer.valueOf(((ModelType) PublishRequestActivity.this.driveTypes.get(0)).getTypeId());
                PublishRequestActivity.this.gearboxTypes = JSON.parseArray(jsonObject.getJSONArray("gearbox").toJSONString(), ModelType.class);
                PublishRequestActivity.this.mTxtBiansu.setText(((ModelType) PublishRequestActivity.this.gearboxTypes.get(0)).getTypeName());
                PublishRequestActivity.this.mBiansuId = Integer.valueOf(((ModelType) PublishRequestActivity.this.gearboxTypes.get(0)).getTypeId());
            }

            public void onFailed(int what, Response<JSONObject> response) {
                Exception exception = response.getException();
            }

            public void onStart(int what) {
            }

            public void onFinish(int what) {
            }
        });

    }

    public void onRouteCalculate(float cost, float distance, int duration) {
        this.mTxtDistance.setText(String.format("约%.1f公里", new Object[]{Float.valueOf(distance)}));
        this.resultMoney = 0.0d;
        if (distance > 10.0f) {
            this.resultMoney = (double) (150.0f + ((distance - 10.0f) * 7.0f));
        } else {
            this.resultMoney = 150.0d;
        }
        this.orignPrice = this.resultMoney;
        if (this.mCbIsReturn.isChecked()) {
            this.resultMoney = this.orignPrice - (this.orignPrice * 0.4000000059604645d);
        } else {
            this.resultMoney = this.orignPrice;
        }
        this.mTxtPrice.setText("¥" + new DecimalFormat("##0.0").format(this.resultMoney));
        this.mCbIsReturn.setVisibility(View.VISIBLE);
    }
}
