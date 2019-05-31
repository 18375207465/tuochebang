package com.tuochebang.user;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.framework.app.component.utils.ActivityUtil;
import com.tuochebang.user.util.SharedPreferencesUtil;

public class SplashActivity extends Activity {
    private String TAG = "SplashActivity";
    private Uri uri;

    /* renamed from: com.tuochebang.user.SplashActivity$1 */
    class C09171 implements Runnable {
        C09171() {
        }

        public void run() {
            SplashActivity.this.toNextActivity();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getIntentExtras();
        new Handler().postDelayed(new C09171(), 2000);
    }

    private void getIntentExtras() {
    }

    private void toNextActivity() {
        Bundle bundle = new Bundle();
        if (SharedPreferencesUtil.getInstance().getBoolean(SharedPreferencesUtil.KEY_APP_FRIST_RUN, false)) {
            ActivityUtil.next(this, MainActivity.class, bundle, true);
        } else {
            ActivityUtil.next(this, WelcomeActivity.class, bundle, true);
        }
    }
}
