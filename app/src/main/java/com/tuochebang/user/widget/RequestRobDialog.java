package com.tuochebang.user.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tuochebang.user.R;

public class RequestRobDialog extends Dialog {
    private LinearLayout mLLChoice;
    private TextView mTvTitle;

    public interface DialogButtonInterface {
        void onDialogButtonClick(DialogResult dialogResult);
    }

    /* renamed from: com.tuochebang.user.widget.RequestRobDialog$1 */
    class C10541 implements View.OnClickListener {
        C10541() {
        }
        @Override
        public void onClick(View v) {
            RequestRobDialog.this.dismiss();

        }
    }

    /* renamed from: com.tuochebang.user.widget.RequestRobDialog$2 */
    class C10552 implements View.OnClickListener {
        C10552() {
        }


        @Override
        public void onClick(View v) {
            RequestRobDialog.this.dismiss();

        }
    }

    public enum DialogResult {
        Yes,
        No
    }

    public RequestRobDialog(Context context, String title, String content, boolean isContentHtml) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C10541());
        this.mTvTitle.setText(title);
        setCanceledOnTouchOutside(true);
    }

    public RequestRobDialog(Context context, String title, String content) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.ll_dismiss).setOnClickListener(new C10552());
        this.mTvTitle.setText(title);
        setCanceledOnTouchOutside(true);
    }

    public RequestRobDialog(Context context, String title, String content, final DialogButtonInterface dialogButtonInterface) {
        super(context, R.style.DialogMainFullScreen);
        requestWindowFeature(1);
        setContentView(R.layout.dialog_push_request);
        this.mLLChoice = (LinearLayout) findViewById(R.id.ll_choice);
        this.mTvTitle = (TextView) findViewById(R.id.tv_title);
        this.mLLChoice.setVisibility(View.VISIBLE);
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                RequestRobDialog.this.dismiss();
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                RequestRobDialog.this.dismiss();
                dialogButtonInterface.onDialogButtonClick(DialogResult.Yes);
            }
        });
        setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                dialogButtonInterface.onDialogButtonClick(DialogResult.No);
            }
        });
        setCanceledOnTouchOutside(false);
    }
}
