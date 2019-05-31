package com.tuochebang.user.ui.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.task.FeedBackRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class FeedBackActivity extends BaseActivity {
    private EditText mEdInfo;
    private Toolbar mToolBar;
    private TextView mTxtSure;

    /* renamed from: com.tuochebang.user.ui.setting.FeedBackActivity$1 */
    class C09991 implements OnClickListener {
        C09991() {
        }

        public void onClick(View v) {
            FeedBackActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.FeedBackActivity$2 */
    class C10002 implements OnClickListener {
        C10002() {
        }

        public void onClick(View v) {
            FeedBackActivity.this.httpFeedBackRequest();
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.FeedBackActivity$3 */
    class C10013 implements TextWatcher {
        C10013() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                FeedBackActivity.this.mTxtSure.setVisibility(View.VISIBLE);
            } else {
                FeedBackActivity.this.mTxtSure.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.setting.FeedBackActivity$4 */
    class C10024 implements OnResponseListener<Object> {
        C10024() {
        }

        public void onSucceed(int what, Response<Object> response) {
            FeedBackActivity.this.showToast("提交成功");
            FeedBackActivity.this.finish();
        }

        public void onFailed(int what, Response<Object> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            FeedBackActivity.this.showCommonProgreessDialog("提交中..");
        }

        public void onFinish(int what) {
            FeedBackActivity.this.dismissCommonProgressDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initToolBar();
        initView();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mTxtSure = (TextView) findViewById(R.id.tcb_sure_txt);
        this.mToolBar.setNavigationOnClickListener(new C09991());
        this.mTxtSure.setOnClickListener(new C10002());
    }

    private void initView() {
        this.mEdInfo = (EditText) findViewById(R.id.tcb_feedback_et);
        this.mEdInfo.addTextChangedListener(new C10013());
    }

    private void httpFeedBackRequest() {
        RequestQueue queue = NoHttp.newRequestQueue();
        queue.add(0, new FeedBackRequest(ServerUrl.getInst().FEEDBACK_URL(), RequestMethod.POST, this.mEdInfo.getText().toString()), new C10024());

    }
}
