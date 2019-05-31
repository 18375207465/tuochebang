package com.tuochebang.user.request.task;

import android.text.TextUtils;
import com.luck.picture.lib.config.PictureConfig;
import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.LoginInfo;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONException;
import org.json.JSONObject;

public class EditUserRequest extends BaseRequest<LoginInfo> {
    public EditUserRequest(String url, RequestMethod requestMethod, String picture, String nickName, String companyName) throws JSONException {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        if (picture != null) {
            try {
                if (!TextUtils.isEmpty(picture)) {
                    this.mParams.put(PictureConfig.FC_TAG, picture);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mParams.put("nickName", nickName);
        if (!(companyName == null || TextUtils.isEmpty(companyName))) {
            this.mParams.put("companyName", companyName);
        }
        setDefineRequestBodyForJson(this.mParams);
    }
}
