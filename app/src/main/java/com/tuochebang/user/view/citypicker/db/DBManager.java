package com.tuochebang.user.view.citypicker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import com.tuochebang.user.view.citypicker.model.City;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DBManager {
    private static final String ASSETS_NAME = "china_cities.db";
    private static final int BUFFER_SIZE = 1024;
    private static final String DB_NAME = "china_cities.db";
    private static final String NAME = "name";
    private static final String PINYIN = "pinyin";
    private static final String TABLE_NAME = "city";
    private String DB_PATH;
    private Context mContext;

    private class CityComparator implements Comparator<City> {
        private CityComparator() {
        }

        public int compare(City lhs, City rhs) {
            return lhs.getPinyin().substring(0, 1).compareTo(rhs.getPinyin().substring(0, 1));
        }
    }

    public DBManager(Context context) {
        this.mContext = context;
        this.DB_PATH = File.separator + "data" + Environment.getDataDirectory().getAbsolutePath() + File.separator + context.getPackageName() + File.separator + "databases" + File.separator;
    }

    public void copyDBFile() {
        File dir = new File(this.DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(this.DB_PATH + "china_cities.db");
        if (!dbFile.exists()) {
            try {
                InputStream is = this.mContext.getResources().getAssets().open("china_cities.db");
                OutputStream os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = is.read(buffer, 0, buffer.length);
                    if (length > 0) {
                        os.write(buffer, 0, length);
                    } else {
                        os.flush();
                        os.close();
                        is.close();
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(this.DB_PATH + "china_cities.db", null);
        Cursor cursor = db.rawQuery("select * from city", null);
        List<City> result = new ArrayList();
        while (cursor.moveToNext()) {
            result.add(new City(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getString(cursor.getColumnIndex(PINYIN))));
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    public List<City> searchCity(String keyword) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(this.DB_PATH + "china_cities.db", null);
        Cursor cursor = db.rawQuery("select * from city where name like \"%" + keyword + "%\" or pinyin like \"%" + keyword + "%\"", null);
        List<City> result = new ArrayList();
        while (cursor.moveToNext()) {
            result.add(new City(cursor.getString(cursor.getColumnIndex(NAME)), cursor.getString(cursor.getColumnIndex(PINYIN))));
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }
}
