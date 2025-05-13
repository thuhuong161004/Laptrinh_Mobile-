package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_trogiup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_trogiup);

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Chuyển về Main_hoso
            Intent intent = new Intent(Main_trogiup.this, Main_hoso.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Tìm LinearLayout của mục "Cách cài đặt tài khoản"
        LinearLayout accountSetup = findViewById(R.id.cachcaidat);
        accountSetup.setOnClickListener(v -> {
            // Chuyển sang Main_trogiup2
            Intent intent = new Intent(Main_trogiup.this, Main_trogiup2.class);
            startActivity(intent);
        });

        LinearLayout cachdangnhap = findViewById(R.id.cachdangnhap);
        cachdangnhap.setOnClickListener(v -> {
            // Chuyển sang Main_trogiup3
            Intent intent = new Intent(Main_trogiup.this, Main_trogiup3.class);
            startActivity(intent);
        });

        LinearLayout dulieu = findViewById(R.id.dulieu);
        dulieu.setOnClickListener(v -> {
            // Chuyển sang Main_trogiup4
            Intent intent = new Intent(Main_trogiup.this, Main_trogiup4.class);
            startActivity(intent);
        });

        LinearLayout tinhnang = findViewById(R.id.tinhnang);
        tinhnang.setOnClickListener(v -> {
            // Chuyển sang Main_trogiup5
            Intent intent = new Intent(Main_trogiup.this, Main_trogiup5.class);
            startActivity(intent);
        });
    }
}