package com.tuochebang.user.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.ReturnCarInfo;
import com.tuochebang.user.request.task.ApplyReturnRequest;
import com.tuochebang.user.ui.returns.FindReturnActivity;
import com.tuochebang.user.ui.user.UserReturnsCarActivity;
import com.tuochebang.user.util.Tools;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class FindReturnCarAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtAcceptOrder;
    private TextView mTxtDestination;
    private TextView mTxtGo;
    private TextView mTxtPrice;
    private TextView mTxtTime;
    private TextView mTxtTitle;

    /* renamed from: com.tuochebang.user.adapter.adapterview.FindReturnCarAdapterView$2 */
    class C09222 implements OnResponseListener<Object> {
        C09222() {
        }

        public void onStart(int what) {
            ((FindReturnActivity) FindReturnCarAdapterView.this.mContext).showCommonProgreessDialog("请稍后...");
        }

        public void onSucceed(int what, Response<Object> response) {
            if (response.get() != null) {
                ((FindReturnActivity) FindReturnCarAdapterView.this.mContext).dismissCommonProgressDialog();
                ToastUtil.showMessage(MyApplication.getInstance(), "抢单成功");
                ActivityUtil.next((FindReturnActivity) FindReturnCarAdapterView.this.mContext, UserReturnsCarActivity.class);
                ((FindReturnActivity) FindReturnCarAdapterView.this.mContext).finish();
            }
        }

        public void onFailed(int what, Response<Object> response) {
        }

        public void onFinish(int what) {
            ((FindReturnActivity) FindReturnCarAdapterView.this.mContext).dismissCommonProgressDialog();
        }
    }

    public FindReturnCarAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public FindReturnCarAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FindReturnCarAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_find_return, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtTitle = (TextView) view.findViewById(R.id.txt_title);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_go_location);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_location);
        this.mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
        this.mTxtAcceptOrder = (TextView) view.findViewById(R.id.txt_btn_accept_order);
    }

    public void refreshView(final ReturnCarInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtPrice.setText(Tools.getShowMoneyString(info.getMoney()));
        this.mTxtTitle.setText(info.getType());
        this.mTxtAcceptOrder.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FindReturnCarAdapterView.this.httpAcceptOrder(info.getReturnId());
            }
        });
    }

    private void httpAcceptOrder(String id) {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new ApplyReturnRequest(ServerUrl.getInst().APPLY_RETURNS(), RequestMethod.POST, id), new C09222());

    }
}
