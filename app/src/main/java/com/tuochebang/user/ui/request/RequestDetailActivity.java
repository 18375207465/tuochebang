package com.tuochebang.user.ui.request;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.optimize.GridViewForScrollView;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.RequestImageListAdapter;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.tuochebang.user.request.task.ConfirmTuocheRequest;
import com.tuochebang.user.request.task.DeleteRequest;
import com.tuochebang.user.ui.ImageSimpleBrowseActivity;
import com.tuochebang.user.ui.returns.DrivingRouteOverLay;
import com.tuochebang.user.util.AMapUtil;
import com.tuochebang.user.util.Tools;
import com.yalantis.ucrop.view.CropImageView;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class RequestDetailActivity extends BaseActivity implements OnRouteSearchListener {
    public static String EXTRAS_REQUEST_INFO = PublishRequestActivity.EXTRAS_REQUEST_INFO;
    private final int ROUTE_TYPE_DRIVE = 2;
    private AMap aMap;
    private RequestImageListAdapter adapter;
    private Button mBtnCancel;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576d, 116.481288d);
    private CircleImageView mImgUserHeader;
    private MapView mMapView;
    private TuocheRequestInfo mRequestInfo;
    private RelativeLayout mRlDriverInfo;
    private RouteSearch mRouteSearch;
    private ScrollView mScrollView;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295d, 116.335891d);
    private Toolbar mToolBar;
    private TextView mTxtCarInfo;
    private TextView mTxtCarType;
    private TextView mTxtDelete;
    private TextView mTxtDriverName;
    private TextView mTxtLocFrom;
    private TextView mTxtLocTo;
    private TextView mTxtOrder;
    private TextView mTxtOrderStatus;
    private TextView mTxtOther;
    private TextView mTxtPrice;
    private TextView mTxtReturn;
    private TextView mTxtTime;
    private TextView mTxtUserPhone;
    private GridViewForScrollView noScrollgridview;

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$1 */
    class C09791 implements OnClickListener {
        C09791() {
        }

        public void onClick(View v) {
            Tools.callPhone(RequestDetailActivity.this, RequestDetailActivity.this.mRequestInfo.getDriverInfo().getMobile());
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$2 */
    class C09802 implements OnClickListener {
        C09802() {
        }

        public void onClick(View v) {
            RequestDetailActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$3 */
    class C09813 implements OnClickListener {
        C09813() {
        }

        public void onClick(View v) {
            RequestDetailActivity.this.httpConfirmRequest(RequestDetailActivity.this.mRequestInfo);
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$4 */
    class C09824 implements OnItemClickListener {
        C09824() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ImageSimpleBrowseActivity.EXTRA_IMAGE_URLS, (ArrayList) RequestDetailActivity.this.mRequestInfo.getPicture());
            bundle.putInt("position", position);
            ActivityUtil.next((Activity) RequestDetailActivity.this.mContext, ImageSimpleBrowseActivity.class, bundle, false, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$5 */
    class C09845 implements OnClickListener {

        /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$5$1 */
        class C09831 implements CommonNoticeDialog.DialogButtonInterface {
            C09831() {
            }

            public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                if (dialogResult == CommonNoticeDialog.DialogResult.Yes) {
                    RequestDetailActivity.this.httpDeleteRequest(RequestDetailActivity.this.mRequestInfo);
                }
            }
        }

        C09845() {
        }

        public void onClick(View v) {
            RequestDetailActivity.this.showChoiceDialog("提示", "是否删除该拖车请求?", new C09831());
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$6 */
    class C09856 implements OnResponseListener<Object> {
        C09856() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ((RequestDetailActivity) RequestDetailActivity.this.mContext).dismissCommonProgressDialog();
                ToastUtil.showMessage(MyApplication.getInstance(), "删除成功");
                BroadCastUtil.sendBroadCast(RequestDetailActivity.this.mContext, BroadCastAction.TUOCHE_REQUEST_CHANGE);
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((RequestDetailActivity) RequestDetailActivity.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onFinish(int what) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.request.RequestDetailActivity$7 */
    class C09867 implements OnResponseListener<Object> {
        C09867() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ToastUtil.showMessage(MyApplication.getInstance(), "确认成功");
                BroadCastUtil.sendBroadCast(RequestDetailActivity.this.mContext, BroadCastAction.TUOCHE_REQUEST_CHANGE);
                RequestDetailActivity.this.finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((RequestDetailActivity) RequestDetailActivity.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onFinish(int what) {
            ((RequestDetailActivity) RequestDetailActivity.this.mContext).dismissCommonProgressDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);
        getIntentExtras();
        initToolBar();
        initView(savedInstanceState);
        initData();
    }

    private void initData() {
        refreshView();
    }

    private void refreshView() {
        this.mTxtCarInfo.setText(this.mRequestInfo.getCar().getInfo() + " " + this.mRequestInfo.getCar().getModelName() + " " + this.mRequestInfo.getCar().getGearName() + " " + this.mRequestInfo.getCar().getDriveName());
        this.mTxtCarType.setText(this.mRequestInfo.getTypeName());
        if (this.mRequestInfo.getIsReturn() == 0) {
            this.mTxtReturn.setText("不需要返程车");
        } else {
            this.mTxtReturn.setText("需要返程车");
        }
        this.mTxtOther.setText(this.mRequestInfo.getOtherCar() + " " + (this.mRequestInfo.getIsCrane() == 1 ? "需要吊车" : "不需要吊车"));
        this.mTxtLocFrom.setText(this.mRequestInfo.getBegin());
        this.mTxtLocTo.setText(this.mRequestInfo.getEnd());
        if (this.mRequestInfo.getStatus() == 0) {
            this.mTxtOrderStatus.setText("待抢单");
        } else if (this.mRequestInfo.getStatus() == 1) {
            this.mTxtOrderStatus.setText("进行中");
            this.mRlDriverInfo.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(this.mRequestInfo.getDriverInfo().getPicture(), this.mImgUserHeader);
            this.mTxtDriverName.setText(this.mRequestInfo.getDriverInfo().getNickName());
            this.mTxtUserPhone.setText(this.mRequestInfo.getDriverInfo().getMobile());
            findViewById(R.id.img_call).setOnClickListener(new C09791());
        } else if (this.mRequestInfo.getStatus() == 2) {
            this.mTxtOrderStatus.setText("已完成");
        }
        this.mTxtTime.setText(this.mRequestInfo.getTime());
        this.mTxtPrice.setText(Tools.getShowMoneyString(this.mRequestInfo.getMoney()));
        this.adapter.setList(this.mRequestInfo.getPicture());
    }

    private void getIntentExtras() {
        this.mRequestInfo = (TuocheRequestInfo) getIntent().getSerializableExtra(EXTRAS_REQUEST_INFO);
        this.mStartPoint = new LatLonPoint(this.mRequestInfo.getLatitude(), this.mRequestInfo.getLongitude());
        this.mEndPoint = new LatLonPoint(this.mRequestInfo.getE_latitude(), this.mRequestInfo.getE_longitude());
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mTxtDelete = (TextView) findViewById(R.id.tcb_operation_txt);
        this.mToolBar.setNavigationOnClickListener(new C09802());
        if (this.mRequestInfo.getStatus() == 1) {
            this.mTxtDelete.setOnClickListener(new C09813());
        } else {
            this.mTxtDelete.setVisibility(View.GONE);
        }
    }

    private void initView(Bundle savedInstanceState) {
        this.mImgUserHeader = (CircleImageView) findViewById(R.id.img_user);
        this.mTxtDriverName = (TextView) findViewById(R.id.txt_driver_name);
        this.mTxtUserPhone = (TextView) findViewById(R.id.txt_user_phone);
        this.mRlDriverInfo = (RelativeLayout) findViewById(R.id.rl_driver_info);
        this.mRlDriverInfo.setVisibility(View.GONE);
        this.mTxtTime = (TextView) findViewById(R.id.tcb_request_time_txt);
        this.mTxtLocTo = (TextView) findViewById(R.id.tcb_request_to_loc_txt);
        this.mTxtLocFrom = (TextView) findViewById(R.id.tcb_request_from_loc_txt);
        this.mTxtOrder = (TextView) findViewById(R.id.tcb_request_order_txt);
        this.mTxtOrderStatus = (TextView) findViewById(R.id.tcb_request_order_status_txt);
        this.mTxtPrice = (TextView) findViewById(R.id.tcb_request_price_txt);
        this.mTxtCarInfo = (TextView) findViewById(R.id.txt_car_info);
        this.mTxtCarType = (TextView) findViewById(R.id.txt_car_type);
        this.mTxtReturn = (TextView) findViewById(R.id.txt_is_return);
        this.mTxtOther = (TextView) findViewById(R.id.txt_car_other);
        this.mBtnCancel = (Button) findViewById(R.id.cancel_order);
        this.mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        this.mMapView = (MapView) findViewById(R.id.mapview);
        this.mMapView.onCreate(savedInstanceState);
        if (this.aMap == null) {
            this.aMap = this.mMapView.getMap();
        }
        this.mScrollView.requestDisallowInterceptTouchEvent(false);
        this.mRouteSearch = new RouteSearch(this);
        this.mRouteSearch.setRouteSearchListener(this);
        setfromandtoMarker();
        searchRouteResult(2, 0);
        this.adapter = new RequestImageListAdapter(this.mContext);
        this.noScrollgridview = (GridViewForScrollView) findViewById(R.id.noScrollgridview);
        this.noScrollgridview.setSelector(new ColorDrawable(0));
        this.noScrollgridview.setAdapter(this.adapter);
        this.noScrollgridview.setOnItemClickListener(new C09824());
        if (this.mRequestInfo.getStatus() == 1) {
            this.mBtnCancel.setVisibility(View.VISIBLE);
        }
        this.mBtnCancel.setOnClickListener(new C09845());
    }

    private void setfromandtoMarker() {
        this.aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(this.mStartPoint)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_starting)));
        this.aMap.addMarker(new MarkerOptions().position(AMapUtil.convertToLatLng(this.mEndPoint)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_position)));
    }

    public void searchRouteResult(int routeType, int mode) {
        if (this.mStartPoint != null) {
            FromAndTo fromAndTo;
            if (this.mEndPoint == null) {
                fromAndTo = new FromAndTo(this.mStartPoint, this.mEndPoint);
            } else {
                fromAndTo = new FromAndTo(this.mStartPoint, this.mEndPoint);
            }
            if (routeType == 2) {
                this.mRouteSearch.calculateDriveRouteAsyn(new DriveRouteQuery(fromAndTo, mode, null, null, ""));
            }
        }
    }

    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    }

    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        this.aMap.clear();
        if (errorCode == 1000 && result != null && result.getPaths() != null) {
            if (result.getPaths().size() > 0) {
                this.mDriveRouteResult = result;
                DrivePath drivePath = (DrivePath) this.mDriveRouteResult.getPaths().get(0);
                DrivingRouteOverLay drivingRouteOverlay = new DrivingRouteOverLay(this.mContext, this.aMap, drivePath, this.mDriveRouteResult.getStartPos(), this.mDriveRouteResult.getTargetPos(), null);
                drivingRouteOverlay.setNodeIconVisibility(false);
                drivingRouteOverlay.addStartAndEndMarker();
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                int dis = (int) drivePath.getDistance();
                int duration = (int) drivePath.getDuration();
            } else if (result != null && result.getPaths() == null) {
            }
        }
    }

    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    }

    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

    private void httpDeleteRequest(TuocheRequestInfo info) {
        RequestQueue queue = NoHttp.newRequestQueue();
        DeleteRequest request = new DeleteRequest(ServerUrl.getInst().DELETE_TUOCHE_REQUEST(), RequestMethod.POST, info.getRequestId(), String.valueOf(info.getStatus()));
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C09856());

    }

    private void httpConfirmRequest(TuocheRequestInfo info) {
        RequestQueue queue = NoHttp.newRequestQueue();
        ConfirmTuocheRequest request = new ConfirmTuocheRequest(ServerUrl.getInst().CONFIRM_TUOCHE_REQUEST(), RequestMethod.POST, info.getRequestId());
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C09867());

    }
}
