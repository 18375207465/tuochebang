package com.tuochebang.user.ui.register;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import com.tuochebang.user.R;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.task.RegisterRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class RegisterActivity extends BaseActivity {
    private static final int PHONE_NUMBER_SIZE = 11;
    private static final int PWD_SIZE = 1;
    private Button mBtnClear;
    private Button mBtnLogin;
    private Button mBtnPwdClear;
    private EditText mEdPhone;
    private EditText mEdPwd;
    private Toolbar mToolBar;

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$1 */
    class C09561 implements OnClickListener {
        C09561() {
        }

        public void onClick(View v) {
            RegisterActivity.this.finish();
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$2 */
    class C09572 implements TextWatcher {
        C09572() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            RegisterActivity.this.mBtnClear.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$3 */
    class C09583 implements OnTouchListener {
        C09583() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return false;
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$4 */
    class C09594 implements TextWatcher {
        C09594() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                RegisterActivity.this.mBtnPwdClear.setVisibility(View.VISIBLE);
            } else {
                RegisterActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
            }
            if (s.length() < 1) {
                RegisterActivity.this.mBtnLogin.setEnabled(false);
            } else if (RegisterActivity.this.mEdPhone.getText().toString().length() >= 11) {
                RegisterActivity.this.mBtnLogin.setEnabled(true);
            } else {
                RegisterActivity.this.mBtnLogin.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$5 */
    class C09605 implements OnClickListener {
        C09605() {
        }

        public void onClick(View v) {
            httpRegisterRequest(mEdPhone.getText().toString(), mEdPwd.getText().toString());
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$6 */
    class C09616 implements OnClickListener {
        C09616() {
        }

        public void onClick(View v) {
            RegisterActivity.this.mEdPhone.getText().clear();
            RegisterActivity.this.mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$7 */
    class C09627 implements OnClickListener {
        C09627() {
        }

        public void onClick(View v) {
            RegisterActivity.this.mEdPwd.getText().clear();
            RegisterActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.user.ui.register.RegisterActivity$8 */
    class C09638 implements OnResponseListener<String> {
        C09638() {
        }

        public void onSucceed(int what, Response<String> response) {
            showToast("注册成功");
            finish();
        }

        public void onFailed(int what, Response<String> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
            showCommonProgreessDialog("注册中");
        }

        public void onFinish(int what) {
            dismissCommonProgressDialog();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initToolBar();
        initListener();
    }

    private void initToolBar() {
        this.mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.mToolBar.setNavigationOnClickListener(new C09561());
    }

    private void initView() {
        this.mEdPhone = (EditText) findViewById(R.id.username);
        this.mEdPwd = (EditText) findViewById(R.id.password);
        this.mBtnLogin = (Button) findViewById(R.id.register);
        this.mBtnClear = (Button) findViewById(R.id.bt_username_clear);
        this.mBtnPwdClear = (Button) findViewById(R.id.bt_pwd_clear);
    }

    private void initListener() {
        this.mEdPhone.addTextChangedListener(new C09572());
        this.mEdPhone.setOnTouchListener(new C09583());
        this.mEdPwd.addTextChangedListener(new C09594());
        this.mBtnLogin.setOnClickListener(new C09605());
        this.mBtnClear.setOnClickListener(new C09616());
        this.mBtnPwdClear.setOnClickListener(new C09627());
    }

    private void httpRegisterRequest(String name, String password) {
        RequestQueue queue = NoHttp.newRequestQueue();

        RegisterRequest request = new RegisterRequest(ServerUrl.getInst().GET_TUOCHE_REGISTER(), RequestMethod.POST, name, password);
        request.setmBeanClass(String.class, 0);
        queue.add(0, request, new C09638());
    }
}
