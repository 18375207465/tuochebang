package com.tuochebang.user.request;

import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.IdCard;
import com.yanzhenjie.nohttp.RequestMethod;

public class TestRequest extends BaseRequest<IdCard> {
    public TestRequest(String url, RequestMethod requestMethod) {
        super(url, requestMethod);
    }
}
