package com.tuochebang.user.util;

import android.content.Context;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.FromAndTo;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import java.util.ArrayList;
import java.util.List;

public class RouteTask implements OnRouteSearchListener {
    private static RouteTask mRouteTask;
    private PositionEntity mFromPoint;
    private List<OnRouteCalculateListener> mListeners = new ArrayList();
    private RouteSearch mRouteSearch;
    private PositionEntity mToPoint;

    public interface OnRouteCalculateListener {
        void onRouteCalculate(float f, float f2, int i);
    }

    public static RouteTask getInstance(Context context) {
        if (mRouteTask == null) {
            mRouteTask = new RouteTask(context);
        }
        return mRouteTask;
    }

    public PositionEntity getStartPoint() {
        return this.mFromPoint;
    }

    public void setStartPoint(PositionEntity fromPoint) {
        this.mFromPoint = fromPoint;
    }

    public PositionEntity getEndPoint() {
        return this.mToPoint;
    }

    public void setEndPoint(PositionEntity toPoint) {
        this.mToPoint = toPoint;
    }

    private RouteTask(Context context) {
        this.mRouteSearch = new RouteSearch(context);
        this.mRouteSearch.setRouteSearchListener(this);
    }

    public void search() {
        if (this.mFromPoint != null && this.mToPoint != null) {
            this.mRouteSearch.calculateDriveRouteAsyn(new DriveRouteQuery(new FromAndTo(new LatLonPoint(this.mFromPoint.latitue, this.mFromPoint.longitude), new LatLonPoint(this.mToPoint.latitue, this.mToPoint.longitude)), 0, null, null, ""));
        }
    }

    public void search(PositionEntity fromPoint, PositionEntity toPoint) {
        this.mFromPoint = fromPoint;
        this.mToPoint = toPoint;
        search();
    }

    public void addRouteCalculateListener(OnRouteCalculateListener listener) {
        synchronized (this) {
            if (this.mListeners.contains(listener)) {
                return;
            }
            this.mListeners.add(listener);
        }
    }

    public void removeRouteCalculateListener(OnRouteCalculateListener listener) {
        synchronized (this) {
            this.mListeners.remove(listener);
        }
    }

    public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
    }

    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int resultCode) {
        if (resultCode == 1000 && driveRouteResult != null) {
            synchronized (this) {
                for (OnRouteCalculateListener listener : this.mListeners) {
                    List<DrivePath> drivepaths = driveRouteResult.getPaths();
                    float distance = 0.0f;
                    int duration = 0;
                    if (drivepaths.size() > 0) {
                        DrivePath drivepath = (DrivePath) drivepaths.get(0);
                        distance = drivepath.getDistance() / 1000.0f;
                        duration = (int) (drivepath.getDuration() / 60);
                    }
                    listener.onRouteCalculate(driveRouteResult.getTaxiCost(), distance, duration);
                }
            }
        }
    }

    public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
    }

    public void onRideRouteSearched(RideRouteResult arg0, int arg1) {
    }
}
