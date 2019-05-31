package com.tuochebang.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.framework.app.component.adapter.CommonBaseAdapter;
import com.tuochebang.user.adapter.adapterview.FilterTypeListAdapterView;
import com.tuochebang.user.adapter.adapterview.RequestPayListAdapterView;
import com.tuochebang.user.adapter.adapterview.RequestPayListAdapterView.onCheckListener;
import com.tuochebang.user.request.entity.ModelType;
import com.tuochebang.user.ui.SelectItemActivity;

public class FilterTypeListAdapter extends CommonBaseAdapter<ModelType> {
    private ModelType mSelectFilterName = new ModelType();
    private String mType = "";

    /* renamed from: com.tuochebang.user.adapter.FilterTypeListAdapter$1 */
    class C09191 implements onCheckListener {
        C09191() {
        }

        public void checkListener() {
            FilterTypeListAdapter.this.notifyDataSetChanged();
        }
    }

    public FilterTypeListAdapter(Context context) {
        super(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.mType.equals(SelectItemActivity.PAY_TYPE)) {
            if (convertView == null) {
                convertView = new RequestPayListAdapterView(this.mContext);
            }
            ((RequestPayListAdapterView) convertView).setSelectFilterName(this.mSelectFilterName);
            ((RequestPayListAdapterView) convertView).refreshView((ModelType) getItem(position), position);
            ((RequestPayListAdapterView) convertView).setOnCheckListener(new C09191());
        } else {
            if (convertView == null) {
                convertView = new FilterTypeListAdapterView(this.mContext);
            }
            ((FilterTypeListAdapterView) convertView).setSelectFilterName(this.mSelectFilterName);
            ((FilterTypeListAdapterView) convertView).refreshView((ModelType) getItem(position));
        }
        return convertView;
    }

    public void setSelectFilterName(ModelType selectFilterName) {
        this.mSelectFilterName = selectFilterName;
        notifyDataSetChanged();
    }

    public void setType(String type) {
        this.mType = type;
    }
}
