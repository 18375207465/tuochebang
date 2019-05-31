package com.tuochebang.user.request.task;

import com.alibaba.fastjson.JSON;
import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.TuocheRequestInfo;
import com.yanzhenjie.nohttp.RequestMethod;
import java.util.List;
import org.json.JSONObject;

public class GetUserTuocheRequest extends BaseRequest<List<TuocheRequestInfo>> {
    public GetUserTuocheRequest(String url, RequestMethod requestMethod, int status, int pageNo) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("status", status);
            this.mParams.put("longitude", "106.672461");
            this.mParams.put("latitude", "26.593061");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHeader("pagesize", String.valueOf(10));
        setHeader("pageno", String.valueOf(pageNo));
        setDefineRequestBodyForJson(this.mParams);
    }

    protected List<TuocheRequestInfo> parseObject(String jsonData) {
        return JSON.parseArray(JSON.parseObject(jsonData).getJSONArray("request").toJSONString(), TuocheRequestInfo.class);
    }
}
