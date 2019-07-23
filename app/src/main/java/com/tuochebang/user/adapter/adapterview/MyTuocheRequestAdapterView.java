package com.tuochebang.user.adapter.adapterview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.framework.app.component.dialog.CommonNoticeDialog;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.constant.AppConstant;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.tuochebang.user.request.task.DeleteRequest;
import com.tuochebang.user.ui.request.PublishRequestActivity;
import com.tuochebang.user.ui.user.UserRequestActivity;
import com.tuochebang.user.util.Tools;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;


public class MyTuocheRequestAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtDelete;
    private TextView mTxtDestination;
    private TextView mTxtEdit;
    private TextView mTxtGo;
    private TextView mTxtPrice;
    private TextView mTxtTime;

    /* renamed from: com.tuochebang.user.adapter.adapterview.MyTuocheRequestAdapterView$3 */
    class C09263 implements OnResponseListener<Object> {
        C09263() {
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ((UserRequestActivity) MyTuocheRequestAdapterView.this.mContext).dismissCommonProgressDialog();
                ToastUtil.showMessage(MyApplication.getInstance(), "删除成功");
                BroadCastUtil.sendBroadCast(MyTuocheRequestAdapterView.this.mContext, BroadCastAction.TUOCHE_REQUEST_CHANGE);
            }
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            ((UserRequestActivity) MyTuocheRequestAdapterView.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onFinish(int what) {

        }


    }

    public MyTuocheRequestAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public MyTuocheRequestAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyTuocheRequestAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_mine_tuoche_request, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_go_location);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_location);
        this.mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
        this.mTxtEdit = (TextView) view.findViewById(R.id.txt_btn_edit);
        this.mTxtDelete = (TextView) view.findViewById(R.id.txt_btn_delete);
    }

    public void refreshView(final TuocheRequestInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtPrice.setText(info.getMoney() + "");
        if (info.getStatus() == 0) {
            this.mTxtEdit.setVisibility(VISIBLE);
            this.mTxtDelete.setVisibility(VISIBLE);
        } else if (info.getStatus() == 1) {
            this.mTxtDelete.setVisibility(VISIBLE);
        } else {
            this.mTxtDelete.setVisibility(GONE);
        }
        this.mTxtEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(PublishRequestActivity.EXTRAS_REQUEST_INFO, info);
                bundle.putInt(PublishRequestActivity.EXTRAS_REQUEST_TYPE, PublishRequestActivity.TYPE_EDIT);
                ActivityUtil.next((Activity) MyTuocheRequestAdapterView.this.mContext, PublishRequestActivity.class, bundle);
            }
        });
        this.mTxtDelete.setOnClickListener(new OnClickListener() {

            //删除拖车请求  进行中
            /* renamed from: com.tuochebang.user.adapter.adapterview.MyTuocheRequestAdapterView$2$1 */
            class C09241 implements CommonNoticeDialog.DialogButtonInterface {
                C09241() {
                }

                public void onDialogButtonClick(CommonNoticeDialog.DialogResult dialogResult) {
                    if (dialogResult != CommonNoticeDialog.DialogResult.Yes) {
                        return;
                    }
                    if (info.getStatus() == 1) {
                        Tools.callPhone((UserRequestActivity) MyTuocheRequestAdapterView.this.mContext, AppConstant.APP_PHONE);
                    } else {
                        MyTuocheRequestAdapterView.this.httpDeleteRequest(info);
                    }
                }
            }

            public void onClick(View v) {
                ((UserRequestActivity) MyTuocheRequestAdapterView.this.mContext).showChoiceDialog("提示", "是否删除该拖车请求?", new C09241());
            }
        });
    }

    private void httpDeleteRequest(TuocheRequestInfo info) {
        RequestQueue queue = NoHttp.newRequestQueue();
        DeleteRequest request = new DeleteRequest(ServerUrl.getInst().DELETE_TUOCHE_REQUEST(), RequestMethod.POST, info.getRequestId(), String.valueOf(info.getStatus()));
        request.setmBeanClass(Object.class, 0);
        queue.add(0, request, new C09263());

    }
}
