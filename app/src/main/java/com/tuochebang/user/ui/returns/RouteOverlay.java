package com.tuochebang.user.ui.returns;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.LatLngBounds.Builder;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.tuochebang.user.R;

import java.util.ArrayList;
import java.util.List;

public class RouteOverlay {
    protected List<Polyline> allPolyLines = new ArrayList();
    private Bitmap busBit;
    private Bitmap driveBit;
    private Bitmap endBit;
    protected Marker endMarker;
    protected LatLng endPoint;
    protected AMap mAMap;
    private Context mContext;
    protected boolean nodeIconVisible = true;
    private Bitmap startBit;
    protected Marker startMarker;
    protected LatLng startPoint;
    protected List<Marker> stationMarkers = new ArrayList();
    private Bitmap walkBit;

    public RouteOverlay(Context context) {
        this.mContext = context;
    }

    public void removeFromMap() {
        if (this.startMarker != null) {
            this.startMarker.remove();
        }
        if (this.endMarker != null) {
            this.endMarker.remove();
        }
        for (Marker marker : this.stationMarkers) {
            marker.remove();
        }
        for (Polyline line : this.allPolyLines) {
            line.remove();
        }
        destroyBit();
    }

    private void destroyBit() {
        if (this.startBit != null) {
            this.startBit.recycle();
            this.startBit = null;
        }
        if (this.endBit != null) {
            this.endBit.recycle();
            this.endBit = null;
        }
        if (this.busBit != null) {
            this.busBit.recycle();
            this.busBit = null;
        }
        if (this.walkBit != null) {
            this.walkBit.recycle();
            this.walkBit = null;
        }
        if (this.driveBit != null) {
            this.driveBit.recycle();
            this.driveBit = null;
        }
    }

    protected BitmapDescriptor getStartBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.icon_starting);
    }

    protected BitmapDescriptor getEndBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.icon_position);
    }

    protected BitmapDescriptor getBusBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_bus);
    }

    protected BitmapDescriptor getWalkBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_man);
    }

    protected BitmapDescriptor getDriveBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.mipmap.amap_car);
    }

    public void addStartAndEndMarker() {
        this.startMarker = this.mAMap.addMarker(new MarkerOptions().position(this.startPoint).icon(getStartBitmapDescriptor()).title("起点"));
        this.endMarker = this.mAMap.addMarker(new MarkerOptions().position(this.endPoint).icon(getEndBitmapDescriptor()).title("终点"));
    }

    public void zoomToSpan() {
        if (this.startPoint != null && this.mAMap != null) {
            try {
                this.mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(getLatLngBounds(), 70));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    protected LatLngBounds getLatLngBounds() {
        Builder b = LatLngBounds.builder();
        b.include(new LatLng(this.startPoint.latitude, this.startPoint.longitude));
        b.include(new LatLng(this.endPoint.latitude, this.endPoint.longitude));
        return b.build();
    }

    public void setNodeIconVisibility(boolean visible) {
        try {
            this.nodeIconVisible = visible;
            if (this.stationMarkers != null && this.stationMarkers.size() > 0) {
                for (int i = 0; i < this.stationMarkers.size(); i++) {
                    ((Marker) this.stationMarkers.get(i)).setVisible(visible);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void addStationMarker(MarkerOptions options) {
        if (options != null) {
            Marker marker = this.mAMap.addMarker(options);
            if (marker != null) {
                this.stationMarkers.add(marker);
            }
        }
    }

    protected void addPolyLine(PolylineOptions options) {
        if (options != null) {
            Polyline polyline = this.mAMap.addPolyline(options);
            if (polyline != null) {
                this.allPolyLines.add(polyline);
            }
        }
    }

    protected float getRouteWidth() {
        return 13.0f;
    }

    protected int getWalkColor() {
        return Color.parseColor("#6db74d");
    }

    protected int getBusColor() {
        return Color.parseColor("#537edc");
    }

    protected int getDriveColor() {
        return Color.parseColor("#1FBBED");
    }
}
