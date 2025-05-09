package com.example.laptrinh_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private ExpenseAdapter adapter;
    private List<ExpenseAdapter.Expense> expenseList = new ArrayList<>();
    private TextView tvTotalExpense;
    private RecyclerView rvExpenses;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());
        sharedPreferences = getContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

        rvExpenses = view.findViewById(R.id.rv_expenses);
        tvTotalExpense = view.findViewById(R.id.tv_total_expense);
        rvExpenses.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ExpenseAdapter(getContext(), expenseList);
        rvExpenses.setAdapter(adapter);

        addSampleExpensesIfNeeded();

        FloatingActionButton fabAddExpense = view.findViewById(R.id.fab_add_expense);
        fabAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddExpenseActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExpenses();
    }

    private void addSampleExpensesIfNeeded() {
        boolean hasAddedSampleData = sharedPreferences.getBoolean("hasAddedExpenseSampleData", false);
        if (!hasAddedSampleData) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_EXPENSES, null, null, null, null, null, null);
            if (cursor.getCount() == 0) {
                dbHelper.addExpense(500000.0, "Ăn uống", "2025-05-08", "Ăn ngoài");
                dbHelper.addExpense(200000.0, "Giao thông", "2025-05-07", "Đi xe buýt");
            }
            cursor.close();
            db.close();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasAddedExpenseSampleData", true);
            editor.apply();
        }
    }

    private void loadExpenses() {
        expenseList.clear();
        double totalExpense = 0;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_EXPENSES, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_EXPENSE));
                double amount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT_EXPENSE));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_EXPENSE));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_EXPENSE));
                String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE_EXPENSE));
                expenseList.add(new ExpenseAdapter.Expense(id, amount, category, date, note));
                totalExpense += amount;
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        if (expenseList.isEmpty()) {
            tvTotalExpense.setText("Chưa có chi tiêu");
        } else {
            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
            String formattedTotal = formatter.format(totalExpense) + " ₫";
            tvTotalExpense.setText(String.format("Tổng chi tiêu: %s", formattedTotal));
        }
    }
}