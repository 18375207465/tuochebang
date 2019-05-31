package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class ApplyReturnRequest extends BaseRequest<Object> {
    public ApplyReturnRequest(String url, RequestMethod requestMethod, String returnId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("returnId", returnId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected Object parseObject(String jsonData) {
        return "";
    }
}
