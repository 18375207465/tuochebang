package com.tuochebang.user.ui.returns;

import android.content.Context;
import android.graphics.Color;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.TMC;
import com.tuochebang.user.R;
import com.tuochebang.user.util.AMapUtil;
import java.util.ArrayList;
import java.util.List;

public class DrivingRouteOverLay extends RouteOverlay {
    private DrivePath drivePath;
    private boolean isColorfulline = true;
    private Context mContext;
    private List<LatLng> mLatLngsOfPath;
    private PolylineOptions mPolylineOptions;
    private PolylineOptions mPolylineOptionscolor = null;
    private float mWidth = 15.0f;
    private List<LatLonPoint> throughPointList;
    private List<Marker> throughPointMarkerList = new ArrayList();
    private boolean throughPointMarkerVisible = true;
    private List<TMC> tmcs;

    public void setIsColorfulline(boolean iscolorfulline) {
        this.isColorfulline = iscolorfulline;
    }

    public DrivingRouteOverLay(Context context, AMap amap, DrivePath path, LatLonPoint start, LatLonPoint end, List<LatLonPoint> throughPointList) {
        super(context);
        this.mContext = context;
        this.mAMap = amap;
        this.drivePath = path;
        this.startPoint = AMapUtil.convertToLatLng(start);
        this.endPoint = AMapUtil.convertToLatLng(end);
        this.throughPointList = throughPointList;
    }

    public float getRouteWidth() {
        return this.mWidth;
    }

    public void setRouteWidth(float mWidth) {
        this.mWidth = mWidth;
    }

