package com.example.laptrinh_mobile.Thongbao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;
import com.google.android.material.button.MaterialButton;

public class Main_datthanhcong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_datthanhcong);

        // Nhận dữ liệu ngày/tháng/năm (nếu cần)
        Intent intent = getIntent();
        int selectedDay = intent.getIntExtra("selectedDay", -1);
        int selectedMonth = intent.getIntExtra("selectedMonth", 5);
        int selectedYear = intent.getIntExtra("selectedYear", 2025);

        // Xử lý nút "Thoát"
        MaterialButton buttonThoat = findViewById(R.id.button_thoat);
        if (buttonThoat != null) {
            buttonThoat.setOnClickListener(v -> {
                Intent intentThoat = new Intent(Main_datthanhcong.this, Main_thongbao.class);
                startActivity(intentThoat);
                finish(); // Đóng activity hiện tại
            });
        } else {
            Toast.makeText(this, "Không tìm thấy nút Thoát!", Toast.LENGTH_SHORT).show();
        }
    }
}