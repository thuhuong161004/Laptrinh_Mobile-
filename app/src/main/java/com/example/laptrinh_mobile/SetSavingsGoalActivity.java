package com.example.laptrinh_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laptrinh_mobile.Hoso.Main_hoso;
import com.example.laptrinh_mobile.Thongbao.Main_thongbao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SetSavingsGoalActivity extends AppCompatActivity {

    private EditText etCurrentBalance;
    private EditText etSavingsGoal, etCompletionDate, etSavingsPurpose;
    private Button btnSetGoal;

    private static final String PREFS_NAME = "SavingsAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datmuctieutietkiem);

        // Khởi tạo view
        etCurrentBalance = findViewById(R.id.etCurrentBalance);
        etSavingsGoal = findViewById(R.id.etSavingsGoal);
        etCompletionDate = findViewById(R.id.etCompletionDate);
        etSavingsPurpose = findViewById(R.id.etSavingsPurpose);
        btnSetGoal = findViewById(R.id.btnSetGoal);


        ImageView ivHelp = findViewById(R.id.ivHelp); // đặt vào trong onCreate
        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetSavingsGoalActivity.this, GoiYTietKiemActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout navThongbao = findViewById(R.id.navNotifications);
        navThongbao.setOnClickListener(v -> {
            Intent intent = new Intent(SetSavingsGoalActivity.this, Main_thongbao.class);
            startActivity(intent);
            finish();
        });

        LinearLayout navtietkiem = findViewById(R.id.navSavings);
        navtietkiem.setOnClickListener(v -> {
            Intent intent = new Intent(SetSavingsGoalActivity.this, AddExpenseActivity.class);
            startActivity(intent);
            finish();
        });
        loadCurrentBalance();

        etCompletionDate.setOnClickListener(v -> showDatePickerDialog());

        btnSetGoal.setOnClickListener(v -> {
            if (validateInputs()) {
                saveGoalData();
                startSavingsProgressActivity();
            }
        });
    }

    private void loadCurrentBalance() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String balance = prefs.getString("current_balance", "50,000,000 VND");
        etCurrentBalance.setText(balance);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    etCompletionDate.setText(sdf.format(selectedDate.getTime()));
                },
                year, month, day
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private boolean validateInputs() {
        if (etSavingsGoal.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền tiết kiệm", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etCompletionDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày hoàn thành", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etSavingsPurpose.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mục tiêu tiết kiệm", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveGoalData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save goal amount
        String goalAmountStr = etSavingsGoal.getText().toString().trim();
        editor.putString("goal_amount", goalAmountStr);

        // Save completion date
        String completionDate = etCompletionDate.getText().toString().trim();
        editor.putString("completion_date", completionDate);

        // Save purpose
        String purpose = etSavingsPurpose.getText().toString().trim();
        editor.putString("purpose", purpose);
        // Save current balance
        String currentBalanceStr = etCurrentBalance.getText().toString().trim();
        editor.putString("current_balance", formatCurrency(Long.parseLong(currentBalanceStr)));


        // Parse goal amount
        try {
            long goalAmount = Long.parseLong(goalAmountStr.replaceAll("[^0-9]", ""));

            // Giả định số tiền hiện đang có được lấy từ balance
            SharedPreferences prefs2 = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            String balanceStr = prefs2.getString("current_balance", "0");
            long currentBalance = Long.parseLong(balanceStr.replaceAll("[^0-9]", ""));

            long savedAmount = Math.min(currentBalance, goalAmount); // Giới hạn tối đa bằng goal
            int moneyProgress = (int) ((savedAmount * 100) / goalAmount);

            editor.putString("saved_amount", formatCurrency(savedAmount));
            editor.putInt("money_progress", moneyProgress);
        } catch (NumberFormatException e) {
            editor.putString("saved_amount", "0 VND");
            editor.putInt("money_progress", 0);
        }

        // Tính tiến độ thời gian
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = sdf.format(Calendar.getInstance().getTime());
        int timeProgress = SavingsTracker.calculateTimeProgress(startDate, completionDate);
        editor.putInt("time_progress", timeProgress);

        editor.apply(); // Đừng quên dòng này để lưu thay đổi
    }

    private String formatCurrency(long amount) {
        return String.format("%,d VND", amount);
    }

    private void startSavingsProgressActivity() {
        Intent intent = new Intent(this, SavingsProgressActivity.class);
        startActivity(intent);
    }



}
