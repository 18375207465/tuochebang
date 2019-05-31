package com.tuochebang.user.ui.returns;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.ReturnCarInfo;
import com.tuochebang.user.request.task.ConfirmReturnRequest;
import com.tuochebang.user.util.AMapUtil;
import com.tuochebang.user.util.Tools;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class ReturnsDetailActivity extends BaseActivity implements OnRouteSearchListener {
    public static String EXTRAS_RETURNS_CAR = "extras_returns_car";
    private final int ROUTE_TYPE_DRIVE = 2;
    private AMap aMap;
    private ReturnCarInfo info;
    private DriveRouteResult mDriveRouteResult;
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576d, 116.481288d);
    private MapView mMapView;
    private RouteSearch mRouteSearch;
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295d, 116.335891d);
    private Toolbar mToolBar;
    private TextView mTxtBeginLocation;
    private TextView mTxtBeginTime;
    private TextView mTxtCompanyName;
    private TextView mTxtEndLocation;
    private TextView mTxtOperation;
    private TextView mTxtOrderNum;
    private TextView mTxtOrderStatus;
    private TextView mTxtOrderTime;
    private TextView mTxtPhoneNum;
    private TextView mTxtPrice;
    private TextView mTxtType;

    /* renamed from: com.tuochebang.user.ui.returns.ReturnsDetailActivity$1 */
    class C09961 implements OnClickListener {
        C09961() {
        }

        public void onClick(View v) {
            ReturnsDetailActivity.this.httpConfirmReturn(ReturnsDetailActivity.this.info);
        }
    }

    /* renamed from: com.tuochebang.user.ui.returns.ReturnsDetailActivity$2 */
    class C09972 implements OnResponseListener<Object> {
        C09972() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ((ReturnsDetailActivity) ReturnsDetailActivity.this.mContext).dismissCommonProgressDialog();
                ToastUtil.showMessage(MyApplication.getInstance(), "确认成功");
                BroadCastUtil.sendBroadCast(ReturnsDetailActivity.this.mContext, BroadCastAction.TUOCHE_REQUEST_CHANGE);
                ((ReturnsDetailActivity) ReturnsDetailActivity.this.mContext).finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((ReturnsDetailActivity) ReturnsDetailActivity.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onFinish(int what) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.returns.ReturnsDetailActivity$3 */
    class C09983 implements OnClickListener {
        C09983() {
        }

        public void onClick(View v) {
            ReturnsDetailActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returns_detail);
        getIntentExtras();
        initToolBar();
        initView(savedInstanceState);
        initData();
    }

    private void initData() {
        this.mTxtOrderStatus.setText(this.info.getStatus() == 1 ? "进行中" : "已完成");
        this.mTxtBeginLocation.setText(this.info.getBegin());
        this.mTxtEndLocation.setText(this.info.getEnd());
        this.mTxtCompanyName.setText(this.info.getName());
        this.mTxtPhoneNum.setText(this.info.getMobile());
        this.mTxtPrice.setText(Tools.getShowMoneyString(this.info.getMoney()));
        this.mTxtType.setText(this.info.getType());
        this.mTxtOperation.setOnClickListener(new C09961());
        if (this.info.getStatus() == 1) {
            this.mTxtOperation.setVisibility(View.VISIBLE);
        } else {
            this.mTxtOperation.setVisibility(View.GONE);
        }
    }

    private void httpConfirmReturn(ReturnCarInfo info) {
        RequestQueue queue = NoHttp.newRequestQueue();
        ConfirmReturnRequest request = new ConfirmReturnRequest(ServerUrl.getInst().CONFIRM_TUOCHE_RETURN(), RequestMethod.POST, info.getReturnId());
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C09972());

    }

    private void getIntentExtras() {
        this.info = (ReturnCarInfo) getIntent().getSerializableExtra(EXTRAS_RETURNS_CAR);
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C09983());
    }

    private void initView(Bundle savedInstanceState) {
        this.mTxtOrderTime = (TextView) findViewById(R.id.tcb_order_time_txt);
        this.mTxtOrderNum = (TextView) findViewById(R.id.tcb_order_num_txt);
        this.mTxtOrderStatus = (TextView) findViewById(R.id.tcb_order_status_txt);
        this.mTxtType = (TextView) findViewById(R.id.tcb_type_txt);
        this.mTxtBeginTime = (TextView) findViewById(R.id.tcb_begin_time_txt);
        this.mTxtBeginLocation = (TextView) findViewById(R.id.tcb_begin_location_txt);
        this.mTxtEndLocation = (TextView) findViewById(R.id.tcb_end_location_txt);
        this.mTxtCompanyName = (TextView) findViewById(R.id.tcb_company_name_txt);
        this.mTxtPhoneNum = (TextView) findViewById(R.id.tcb_phone_num_txt);
        this.mTxtPrice = (TextView) findViewById(R.id.tcb_return_price);
        this.mTxtOperation = (TextView) findViewById(R.id.tcb_operation_txt);
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
}
