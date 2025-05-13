package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_luudanhgia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_luudanhgia);

        // Nhận dữ liệu từ Main_danhgia
        Intent intent = getIntent();
        float rating = intent.getFloatExtra("rating", 0);
        String comment = intent.getStringExtra("comment");

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Trả lại số sao đã chọn
            Intent resultIntent = new Intent();
            resultIntent.putExtra("saved_rating", rating);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}