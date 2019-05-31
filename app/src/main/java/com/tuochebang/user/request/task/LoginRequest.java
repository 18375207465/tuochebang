package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class LoginRequest extends BaseRequest<LoginInfo> {
    public LoginRequest(String url, RequestMethod requestMethod, String name, String password) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("account", name);
            this.mParams.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
