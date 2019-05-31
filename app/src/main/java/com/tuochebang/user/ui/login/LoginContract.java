package com.tuochebang.user.ui.login;

import com.tuochebang.user.base.BasePresenter;
import com.tuochebang.user.base.BaseView;

public class LoginContract {

    interface LoginView extends BaseView<Presenter> {
        void loginSuccess();
    }

    interface Presenter extends BasePresenter {
        void login(String str, String str2);
    }
}
