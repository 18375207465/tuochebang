package com.tuochebang.user.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.tuochebang.user.MainActivity;

public class JPushReceiver extends BroadcastReceiver {
    private final String TAG = "JPushReceiver";

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("JPushReceiver", "onReceive - " + intent.getAction());
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.d("JPushReceiver", "[MyReceiver] 接收Registration Id : " + bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d("JPushReceiver", "收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d("JPushReceiver", "收到了通知");
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d("JPushReceiver", "用户点击打开了通知");
            Intent i = new Intent(context, MainActivity.class);
            i.setFlags(268435456);
            context.startActivity(i);
        } else {
            Log.d("JPushReceiver", "Unhandled intent - " + intent.getAction());
        }
    }
}
