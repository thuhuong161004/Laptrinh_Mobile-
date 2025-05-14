package com.example.laptrinh_mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finance_%s.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_EXPENSES = "expenses";
    public static final String TABLE_INCOMES = "incomes";
    public static final String COLUMN_ID_EXPENSE = "id";
    public static final String COLUMN_AMOUNT_EXPENSE = "amount";
    public static final String COLUMN_CATEGORY_EXPENSE = "category";
    public static final String COLUMN_DATE_EXPENSE = "date";
    public static final String COLUMN_NOTE_EXPENSE = "note";
    public static final String COLUMN_ID_INCOME = "id";
    public static final String COLUMN_AMOUNT_INCOME = "amount";
    public static final String COLUMN_CATEGORY_INCOME = "category";
    public static final String COLUMN_DATE_INCOME = "date";
    public static final String COLUMN_NOTE_INCOME = "note";

    private String uid;

    public DatabaseHelper(Context context, String uid) {
        super(context, String.format(DATABASE_NAME, uid), null, DATABASE_VERSION);
        this.uid = uid;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpenseTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT_EXPENSE + " REAL, " +
                COLUMN_CATEGORY_EXPENSE + " TEXT, " +
                COLUMN_DATE_EXPENSE + " TEXT, " +
                COLUMN_NOTE_EXPENSE + " TEXT)";
        db.execSQL(createExpenseTable);

        String createIncomeTable = "CREATE TABLE " + TABLE_INCOMES + " (" +
                COLUMN_ID_INCOME + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT_INCOME + " REAL, " +
                COLUMN_CATEGORY_INCOME + " TEXT, " +
                COLUMN_DATE_INCOME + " TEXT, " +
                COLUMN_NOTE_INCOME + " TEXT)";
        db.execSQL(createIncomeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Không có nâng cấp trong phiên bản gốc
    }

    public long addExpense(double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_EXPENSE, amount);
        values.put(COLUMN_CATEGORY_EXPENSE, category);
        values.put(COLUMN_DATE_EXPENSE, date);
        values.put(COLUMN_NOTE_EXPENSE, note);
        long id = db.insert(TABLE_EXPENSES, null, values);
        db.close();
        return id;
    }

    public long addIncome(double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_INCOME, amount);
        values.put(COLUMN_CATEGORY_INCOME, category);
        values.put(COLUMN_DATE_INCOME, date);
        values.put(COLUMN_NOTE_INCOME, note);
        long id = db.insert(TABLE_INCOMES, null, values);
        db.close();
        return id;
    }

    public int updateExpense(int id, double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_EXPENSE, amount);
        values.put(COLUMN_CATEGORY_EXPENSE, category);
        values.put(COLUMN_DATE_EXPENSE, date);
        values.put(COLUMN_NOTE_EXPENSE, note);
        int rows = db.update(TABLE_EXPENSES, values, COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int updateIncome(long id, double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_INCOME, amount);
        values.put(COLUMN_CATEGORY_INCOME, category);
        values.put(COLUMN_DATE_INCOME, date);
        values.put(COLUMN_NOTE_INCOME, note);
        int rows = db.update(TABLE_INCOMES, values, COLUMN_ID_INCOME + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_EXPENSES, COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public int deleteIncome(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_INCOMES, COLUMN_ID_INCOME + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    public double getTotalExpense() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT_EXPENSE + ") FROM " + TABLE_EXPENSES, null);
        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return total;
    }

    public Map<String, Double> getExpenseSummary(String startDate, String endDate) {
        Map<String, Double> summary = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY_EXPENSE + ", SUM(" + COLUMN_AMOUNT_EXPENSE + ") as total FROM " + TABLE_EXPENSES;
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            query += " WHERE " + COLUMN_DATE_EXPENSE + " BETWEEN ? AND ?";
            Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_EXPENSE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                summary.put(category, total);
            }
            cursor.close();
        } else {
            Cursor cursor = db.rawQuery(query + " GROUP BY " + COLUMN_CATEGORY_EXPENSE, null);
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_EXPENSE));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                summary.put(category, total);
            }
            cursor.close();
        }
        db.close();
        return summary;
    }

    public Map<String, Double> getIncomeSummary(String startDate, String endDate) {
        Map<String, Double> summary = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CATEGORY_INCOME + ", SUM(" + COLUMN_AMOUNT_INCOME + ") as total FROM " + TABLE_INCOMES;
        if (!startDate.isEmpty() && !endDate.isEmpty()) {
            query += " WHERE " + COLUMN_DATE_INCOME + " BETWEEN ? AND ?";
            Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_INCOME));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                summary.put(category, total);
            }
            cursor.close();
        } else {
            Cursor cursor = db.rawQuery(query + " GROUP BY " + COLUMN_CATEGORY_INCOME, null);
            while (cursor.moveToNext()) {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_INCOME));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                summary.put(category, total);
            }
            cursor.close();
        }
        db.close();
        return summary;
    }

    public String getExpenseDateRange() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(" + COLUMN_DATE_EXPENSE + "), MAX(" + COLUMN_DATE_EXPENSE + ") FROM " + TABLE_EXPENSES, null);
        String range = "Không có dữ liệu";
        if (cursor.moveToFirst() && cursor.getString(0) != null && cursor.getString(1) != null) {
            range = cursor.getString(0) + " - " + cursor.getString(1);
        }
        cursor.close();
        db.close();
        return range;
    }

    public String getIncomeDateRange() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MIN(" + COLUMN_DATE_INCOME + "), MAX(" + COLUMN_DATE_INCOME + ") FROM " + TABLE_INCOMES, null);
        String range = "Không có dữ liệu";
        if (cursor.moveToFirst() && cursor.getString(0) != null && cursor.getString(1) != null) {
            range = cursor.getString(0) + " - " + cursor.getString(1);
        }
        cursor.close();
        db.close();
        return range;
    }
}