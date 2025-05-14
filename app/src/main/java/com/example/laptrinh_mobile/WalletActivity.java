package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {

    private TextView tvTotalExpense;
    private ImageView btnAdd;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        try {
            dbHelper = DatabaseManager.getInstance().getDatabaseHelper();
        } catch (IllegalStateException e) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Lỗi")
                    .setMessage("Không thể truy cập cơ sở dữ liệu: " + e.getMessage())
                    .setPositiveButton("OK", (dialog, which) -> finish())
                    .show();
            return;
        }

        tvTotalExpense = findViewById(R.id.tv_total_expense);
        btnAdd = findViewById(R.id.btnAdd);

        double total = dbHelper.getTotalExpense();
        tvTotalExpense.setText(String.format("%,.0f VND", total));

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(WalletActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        double total = dbHelper.getTotalExpense();
        tvTotalExpense.setText(String.format("%,.0f VND", total));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}