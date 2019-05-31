package com.tuochebang.user.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.base.ServerUrl;
import com.tuochebang.user.request.entity.MessageInfo;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class GetMessageDetailRequest extends BaseRequest<MessageInfo> {
    public GetMessageDetailRequest(String url, RequestMethod requestMethod, String messageId) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("messageId", messageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected MessageInfo parseObject(String jsonData) {
        return (MessageInfo) JSON.parseObject(JSON.parseObject(jsonData).getJSONObject(ServerUrl.MESSAGE).toJSONString(), MessageInfo.class);
    }
}
