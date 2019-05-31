package com.tuochebang.user.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.framework.app.component.utils.Base64;
import com.tuochebang.user.app.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class SharedPreferencesUtil {
    public static final String KEY_APP_FRIST_RUN = "app_frist_run";
    public static final String KEY_PUSH_MSG = "push_msg";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USER = "user";
    private static final String PREFERENCES_FILE = "Touchebang";
    private static final String TAG = SharedPreferencesUtil.class.getSimpleName();
    private static SharedPreferencesUtil mInstance;
    private static SharedPreferences mSharedPreferences;
    private Editor mEditor;

    public static SharedPreferencesUtil getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPreferencesUtil(MyApplication.getInstance());
        }
        return mInstance;
    }

    private SharedPreferencesUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences("Touchebang", 0);
        mEditor = mSharedPreferences.edit();
    }

    public void saveObject(String key, Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(byteArrayOutputStream).writeObject(object);
            String strObjectBase64 = new String(Base64.encodeBase64(byteArrayOutputStream.toByteArray()));
            this.mEditor.putString(key, strObjectBase64);
            this.mEditor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> Object getObject(String key) {
        Object obj = null;
        String strObjectBase64 = mSharedPreferences.getString(key, "");
        if (strObjectBase64.length() == 0) {
        } else {
            try {
                obj = new ObjectInputStream(new ByteArrayInputStream(Base64.decodeBase64(strObjectBase64))).readObject();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        return obj;
    }

    public String getString(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public void putString(String key, String value) {
        this.mEditor.putString(key, value);
        this.mEditor.commit();
    }

    public float getFloat(String key) {
        return mSharedPreferences.getFloat(key, 0.0f);
    }

    public void putFloat(String key, float value) {
        this.mEditor.putFloat(key, value);
        this.mEditor.commit();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public void putLong(String key, long value) {
        this.mEditor.putLong(key, value);
        this.mEditor.commit();
    }

    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public void putInt(String key, int value) {
        this.mEditor.putInt(key, value);
        this.mEditor.commit();
    }

    public boolean getBoolean(String key, boolean defaultBoolean) {
        return mSharedPreferences.getBoolean(key, defaultBoolean);
    }

    public void putBoolean(String key, boolean value) {
        this.mEditor.putBoolean(key, value);
        this.mEditor.commit();
    }

    public void removeValue(String key) {
        this.mEditor.remove(key);
        this.mEditor.commit();
    }

    public void clearAllValues() {
        this.mEditor.clear();
        this.mEditor.commit();
    }
}
