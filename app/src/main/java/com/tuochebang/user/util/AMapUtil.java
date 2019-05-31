package com.tuochebang.user.util;

import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;
import cn.jiguang.api.utils.ByteBufferUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteRailwayItem;
import com.framework.app.component.utils.DateUtil;
import com.tuochebang.user.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AMapUtil {
    public static final String HtmlBlack = "#000000";
    public static final String HtmlGray = "#808080";

    public static String checkEditText(EditText editText) {
        if (editText == null || editText.getText() == null || editText.getText().toString().trim().equals("")) {
            return "";
        }
        return editText.getText().toString().trim();
    }

    public static Spanned stringToSpan(String src) {
        return src == null ? null : Html.fromHtml(src.replace("\n", "<br />"));
    }

    public static String colorFont(String src, String color) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("<font color=").append(color).append(">").append(src).append("</font>");
        return strBuf.toString();
    }

    public static String makeHtmlNewLine() {
        return "<br />";
    }

    public static String makeHtmlSpace(int number) {
        String space = "&nbsp;";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < number; i++) {
            result.append("&nbsp;");
        }
        return result.toString();
    }

    public static String getFriendlyLength(int lenMeter) {
        if (lenMeter > ByteBufferUtils.ERROR_CODE) {
            return (lenMeter / 1000) + ChString.Kilometer;
        } else if (lenMeter > 1000) {
            return new DecimalFormat("##0.0").format((double) (((float) lenMeter) / 1000.0f)) + ChString.Kilometer;
        } else if (lenMeter > 100) {
            return ((lenMeter / 50) * 50) + ChString.Meter;
        } else {
            int dis = (lenMeter / 10) * 10;
            if (dis == 0) {
                dis = 10;
            }
            return dis + ChString.Meter;
        }
    }

    public static boolean IsEmptyOrNullString(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList();
        for (LatLonPoint point : shapes) {
            lineShapes.add(convertToLatLng(point));
        }
        return lineShapes;
    }

    public static String convertToTime(long time) {
        return new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS).format(new Date(time));
    }

    public static String getFriendlyTime(int second) {
        if (second > 3600) {
            return (second / 3600) + "小时" + ((second % 3600) / 60) + "分钟";
        } else if (second < 60) {
            return second + "秒";
        } else {
            return (second / 60) + "分钟";
        }
    }

    public static int getDriveActionID(String actionName) {
        if (actionName == null || actionName.equals("")) {
            return R.mipmap.dir3;
        }
        if ("左转".equals(actionName)) {
            return R.mipmap.dir2;
        }
        if ("右转".equals(actionName)) {
            return R.mipmap.dir1;
        }
        if ("向左前方行驶".equals(actionName) || "靠左".equals(actionName)) {
            return R.mipmap.dir6;
        }
        if ("向右前方行驶".equals(actionName) || "靠右".equals(actionName)) {
            return R.mipmap.dir5;
        }
        if ("向左后方行驶".equals(actionName) || "左转调头".equals(actionName)) {
            return R.mipmap.dir7;
        }
        if ("向右后方行驶".equals(actionName)) {
            return R.mipmap.dir8;
        }
        if ("直行".equals(actionName) || !"减速行驶".equals(actionName)) {
            return R.mipmap.dir3;
        }
        return R.mipmap.dir4;
    }

    public static int getWalkActionID(String actionName) {
        if (actionName == null || actionName.equals("")) {
            return R.mipmap.dir13;
        }
        if ("左转".equals(actionName)) {
            return R.mipmap.dir2;
        }
        if ("右转".equals(actionName)) {
            return R.mipmap.dir1;
        }
        if ("向左前方".equals(actionName) || "靠左".equals(actionName)) {
            return R.mipmap.dir6;
        }
        if ("向右前方".equals(actionName) || "靠右".equals(actionName)) {
            return R.mipmap.dir5;
        }
        if ("向左后方".equals(actionName)) {
            return R.mipmap.dir7;
        }
        if ("向右后方".equals(actionName)) {
            return R.mipmap.dir8;
        }
        if ("直行".equals(actionName)) {
            return R.mipmap.dir3;
        }
        if ("通过人行横道".equals(actionName)) {
            return R.mipmap.dir9;
        }
        if ("通过过街天桥".equals(actionName)) {
            return R.mipmap.dir11;
        }
        if ("通过地下通道".equals(actionName)) {
            return R.mipmap.dir10;
        }
        return R.mipmap.dir13;
    }

    public static String getBusPathTitle(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        List<BusStep> busSetps = busPath.getSteps();
        if (busSetps == null) {
            return String.valueOf("");
        }
        StringBuffer sb = new StringBuffer();
        for (BusStep busStep : busSetps) {
            StringBuffer title = new StringBuffer();
            if (busStep.getBusLines().size() > 0) {
                for (RouteBusLineItem busline : busStep.getBusLines()) {
                    if (busline != null) {
                        title.append(getSimpleBusLineName(busline.getBusLineName()));
                        title.append(" / ");
                    }
                }
                sb.append(title.substring(0, title.length() - 3));
                sb.append(" > ");
            }
            if (busStep.getRailway() != null) {
                RouteRailwayItem railway = busStep.getRailway();
                sb.append(railway.getTrip() + "(" + railway.getDeparturestop().getName() + " - " + railway.getArrivalstop().getName() + ")");
                sb.append(" > ");
            }
        }
        return sb.substring(0, sb.length() - 3);
    }

    public static String getBusPathDes(BusPath busPath) {
        if (busPath == null) {
            return String.valueOf("");
        }
        String time = getFriendlyTime((int) busPath.getDuration());
        String subDis = getFriendlyLength((int) busPath.getDistance());
        return String.valueOf(time + " | " + subDis + " | 步行" + getFriendlyLength((int) busPath.getWalkDistance()));
    }

    public static String getSimpleBusLineName(String busLineName) {
        if (busLineName == null) {
            return String.valueOf("");
        }
        return busLineName.replaceAll("\\(.*?\\)", "");
    }
}
