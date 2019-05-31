package com.tuochebang.user.request.base;

public class ServerUrl {
    public static final String IMAGE = "image";
    public static final String MESSAGE = "message";
    private static final String REQUEST = "request";
    private static final String RETURNS = "returns";
    private static final String SETTING = "setting";
    private static String URL_PATH = "/tuochebang/rest/client/v1.3.0/";
    private static String URL_RELASE = "http://api.tuocb.com";
    private static String URL_TEST = "http://115.28.244.177:8188";
    public static String URL_UPLOAD = "http://tuochebang2.oss-cn-qingdao.aliyuncs.com";
    public static final String USER = "user";
    private static ServerUrl mServerUrl;

    public static ServerUrl getInst() {
        if (mServerUrl == null) {
            mServerUrl = new ServerUrl();
        }
        return mServerUrl;
    }

    public static String getBaseUrlPath() {
        return URL_RELASE + URL_PATH;
    }

    public String getTcbDomain(String modulekey) {
        String rootUrl = getBaseUrlPath();
        if (modulekey.equals("user")) {
            return rootUrl + "user/";
        }
        if (modulekey.equals("image")) {
            return rootUrl + "image/";
        }
        if (modulekey.equals(MESSAGE)) {
            return rootUrl + "message/";
        }
        if (modulekey.equals(REQUEST)) {
            return rootUrl + "request/";
        }
        if (modulekey.equals(RETURNS)) {
            return rootUrl + "tuochereturn/";
        }
        if (modulekey.equals(SETTING)) {
            return rootUrl + "setting/";
        }
        return rootUrl;
    }

    public String USER_LOGIN_URL() {
        return getTcbDomain("user") + "login";
    }

    public String UPDATE_USER_INFO_URL() {
        return getTcbDomain("user") + "update/token";
    }

    public String USER_INFO_URL() {
        return getTcbDomain("user") + "info/token";
    }

    public String FEEDBACK_URL() {
        return getTcbDomain(SETTING) + "submit";
    }

    public String UPLOAD_IMAGE() {
        return getTcbDomain("image") + "upload";
    }

    public String GET_PAY_METHOD() {
        return getTcbDomain("") + "pay/type";
    }

    public String GET_CAR_TYPE_METHOD() {
        return getTcbDomain("") + "car/type";
    }

    public String GET_REQUEST_TYPE() {
        return getTcbDomain("") + "request/type";
    }

    public String SUBMIT_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "submit/token";
    }

    public String EDIT_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "edit/token";
    }

    public String DELETE_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "delete/token";
    }

    public String CONFIRM_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "finish/token";
    }

    public String CONFIRM_TUOCHE_RETURN() {
        return getTcbDomain(RETURNS) + "finish/token";
    }

    public String GET_TUOCHE_REQUEST() {
        return getTcbDomain(REQUEST) + "query/token";
    }

    public String GET_TUOCHE_RETURNS() {
        return getTcbDomain(RETURNS) + "query/token";
    }

    public String APPLY_RETURNS() {
        return getTcbDomain(RETURNS) + "rob/token";
    }

    public String GET_TUOCHE_MESSAGE() {
        return getTcbDomain(MESSAGE) + "query/token";
    }

    public String GET_TUOCHE_DETAIL_MESSAGE() {
        return getTcbDomain(MESSAGE) + "queryinfo/token";
    }

    public String GET_TUOCHE_REGISTER() {
        return getTcbDomain("user") + "register";
    }

    public String ABOUT_URL() {
        return "http://api.tuocb.com/tuochebang/client/v1.0/activity/aboutc";
    }

    public String SERVICE_LIENCE() {
        return "http://api.tuocb.com/tuochebang/client/v1.0/activity/fuwuxieyi";
    }
}
