package com.tuochebang.user.view.citypicker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.view.citypicker.model.City;
import com.tuochebang.user.view.citypicker.model.LocateState;
import com.tuochebang.user.view.citypicker.utils.PinyinUtils;
import com.tuochebang.user.view.citypicker.view.WrapHeightGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;
    private LayoutInflater inflater;
    private HashMap<String, Integer> letterIndexes;
    private int locateState = 111;
    private String locatedCity;
    private List<City> mCities;
    private Context mContext;
    private OnCityClickListener onCityClickListener;
    private String[] sections;

    public interface OnCityClickListener {
        void onCityClick(String str);

        void onLocateClick();
    }

    /* renamed from: com.tuochebang.user.view.citypicker.adapter.CityListAdapter$1 */
    class C10381 implements OnClickListener {
        C10381() {
        }

        public void onClick(View v) {
            if (CityListAdapter.this.locateState == LocateState.FAILED) {
                if (CityListAdapter.this.onCityClickListener != null) {
                    CityListAdapter.this.onCityClickListener.onLocateClick();
                }
            } else if (CityListAdapter.this.locateState == LocateState.SUCCESS && CityListAdapter.this.onCityClickListener != null) {
                CityListAdapter.this.onCityClickListener.onCityClick(CityListAdapter.this.locatedCity);
            }
        }
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }

    public CityListAdapter(Context mContext, List<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null) {
            mCities = new ArrayList();
        }
        mCities.add(0, new City("定位", "0"));
        mCities.add(1, new City("热门", "1"));
        int size = mCities.size();
        this.letterIndexes = new HashMap();
        this.sections = new String[size];
        int index = 0;
        while (index < size) {
            String currentLetter = PinyinUtils.getFirstLetter(((City) mCities.get(index)).getPinyin());
            if (!TextUtils.equals(currentLetter, index >= 1 ? PinyinUtils.getFirstLetter(((City) mCities.get(index - 1)).getPinyin()) : "")) {
                this.letterIndexes.put(currentLetter, Integer.valueOf(index));
                this.sections[index] = currentLetter;
            }
            index++;
        }
    }

    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    public int getLetterPosition(String letter) {
        Integer integer = (Integer) this.letterIndexes.get(letter);
        return integer == null ? -1 : integer.intValue();
    }

    public int getViewTypeCount() {
        return 3;
    }

    public int getItemViewType(int position) {
        return position < 2 ? position : 2;
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
        switch (getItemViewType(position)) {
            case 0:
                view = this.inflater.inflate(R.layout.view_locate_city, parent, false);
                ViewGroup container = (ViewGroup) view.findViewById(R.id.layout_locate);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (this.locateState) {
                    case 111:
                        state.setText(this.mContext.getString(R.string.locating));
                        break;
                    case LocateState.FAILED /*666*/:
                        state.setText(R.string.located_failed);
                        break;
                    case LocateState.SUCCESS /*888*/:
                        state.setText(this.locatedCity);
                        break;
                }
                container.setOnClickListener(new C10381());
                return view;
            case 1:
                view = this.inflater.inflate(R.layout.view_hot_city, parent, false);
                WrapHeightGridView gridView = (WrapHeightGridView) view.findViewById(R.id.gridview_hot_city);
                final HotCityGridAdapter hotCityGridAdapter = new HotCityGridAdapter(this.mContext);
                gridView.setAdapter(hotCityGridAdapter);
                gridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        if (CityListAdapter.this.onCityClickListener != null) {
                            CityListAdapter.this.onCityClickListener.onCityClick(hotCityGridAdapter.getItem(position));
                        }
                    }
                });
                return view;
            case 2:
                CityViewHolder holder;
                if (view == null) {
                    view = this.inflater.inflate(R.layout.item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position < 1) {
                    return view;
                }
                final String city = ((City) this.mCities.get(position)).getName();
                holder.name.setText(city);
                String currentLetter = PinyinUtils.getFirstLetter(((City) this.mCities.get(position)).getPinyin());
                if (TextUtils.equals(currentLetter, position >= 1 ? PinyinUtils.getFirstLetter(((City) this.mCities.get(position - 1)).getPinyin()) : "")) {
                    holder.letter.setVisibility(View.GONE);
                } else {
                    holder.letter.setVisibility(View.VISIBLE);
                    holder.letter.setText(currentLetter);
                }
                holder.name.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (CityListAdapter.this.onCityClickListener != null) {
                            CityListAdapter.this.onCityClickListener.onCityClick(city);
                        }
                    }
                });
                return view;
            default:
                return view;
        }
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }
}
