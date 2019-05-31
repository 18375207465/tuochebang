package com.tuochebang.user.adapter.adapterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.request.entity.ReturnCarInfo;

public class MyReturnCarAdapterView extends RelativeLayout {
    private Context mContext;
    private TextView mTxtCompanyName;
    private TextView mTxtDestination;
    private TextView mTxtGo;
    private TextView mTxtPhoneNum;
    private TextView mTxtPrice;
    private TextView mTxtTime;

    public MyReturnCarAdapterView(Context context) {
        super(context);
        initView(context);
    }

    public MyReturnCarAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyReturnCarAdapterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_view_mine_return_car, this);
        this.mTxtTime = (TextView) view.findViewById(R.id.txt_go_time);
        this.mTxtGo = (TextView) view.findViewById(R.id.txt_go_location);
        this.mTxtDestination = (TextView) view.findViewById(R.id.txt_destination_location);
        this.mTxtPrice = (TextView) view.findViewById(R.id.txt_price);
        this.mTxtCompanyName = (TextView) view.findViewById(R.id.txt_company_name);
        this.mTxtPhoneNum = (TextView) view.findViewById(R.id.txt_phone_num);
    }

    public void refreshView(ReturnCarInfo info) {
        this.mTxtGo.setText(info.getBegin());
        this.mTxtDestination.setText(info.getEnd());
        this.mTxtTime.setText(info.getTime());
        this.mTxtPrice.setText(info.getMoney() + "");
        this.mTxtCompanyName.setText(info.getCorporate());
        this.mTxtPhoneNum.setText(info.getMobile());
    }
}
