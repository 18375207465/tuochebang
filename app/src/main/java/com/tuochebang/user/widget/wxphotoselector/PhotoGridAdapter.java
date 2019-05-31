package com.tuochebang.user.widget.wxphotoselector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.user.widget.wxphotoselector.PhotoGridAdapterView.PhotoOnClickLister;

public class PhotoGridAdapter extends CommonBaseAdapter<String> {
    private String mDirPath;
    private PhotoOnClickLister mPhotoOnClickLister;

    public PhotoGridAdapter(Context context) {
        super(context);
    }

    public void setDirPath(String dirPath) {
        this.mDirPath = dirPath;
    }

    public void setPhotoOnClickLister(PhotoOnClickLister photoOnClickLister) {
        this.mPhotoOnClickLister = photoOnClickLister;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new PhotoGridAdapterView(this.mContext);
        }
        PhotoGridAdapterView photoGridAdapterView = (PhotoGridAdapterView) convertView;
        photoGridAdapterView.setDirPath(this.mDirPath);
        photoGridAdapterView.setPhotoOnClickLister(this.mPhotoOnClickLister);
        photoGridAdapterView.refreshView((String) getItem(position));
        return photoGridAdapterView;
    }
}
