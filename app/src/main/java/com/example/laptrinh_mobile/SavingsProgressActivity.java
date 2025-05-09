package com.example.laptrinh_mobile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SavingsProgressActivity extends AppCompatActivity {
    private TextView tvRecommendation;
    private TextView tvGoalAmount;
    private TextView tvPurpose;
    private TextView tvSavedAmount;
    private TextView tvTimeProgress;
    private ProgressBar progressMoney;
    private ProgressBar progressTime;
    private ImageButton btnBack;

    private static final String PREFS_NAME = "SavingsAppPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tientrinhtietkiem);

        // Initialize UI components
        tvGoalAmount = findViewById(R.id.tvGoalAmount);
        tvPurpose = findViewById(R.id.tvPurpose);
        tvSavedAmount = findViewById(R.id.tvSavedAmount);
        tvTimeProgress = findViewById(R.id.tvTimeProgress);
        progressMoney = findViewById(R.id.progressMoney);
        progressTime = findViewById(R.id.progressTime);
        btnBack = findViewById(R.id.btnBack);
        tvRecommendation = findViewById(R.id.tvRecommendation);
        // Load saved goal data
        loadSavedData();

        // Back button click listener
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to previous screen
            }
        });
    }

    private void loadSavedData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Get saved data
        String goalAmountStr = prefs.getString("goal_amount", "0 VND");
        String purpose = prefs.getString("purpose", "");
        String savedAmountStr = prefs.getString("saved_amount", "0 VND");
        int moneyProgress = prefs.getInt("money_progress", 0);
        int timeProgress = prefs.getInt("time_progress", 0);

        // Display data in UI
        tvGoalAmount.setText(goalAmountStr);
        tvPurpose.setText(purpose);
        tvSavedAmount.setText(savedAmountStr);
        tvTimeProgress.setText("Đạt " + timeProgress + "%");

        progressMoney.setProgress(moneyProgress);
        progressTime.setProgress(timeProgress);

        // Tính toán và hiển thị nhận xét
        try {
            long monthlyIncome = 10_000_000; // Giả định thu nhập hàng tháng
            long goalAmount = Long.parseLong(goalAmountStr.replaceAll("[^0-9]", ""));
            long savedAmount = Long.parseLong(savedAmountStr.replaceAll("[^0-9]", ""));
            long monthlySavings = 2_000_000; // Giả định mức tiết kiệm hàng tháng

            String recommendation = SavingsTracker.generateRecommendation(
                    monthlyIncome, goalAmount, savedAmount, monthlySavings, timeProgress
            );

            tvRecommendation.setText(recommendation);
        } catch (NumberFormatException e) {
            tvRecommendation.setText("Không thể tính toán nhận xét do dữ liệu không hợp lệ.");
        }
    }}


