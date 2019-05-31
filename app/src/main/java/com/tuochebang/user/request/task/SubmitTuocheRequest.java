package com.tuochebang.user.request.task;

import com.tuochebang.user.request.base.BaseRequest;
import com.tuochebang.user.request.entity.TuoCheRequestModel;
import com.yanzhenjie.nohttp.RequestMethod;
import org.json.JSONObject;

public class SubmitTuocheRequest extends BaseRequest<String> {
    public SubmitTuocheRequest(String url, RequestMethod requestMethod, TuoCheRequestModel model) {
        super(url, requestMethod);
        this.mParams = new JSONObject();
        try {
            this.mParams.put("corporate", model.getCorporate());
            this.mParams.put("mobile", model.getMobile());
            this.mParams.put("time", model.getTime());
            this.mParams.put("begin", model.getBegin());
            this.mParams.put("end", model.getEnd());
            this.mParams.put("typeId", model.getTypeId());
            this.mParams.put("payId", model.getPayId());
            this.mParams.put("isReturn", model.getIsReturn());
            this.mParams.put("isCrane", model.getIsCrane());
            this.mParams.put("money", model.getMoney());
            this.mParams.put("car", model.getCar());
            this.mParams.put("modelId", model.getModelId());
            this.mParams.put("gearboxId", model.getGearboxId());
            this.mParams.put("driveId", model.getDriveId());
            this.mParams.put("picture0", model.getPicture0());
            this.mParams.put("picture1", model.getPicture1());
            this.mParams.put("picture2", model.getPicture2());
            this.mParams.put("picture3", model.getPicture3());
            this.mParams.put("longitude", model.getLongitude());
            this.mParams.put("latitude", model.getLatitude());
            this.mParams.put("e_longitude", model.getE_longitude());
            this.mParams.put("e_latitude", model.getE_latitude());
            this.mParams.put("otherCar", model.getOtherCar());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDefineRequestBodyForJson(this.mParams);
    }

    protected String parseObject(String jsonData) {
        return "";
    }
}
