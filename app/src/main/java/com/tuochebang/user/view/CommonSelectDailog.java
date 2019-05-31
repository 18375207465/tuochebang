package com.tuochebang.user.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.tuochebang.user.R;
import com.tuochebang.user.adapter.SelectedDataAdapter;

import java.util.List;

public class CommonSelectDailog {
    private AlertDialog mAlertDialog;
    private Context mContext;
    private ListView mListOptions;
    private OnItemClickedListener mOnItemClickListener;
    private TextView mTxtViewMessage;
    private SelectedDataAdapter optionAdapter;

    public interface OnItemClickedListener {
        void onItemClick(int i, String str);
    }

    /* renamed from: com.tuochebang.user.view.CommonSelectDailog$1 */
    class C10321 implements OnItemClickListener {
        C10321() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
            if (CommonSelectDailog.this.mOnItemClickListener != null) {
                CommonSelectDailog.this.mOnItemClickListener.onItemClick(position, (String) CommonSelectDailog.this.optionAdapter.getItem(position));
            }
        }
    }

    public CommonSelectDailog(Context context, List<String> dataList) {
        this.mContext = context;
        this.mAlertDialog = new Builder(context).create();
        this.mAlertDialog.show();
        Window window = this.mAlertDialog.getWindow();
        window.setContentView(R.layout.dialog_select_list);
        this.mTxtViewMessage = (TextView) window.findViewById(R.id.tv_message);
        this.mListOptions = (ListView) window.findViewById(R.id.lv_options);
        this.optionAdapter = new SelectedDataAdapter(context);
        this.optionAdapter.setList(dataList);
        this.mListOptions.setAdapter(this.optionAdapter);
        this.mListOptions.setOnItemClickListener(new C10321());
    }

    public void setOnItemClickedListener(OnItemClickedListener l) {
        this.mOnItemClickListener = l;
    }

    public void show() {
        if (!this.mAlertDialog.isShowing()) {
            this.mAlertDialog.show();
        }
    }

    public void dismiss() {
        this.mAlertDialog.dismiss();
    }

    public void setMessage(String msg) {
        this.mTxtViewMessage.setText(msg);
    }
}
