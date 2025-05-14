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

public class IncomeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private IncomeAdapter adapter;
    private List<IncomeAdapter.Income> incomeList = new ArrayList<>(); // Sử dụng IncomeAdapter.Income (hoặc Income nếu có)
    private TextView tvTotalIncome;
    private RecyclerView rvIncomes;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        // Sử dụng DatabaseManager để lấy DatabaseHelper
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

        rvIncomes = view.findViewById(R.id.rv_incomes);
        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        rvIncomes.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new IncomeAdapter(getContext(), incomeList);
        rvIncomes.setAdapter(adapter);

        addSampleIncomesIfNeeded();

        FloatingActionButton fabAddIncome = view.findViewById(R.id.fab_add_income);
        fabAddIncome.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddIncomeActivity.class);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadIncomes();
    }

    private void addSampleIncomesIfNeeded() {
        boolean hasAddedSampleData = sharedPreferences.getBoolean("hasAddedIncomeSampleData", false);
        if (!hasAddedSampleData) {
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query(DatabaseHelper.TABLE_INCOMES, null, null, null, null, null, null);
                if (cursor.getCount() == 0) {
                    dbHelper.addIncome(1000000.0, "Tiền lương", "2025-05-08", "Lương tháng");
                    dbHelper.addIncome(300000.0, "Thưởng", "2025-05-07", "Thưởng thêm");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hasAddedIncomeSampleData", true);
                    editor.apply();
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
                if (getContext() != null) {
                    new android.app.AlertDialog.Builder(getContext())
                            .setTitle("Lỗi")
                            .setMessage("Không thể thêm thu nhập mẫu: " + e.getMessage())
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        }
    }

    private void loadIncomes() {
        incomeList.clear();
        double totalIncome = 0;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.TABLE_INCOMES, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID_INCOME));
                double amount = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_AMOUNT_INCOME));
                String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY_INCOME));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_INCOME));
                String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE_INCOME));
                incomeList.add(new IncomeAdapter.Income(id, amount, category, date, note));
                totalIncome += amount;
            }
            cursor.close();
            db.close();
            adapter.notifyDataSetChanged();
            if (incomeList.isEmpty()) {
                tvTotalIncome.setText("Chưa có thu nhập");
            } else {
                NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
                String formattedTotal = formatter.format(totalIncome) + " ₫";
                tvTotalIncome.setText(String.format("Tổng thu nhập: %s", formattedTotal));
            }
        } catch (Exception e) {
            if (getContext() != null) {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể tải thu nhập: " + e.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }
}