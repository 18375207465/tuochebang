package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class DeleteRequest extends BaseRequest<Object> {
    public DeleteRequest(String url, RequestMethod requestMethod, String requestId, String status) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("requestId", requestId);
            this.mParams.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(10));
        setDefineRequestBodyForJson(this.mParams);
    }
}
