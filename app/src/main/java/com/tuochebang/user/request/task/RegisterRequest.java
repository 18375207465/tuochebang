package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class RegisterRequest extends BaseRequest<String> {
    public RegisterRequest(String url, RequestMethod requestMethod, String name, String password) {
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
