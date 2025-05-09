package com.example.laptrinh_mobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseManager.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng expenses
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_ID_EXPENSE = "id";
    public static final String COLUMN_AMOUNT_EXPENSE = "amount";
    public static final String COLUMN_CATEGORY_EXPENSE = "category";
    public static final String COLUMN_DATE_EXPENSE = "date";
    public static final String COLUMN_NOTE_EXPENSE = "note";

    // Bảng incomes
    public static final String TABLE_INCOMES = "incomes";
    public static final String COLUMN_ID_INCOME = "id";
    public static final String COLUMN_AMOUNT_INCOME = "amount";
    public static final String COLUMN_CATEGORY_INCOME = "category";
    public static final String COLUMN_DATE_INCOME = "date";
    public static final String COLUMN_NOTE_INCOME = "note";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExpensesTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_ID_EXPENSE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_AMOUNT_EXPENSE + " REAL," +
                COLUMN_CATEGORY_EXPENSE + " TEXT," +
                COLUMN_DATE_EXPENSE + " TEXT," +
                COLUMN_NOTE_EXPENSE + " TEXT)";
        db.execSQL(createExpensesTable);

        String createIncomesTable = "CREATE TABLE " + TABLE_INCOMES + " (" +
                COLUMN_ID_INCOME + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_AMOUNT_INCOME + " REAL," +
                COLUMN_CATEGORY_INCOME + " TEXT," +
                COLUMN_DATE_INCOME + " TEXT," +
                COLUMN_NOTE_INCOME + " TEXT)";
        db.execSQL(createIncomesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
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

    public int updateExpense(long id, double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_EXPENSE, amount);
        values.put(COLUMN_CATEGORY_EXPENSE, category);
        values.put(COLUMN_DATE_EXPENSE, date);
        values.put(COLUMN_NOTE_EXPENSE, note);
        int rowsAffected = db.update(TABLE_EXPENSES, values, COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int updateIncome(long id, double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT_INCOME, amount);
        values.put(COLUMN_CATEGORY_INCOME, category);
        values.put(COLUMN_DATE_INCOME, date);
        values.put(COLUMN_NOTE_INCOME, note);
        int rowsAffected = db.update(TABLE_INCOMES, values, COLUMN_ID_INCOME + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteExpense(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EXPENSES, COLUMN_ID_EXPENSE + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }

    public int deleteIncome(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_INCOMES, COLUMN_ID_INCOME + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }
}