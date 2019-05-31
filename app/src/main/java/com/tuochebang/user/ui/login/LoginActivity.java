package com.tuochebang.user.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import com.framework.app.component.utils.ActivityUtil;
import com.framework.app.component.utils.BroadCastUtil;
import com.framework.app.component.utils.ToastUtil;
import com.tuochebang.user.BuildConfig;
import com.tuochebang.user.R;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.base.BaseActivity;
import com.tuochebang.user.constant.AppConstant.BroadCastAction;
import com.tuochebang.user.ui.register.RegisterActivity;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    private static final int PHONE_NUMBER_SIZE = 1;
    private static final int PWD_SIZE = 1;
    private boolean isActive;
    private Button mBtnClear;
    private Button mBtnLogin;
    private Button mBtnPwdClear;
    private Button mBtnRegister;
    private EditText mEdPhone;
    private EditText mEdPwd;
    private ScrollView mScrollView;
    private LoginPresenter presenter;

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$1 */
    class C09441 implements TextWatcher {
        C09441() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            LoginActivity.this.mBtnClear.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$2 */
    class C09452 implements OnTouchListener {
        C09452() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            LoginActivity.this.changeScrollView();
            return false;
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$3 */
    class C09463 implements TextWatcher {
        C09463() {
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                LoginActivity.this.mBtnPwdClear.setVisibility(View.VISIBLE);
            } else {
                LoginActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
            }
            if (s.length() < 1) {
                LoginActivity.this.mBtnLogin.setEnabled(false);
            } else if (LoginActivity.this.mEdPhone.getText().toString().length() >= 1) {
                LoginActivity.this.mBtnLogin.setEnabled(true);
            } else {
                LoginActivity.this.mBtnLogin.setEnabled(false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$4 */
    class C09474 implements OnClickListener {
        C09474() {
        }

        public void onClick(View v) {
            LoginActivity.this.presenter.login(LoginActivity.this.mEdPhone.getText().toString(), LoginActivity.this.mEdPwd.getText().toString());
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$5 */
    class C09485 implements OnClickListener {
        C09485() {
        }

        public void onClick(View v) {
            LoginActivity.this.mEdPhone.getText().clear();
            LoginActivity.this.mBtnClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$6 */
    class C09496 implements OnClickListener {
        C09496() {
        }

        public void onClick(View v) {
            LoginActivity.this.mEdPwd.getText().clear();
            LoginActivity.this.mBtnPwdClear.setVisibility(View.INVISIBLE);
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$7 */
    class C09507 implements OnClickListener {
        C09507() {
        }

        public void onClick(View v) {
            ActivityUtil.next(LoginActivity.this, RegisterActivity.class);
        }
    }

    /* renamed from: com.tuochebang.user.ui.login.LoginActivity$8 */
    class C09518 implements Runnable {
        C09518() {
        }

        public void run() {
            LoginActivity.this.mScrollView.scrollTo(0, BuildConfig.VERSION_CODE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        //changeScrollView();
        initListener();
        initData();
    }

    private void initData() {
        this.presenter = new LoginPresenter(this);
    }

    private void initListener() {
        this.mEdPhone.addTextChangedListener(new C09441());
       // this.mEdPhone.setOnTouchListener(new C09452());
        this.mEdPwd.addTextChangedListener(new C09463());
        this.mBtnLogin.setOnClickListener(new C09474());
        this.mBtnClear.setOnClickListener(new C09485());
        this.mBtnPwdClear.setOnClickListener(new C09496());
        this.mBtnRegister.setOnClickListener(new C09507());
    }

    private void initView() {
        this.mEdPhone = (EditText) findViewById(R.id.username);
        this.mEdPwd = (EditText) findViewById(R.id.password);
        this.mBtnLogin = (Button) findViewById(R.id.login);
        this.mBtnRegister = (Button) findViewById(R.id.register);
        this.mBtnClear = (Button) findViewById(R.id.bt_username_clear);
        this.mBtnPwdClear = (Button) findViewById(R.id.bt_pwd_clear);
    }

    protected void onStart() {
        super.onStart();
        this.isActive = true;
    }

    protected void onStop() {
        super.onStop();
        this.isActive = false;
    }

    public void loginSuccess() {
        ToastUtil.showMessage(MyApplication.getInstance(), "登录成功");
        BroadCastUtil.sendBroadCast(this.mContext, BroadCastAction.USER_LOGIN_SUCCESS);
        finish();
    }

    public void setPresenter(LoginContract.Presenter presenter) {
    }

    public boolean isActive() {
        return false;
    }

    public void showProgress() {
        showCommonProgreessDialog("请稍后");
    }

    public void dismissProgress() {
        dismissCommonProgressDialog();
    }

    public void showTip(String message) {
        ToastUtil.showMessage((Context) this, message);
    }

    private void changeScrollView() {
        new Handler().postDelayed(new C09518(), 800);
    }
}
