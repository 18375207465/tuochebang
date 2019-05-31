package com.tuochebang.user.request.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuochebang.user.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;

public class GetCarTypeRequest extends BaseRequest<JSONObject> {
    public GetCarTypeRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
        this.mParams = new org.json.JSONObject();
        setDefineRequestBodyForJson(this.mParams);
    }

    protected JSONObject parseObject(String jsonData) {
        return JSON.parseObject(jsonData);
    }
}
