package com.tuochebang.user.ui.user;

import com.tuochebang.user.base.BasePresenter;
import com.tuochebang.user.base.BaseView;

public class UserContract {

    interface Presenter extends BasePresenter {
        void saveUser(String str, String str2);
    }

    interface UserView extends BaseView<Presenter> {
        void saveSuccess();
    }
}
