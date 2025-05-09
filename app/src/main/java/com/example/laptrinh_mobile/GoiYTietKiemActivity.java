package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GoiYTietKiemActivity extends AppCompatActivity {

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goiymuctieutietkiem);

        // Ánh xạ nút back
        btnBack = findViewById(R.id.btnBack);

        // Gắn sự kiện click
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về activity đặt mục tiêu tiết kiệm
                Intent intent = new Intent(GoiYTietKiemActivity.this, SetSavingsGoalActivity.class);
                startActivity(intent);
                finish(); // Đóng activity hiện tại
            }
        });
    }}
