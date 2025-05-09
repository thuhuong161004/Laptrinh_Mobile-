package com.example.laptrinh_mobile;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WalletActivity extends AppCompatActivity {

    private TextView tvTotalExpense;
    private ExpenseDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        dbHelper = new ExpenseDatabaseHelper(this);
        tvTotalExpense = findViewById(R.id.tv_total_expense);

        double total = dbHelper.getTotalExpense();
        tvTotalExpense.setText(String.format("%,.0f VND", total));
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