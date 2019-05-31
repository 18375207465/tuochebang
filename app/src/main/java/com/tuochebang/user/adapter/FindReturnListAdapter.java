package com.tuochebang.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.user.adapter.adapterview.FindReturnCarAdapterView;
import com.tuochebang.user.request.entity.ReturnCarInfo;

public class FindReturnListAdapter extends CommonBaseAdapter<ReturnCarInfo> {
    public FindReturnListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new FindReturnCarAdapterView(this.mContext);
        }
        ((FindReturnCarAdapterView) convertView).refreshView((ReturnCarInfo) getItem(position));
        return convertView;
    }
}
