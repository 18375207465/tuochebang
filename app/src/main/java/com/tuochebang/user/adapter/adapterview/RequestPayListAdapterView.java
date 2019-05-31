package com.tuochebang.user.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import com.tuochebang.user.R;
import com.tuochebang.user.request.entity.ModelType;

public class RequestPayListAdapterView extends RelativeLayout {
    public static String ALIPAY = "支付宝";
    public static String CASH = "现金";
    private static final String TAG = RequestPayListAdapterView.class.getSimpleName();
    public static String WECHATPAY = "微信支付";
    private onCheckListener mCheckListener;
    private ImageView mImgIcon;
    private RadioButton mRbName;
    private ModelType mSelectFilterName;

    public interface onCheckListener {
        void checkListener();
    }

    /* renamed from: com.tuochebang.user.adapter.adapterview.RequestPayListAdapterView$1 */
    class C09271 implements OnCheckedChangeListener {
        C09271() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (RequestPayListAdapterView.this.mCheckListener != null) {
                RequestPayListAdapterView.this.mCheckListener.checkListener();
            }
        }
    }

    public RequestPayListAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    public RequestPayListAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public RequestPayListAdapterView(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.adapter_view_request_pay_item, this);
        this.mRbName = (RadioButton) findViewById(R.id.pay_method_rb);
        this.mImgIcon = (ImageView) findViewById(R.id.iv_pay_icon);
    }

    public void refreshView(ModelType childType, int position) {
        this.mRbName.setOnCheckedChangeListener(new C09271());
        this.mRbName.setText(childType.getTypeName());
        if (childType.getTypeName().equals(ALIPAY)) {
            this.mImgIcon.setImageResource(R.mipmap.icon_zfb);
        } else if (childType.getTypeName().equals(WECHATPAY)) {
            this.mImgIcon.setImageResource(R.mipmap.icon_weixin);
        } else if (childType.getTypeName().equals(CASH)) {
            this.mImgIcon.setImageResource(R.mipmap.icon_cash);
        }
        if (childType.getTypeName().equalsIgnoreCase(this.mSelectFilterName.getTypeName())) {
            this.mRbName.setChecked(true);
        } else {
            this.mRbName.setChecked(false);
        }
    }

    public void setSelectFilterName(ModelType selectFilterName) {
        this.mSelectFilterName = selectFilterName;
    }

    public void setOnCheckListener(onCheckListener listener) {
        this.mCheckListener = listener;
    }
}
