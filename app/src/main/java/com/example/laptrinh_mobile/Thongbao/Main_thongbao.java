package com.example.laptrinh_mobile.Thongbao;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.Hoso.Main_hoso;
import com.example.laptrinh_mobile.SetSavingsGoalActivity;
import com.example.laptrinh_mobile.R;

public class Main_thongbao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thongbao); // File XML thông báo

        // Tìm ImageButton của nút "Thêm"
        ImageButton addButton = findViewById(R.id.button_add);

        // Thiết lập sự kiện nhấn cho nút "Thêm"
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main_thongbao.this, Main_datlich.class);
            startActivity(intent);
        });

        // Tìm LinearLayout của biểu tượng "Hồ sơ" trong bottom navigation
        LinearLayout navProfile = findViewById(R.id.navProfile);

        // Thiết lập sự kiện nhấn cho biểu tượng "Hồ sơ"
        navProfile.setOnClickListener(v -> {
            // Chuyển sang Main_hoso
            Intent intent = new Intent(Main_thongbao.this, Main_hoso.class);
            startActivity(intent);
            // Kết thúc Main_thongbao để không quay lại bằng nút back
            finish();
        });

        LinearLayout navSavings = findViewById(R.id.navSavings);
        navSavings.setOnClickListener(v -> {
            Intent intent = new Intent(Main_thongbao.this, SetSavingsGoalActivity.class);
            startActivity(intent);
            finish();
        });
    }
}