    public void addToMap() {
        initPolylineOptions();
        try {
            if (this.mAMap != null && this.mWidth != 0.0f && this.drivePath != null) {
                this.mLatLngsOfPath = new ArrayList();
                this.tmcs = new ArrayList();
                List<DriveStep> drivePaths = this.drivePath.getSteps();
                this.mPolylineOptions.add(this.startPoint);
                for (DriveStep step : drivePaths) {
                    List<LatLonPoint> latlonPoints = step.getPolyline();
                    this.tmcs.addAll(step.getTMCs());
                    addDrivingStationMarkers(step, convertToLatLng((LatLonPoint) latlonPoints.get(0)));
                    for (LatLonPoint latlonpoint : latlonPoints) {
                        this.mPolylineOptions.add(convertToLatLng(latlonpoint));
                        this.mLatLngsOfPath.add(convertToLatLng(latlonpoint));
                    }
                }
                this.mPolylineOptions.add(this.endPoint);
                if (this.startMarker != null) {
                    this.startMarker.remove();
                    this.startMarker = null;
                }
                if (this.endMarker != null) {
                    this.endMarker.remove();
                    this.endMarker = null;
                }
                addStartAndEndMarker();
                addThroughPointMarker();
                if (!this.isColorfulline || this.tmcs.size() <= 0) {
                    showPolyline();
                } else {
                    colorWayUpdate(this.tmcs);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void initPolylineOptions() {
        this.mPolylineOptions = null;
        this.mPolylineOptions = new PolylineOptions();
        this.mPolylineOptions.color(getDriveColor()).width(getRouteWidth());
    }

    private void showPolyline() {
        addPolyLine(this.mPolylineOptions);
    }

    private void colorWayUpdate(List<TMC> tmcSection) {
        if (this.mAMap != null && tmcSection != null && tmcSection.size() > 0) {
            addPolyLine(new PolylineOptions().add(this.startPoint, AMapUtil.convertToLatLng((LatLonPoint) ((TMC) tmcSection.get(0)).getPolyline().get(0))).setDottedLine(true));
            String status = "";
            for (int i = 0; i < tmcSection.size(); i++) {
                TMC segmentTrafficStatus = (TMC) tmcSection.get(i);
                List<LatLonPoint> mployline = segmentTrafficStatus.getPolyline();
                int j;
                if (status.equals(segmentTrafficStatus.getStatus())) {
                    for (j = 1; j < mployline.size(); j++) {
                        this.mPolylineOptionscolor.add(AMapUtil.convertToLatLng((LatLonPoint) mployline.get(j)));
                    }
                } else {
                    if (this.mPolylineOptionscolor != null) {
                        addPolyLine(this.mPolylineOptionscolor.color(getcolor(status)));
                    }
                    this.mPolylineOptionscolor = null;
                    this.mPolylineOptionscolor = new PolylineOptions().width(getRouteWidth());
                    status = segmentTrafficStatus.getStatus();
                    for (j = 0; j < mployline.size(); j++) {
                        this.mPolylineOptionscolor.add(AMapUtil.convertToLatLng((LatLonPoint) mployline.get(j)));
                    }
                }
                if (i == tmcSection.size() - 1 && this.mPolylineOptionscolor != null) {
                    addPolyLine(this.mPolylineOptionscolor.color(getcolor(status)));
                    addPolyLine(new PolylineOptions().add(AMapUtil.convertToLatLng((LatLonPoint) mployline.get(mployline.size() - 1)), this.endPoint).setDottedLine(true));
                }
            }
        }
    }

    private int getcolor(String status) {
        if (status.equals("畅通")) {
            return Color.parseColor("#1FBBED");
        }
        if (status.equals("缓行")) {
            return Color.parseColor("#1FBBED");
        }
        if (status.equals("拥堵")) {
            return Color.parseColor("#1FBBED");
        }
        if (status.equals("严重拥堵")) {
            return Color.parseColor("#1FBBED");
        }
        return Color.parseColor("#1FBBED");
    }

    public LatLng convertToLatLng(LatLonPoint point) {
        return new LatLng(point.getLatitude(), point.getLongitude());
    }

    private void addDrivingStationMarkers(DriveStep driveStep, LatLng latLng) {
        addStationMarker(new MarkerOptions().position(latLng).title("方向:" + driveStep.getAction() + "\n道路:" + driveStep.getRoad()).snippet(driveStep.getInstruction()).visible(this.nodeIconVisible).anchor(0.5f, 0.5f).icon(getDriveBitmapDescriptor()));
    }

    protected LatLngBounds getLatLngBounds() {
        Builder b = LatLngBounds.builder();
        b.include(new LatLng(this.startPoint.latitude, this.startPoint.longitude));
        b.include(new LatLng(this.endPoint.latitude, this.endPoint.longitude));
        if (this.throughPointList != null && this.throughPointList.size() > 0) {
            for (int i = 0; i < this.throughPointList.size(); i++) {
                b.include(new LatLng(((LatLonPoint) this.throughPointList.get(i)).getLatitude(), ((LatLonPoint) this.throughPointList.get(i)).getLongitude()));
            }
        }
        return b.build();
    }

    public void setThroughPointIconVisibility(boolean visible) {
        try {
            this.throughPointMarkerVisible = visible;
            if (this.throughPointMarkerList != null && this.throughPointMarkerList.size() > 0) {
                for (int i = 0; i < this.throughPointMarkerList.size(); i++) {
                    ((Marker) this.throughPointMarkerList.get(i)).setVisible(visible);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void addThroughPointMarker() {
        if (this.throughPointList != null && this.throughPointList.size() > 0) {
            for (int i = 0; i < this.throughPointList.size(); i++) {
                LatLonPoint latLonPoint = (LatLonPoint) this.throughPointList.get(i);
                if (latLonPoint != null) {
                    this.throughPointMarkerList.add(this.mAMap.addMarker(new MarkerOptions().position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude())).visible(this.throughPointMarkerVisible).icon(getThroughPointBitDes()).title("途经点")));
                }
            }
        }
    }

    private BitmapDescriptor getThroughPointBitDes() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_through);
    }

    public static int calculateDistance(LatLng start, LatLng end) {
        return calculateDistance(start.longitude, start.latitude, end.longitude, end.latitude);
    }

    public static int calculateDistance(double x1, double y1, double x2, double y2) {
        x1 *= 0.01745329251994329d;
        y1 *= 0.01745329251994329d;
        x2 *= 0.01745329251994329d;
        y2 *= 0.01745329251994329d;
        double sinx1 = Math.sin(x1);
        double siny1 = Math.sin(y1);
        double cosx1 = Math.cos(x1);
        double cosy1 = Math.cos(y1);
        double sinx2 = Math.sin(x2);
        double siny2 = Math.sin(y2);
        double cosx2 = Math.cos(x2);
        double cosy2 = Math.cos(y2);
        double[] v1 = new double[]{(cosy1 * cosx1) - (cosy2 * cosx2), (cosy1 * sinx1) - (cosy2 * sinx2), siny1 - siny2};
        return (int) (Math.asin(Math.sqrt(((v1[0] * v1[0]) + (v1[1] * v1[1])) + (v1[2] * v1[2])) / 2.0d) * 1.27420015798544E7d);
    }

    public static LatLng getPointForDis(LatLng sPt, LatLng ePt, double dis) {
        double preResult = dis / ((double) calculateDistance(sPt, ePt));
        return new LatLng(((ePt.latitude - sPt.latitude) * preResult) + sPt.latitude, ((ePt.longitude - sPt.longitude) * preResult) + sPt.longitude);
    }

    public void removeFromMap() {
        try {
            super.removeFromMap();
            if (this.throughPointMarkerList != null && this.throughPointMarkerList.size() > 0) {
                for (int i = 0; i < this.throughPointMarkerList.size(); i++) {
                    ((Marker) this.throughPointMarkerList.get(i)).remove();
                }
                this.throughPointMarkerList.clear();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
