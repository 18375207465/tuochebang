package com.tuochebang.user.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build.VERSION;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import com.tuochebang.user.base.BaseActivity;

public abstract class CheckPermissionActivity extends BaseActivity {
    public final int REQUEST_CAMERA_CODE = 81;
    public final int REQUEST_CONTACTS_CODE = 152;
    public final int REQUEST_LOCATIN_CODE = 129;
    public final int REQUEST_READ_CODE = 97;
    public final int REQUEST_SETTING_CODE = 145;
    public final int REQUEST_WRITE_CODE = 113;

    /* renamed from: com.tuochebang.user.ui.CheckPermissionActivity$1 */
    class C09301 implements OnClickListener {
        C09301() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Log.i("info", "8--权限被拒绝,此时不会再回调onRequestPermissionsResult方法");
        }
    }

    /* renamed from: com.tuochebang.user.ui.CheckPermissionActivity$2 */
    class C09312 implements OnClickListener {
        C09312() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Log.i("info", "4,需要用户手动设置，开启当前app设置界面");
            CheckPermissionActivity.this.startAppSettings();
        }
    }

    protected abstract void permissionHasGranted(String str);

    protected void checkPermission(String permission, int resultCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != 0) {
            Log.i("info", "1,需要申请权限。");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Log.i("info", "3,用户已经拒绝过一次该权限，需要提示用户为什么需要该权限。\n此时shouldShowRequestPermissionRationale返回：" + ActivityCompat.shouldShowRequestPermissionRationale(this, permission));
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{permission}, resultCode);
            Log.i("info", "2,用户拒绝过该权限，或者用户从未操作过该权限，开始申请权限。-\n此时shouldShowRequestPermissionRationale返回：" + ActivityCompat.shouldShowRequestPermissionRationale(this, permission));
            return;
        }
        permissionHasGranted(permission);
    }

    protected void directRequestPermisssion(String permission, int resultCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, resultCode);
    }

    protected void showMissingPermissionDialog(String desc) {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "提示");
        builder.setMessage("当前应用缺少" + desc + "。\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
        builder.setNegativeButton((CharSequence) "取消", new C09301());
        builder.setPositiveButton((CharSequence) "设置", new C09312());
        builder.setCancelable(false);
        builder.show();
    }

    private void startAppSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    protected static int checkOp(Context context, int op) {
        if (VERSION.SDK_INT >= 19) {
            Object object = context.getSystemService("appops");
            try {
                return ((Integer) object.getClass().getDeclaredMethod("checkOp", new Class[]{Integer.TYPE, Integer.TYPE, String.class}).invoke(object, new Object[]{Integer.valueOf(op), Integer.valueOf(Binder.getCallingUid()), context.getPackageName()})).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
