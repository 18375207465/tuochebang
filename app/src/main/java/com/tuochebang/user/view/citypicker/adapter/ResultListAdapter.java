package com.tuochebang.user.view.citypicker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.view.citypicker.model.City;
import java.util.List;

public class ResultListAdapter extends BaseAdapter {
    private List<City> mCities;
    private Context mContext;

    public static class ResultViewHolder {
        TextView name;
    }

    public ResultListAdapter(Context mContext, List<City> mCities) {
        this.mCities = mCities;
        this.mContext = mContext;
    }

    public void changeData(List<City> list) {
        if (this.mCities == null) {
            this.mCities = list;
        } else {
            this.mCities.clear();
            this.mCities.addAll(list);
        }
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mCities == null ? 0 : this.mCities.size();
    }

    public City getItem(int position) {
        return this.mCities == null ? null : (City) this.mCities.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        ResultViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.item_search_result_listview, parent, false);
            holder = new ResultViewHolder();
            holder.name = (TextView) view.findViewById(R.id.tv_item_result_listview_name);
            view.setTag(holder);
        } else {
            holder = (ResultViewHolder) view.getTag();
        }
        holder.name.setText(((City) this.mCities.get(position)).getName());
        return view;
    }
}
