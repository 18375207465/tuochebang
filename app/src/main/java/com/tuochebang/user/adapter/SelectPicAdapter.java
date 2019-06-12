package com.tuochebang.user.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.framework.app.component.optimize.GridViewForScrollView;
import com.framework.app.component.utils.DensityUtil;
import com.framework.app.component.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tuochebang.user.R;
import com.tuochebang.user.ui.request.PublishRequestActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class SelectPicAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private final Context mContext;
    private int selectedPosition = -1;
    private HashMap<Integer, View> viewMap = new HashMap<>();

    public class ViewHolder {
        public ImageView image;
    }

    public SelectPicAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public int getCount() {
        return PublishRequestActivity.urlCount == 4 ? 4 : PublishRequestActivity.urlCount + 1;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return new Random().nextInt();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
//        int coord = position;
//        if (convertView == null) {
//            convertView = this.inflater.inflate(R.layout.adapter_item_select_pic, parent, false);
//            holder = new ViewHolder();
//            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
        if (!viewMap.containsKey(position) || viewMap.get(position) == null) {
            convertView = this.inflater.inflate(R.layout.adapter_item_select_pic, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            convertView = viewMap.get(position);
            holder = (ViewHolder) convertView.getTag();
        }

        if ((parent instanceof GridViewForScrollView)) {
            if (position == PublishRequestActivity.urlCount) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), R.mipmap.icon_add));
                if (position == 4) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                String uri = PublishRequestActivity.urlUploads[position];
                if (!uri.contains("http")) {
                    uri = ImageLoaderUtil.getUriFromLocalFile(new File(uri));
                }
                ImageLoader.getInstance().displayImage(uri, holder.image, ImageLoaderUtil.getDisplayImageOptions(R.mipmap.logo));
            }
        }
        return convertView;
    }
}
