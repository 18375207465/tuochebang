package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.util.ImageCompress;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class FeedBackRequest extends BaseRequest<Object> {
    public FeedBackRequest(String url, RequestMethod requestMethod, String content) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put(ImageCompress.CONTENT, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
