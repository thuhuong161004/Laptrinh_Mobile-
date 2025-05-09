package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu đã có mục tiêu -> chuyển sang màn hình tiến độ
        if (hasGoal()) {
            startActivity(new Intent(this, SavingsProgressActivity.class));
        } else {
            // Nếu chưa có -> chuyển sang màn hình đặt mục tiêu
            startActivity(new Intent(this, SetSavingsGoalActivity.class));
        }

        // Kết thúc MainActivity để không quay lại màn này khi nhấn Back
        finish();
    }

    private boolean hasGoal() {
        return getSharedPreferences("savings_prefs", MODE_PRIVATE)
                .contains("goal_amount");
    }
}
