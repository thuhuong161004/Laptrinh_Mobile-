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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private List<Expense> expenseList = new ArrayList<>();
    private TextView tvTotalExpense;
    private RecyclerView rvExpenses;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        try {
            dbHelper = DatabaseManager.getInstance().getDatabaseHelper();
        } catch (IllegalStateException e) {
            if (getContext() != null) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể truy cập cơ sở dữ liệu: " + e.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> requireActivity().finish())
                        .show();
            }
            return view;
        }

        sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

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
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.TABLE_EXPENSES, null, null, null, null, null, null);
                if (cursor.getCount() == 0) {
                    dbHelper.addExpense(500000.0, "Ăn uống", "2025-05-08", "Ăn ngoài");
                    dbHelper.addExpense(200000.0, "Giao thông", "2025-05-07", "Đi xe buýt");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hasAddedExpenseSampleData", true);
                    editor.apply();
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
                if (getContext() != null) {
                    new android.app.AlertDialog.Builder(getContext())
                            .setTitle("Lỗi")
                            .setMessage("Không thể thêm chi tiêu mẫu: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        }
    }

    private void loadExpenses() {
        expenseList.clear();
        double totalExpense = 0;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_EXPENSES, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_EXPENSE));
                double amount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT_EXPENSE));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_EXPENSE));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_EXPENSE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE_EXPENSE));
                expenseList.add(new Expense(id, amount, category, description, date));
                totalExpense += amount;
            }
            cursor.close();
            db.close();
            adapter.notifyDataSetChanged();
            if (expenseList.isEmpty()) {
                tvTotalExpense.setText("Chưa có chi tiêu");
            } else {
                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
                String formattedTotal = formatter.format(totalExpense) + " ₫";
                tvTotalExpense.setText(String.format("Tổng chi tiêu: %s", formattedTotal));
            }
        } catch (Exception e) {
            if (getContext() != null) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể tải chi tiêu: " + e.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }
}