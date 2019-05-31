package com.tuochebang.user.ui.login;

import android.util.Log;
import com.tuochebang.user.app.MyApplication;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.LoginInfo;
import com.tuochebang.user.request.task.LoginRequest;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.LoginView view;

    /* renamed from: com.tuochebang.user.ui.login.LoginPresenter$1 */
    class C09521 implements OnResponseListener<LoginInfo> {
        C09521() {
        }

        public void onSucceed(int what, Response<LoginInfo> response) {
            LoginInfo loginInfo = (LoginInfo) response.get();
            if (loginInfo != null) {
                Log.e("LoginPresenter", loginInfo.toString());
                MyApplication.getInstance().setLoginInfo(loginInfo.getToken());
                MyApplication.getInstance().setUserInfo(loginInfo.getUser());
                LoginPresenter.this.view.loginSuccess();
            }
        }

        public void onFailed(int what, Response<LoginInfo> response) {
            Exception exception = response.getException();
        }

        public void onStart(int what) {
        }

        public void onFinish(int what) {
            LoginPresenter.this.view.dismissProgress();
        }
    }

    public LoginPresenter(LoginContract.LoginView view) {
        this.view = view;
        view.setPresenter(this);
    }

    public void login(String name, String password) {
        this.view.showProgress();
        httpLoginRequest(name, password);
    }

    private void httpLoginRequest(String name, String password) {
        RequestQueue queue = NoHttp.newRequestQueue();
        LoginRequest request = new LoginRequest(ServerUrl.getInst().USER_LOGIN_URL(), RequestMethod.POST, name, password);
        request.setmBeanClass(LoginInfo.class, 0);
        queue.add(0, request, new C09521());
    }
}
