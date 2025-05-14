package com.example.laptrinh_mobile;

import android.content.Context;

public class DatabaseManager {
    private static DatabaseManager instance;
    private DatabaseHelper dbHelper;
    private Context context;
    private String uid;

    private DatabaseManager() {}

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void initialize(Context context, String uid) {
        if (context == null || uid == null || uid.isEmpty()) {
            throw new IllegalStateException("Context hoặc UID không hợp lệ");
        }
        this.context = context.getApplicationContext();
        this.uid = uid;
        this.dbHelper = new DatabaseHelper(context, uid);
    }

    public DatabaseHelper getDatabaseHelper() {
        if (dbHelper == null) {
            throw new IllegalStateException("DatabaseHelper chưa được khởi tạo. Hãy gọi initialize trước.");
        }
        return dbHelper;
    }

    public void clear() {
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
        context = null;
        uid = null;
    }
}