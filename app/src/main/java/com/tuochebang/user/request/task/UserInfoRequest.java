package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class UserInfoRequest extends BaseRequest<LoginInfo> {
    public UserInfoRequest(String url, RequestMethod requestMethod, String userId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
