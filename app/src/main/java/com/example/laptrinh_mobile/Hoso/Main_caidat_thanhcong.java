package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_caidat_thanhcong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_caidatthanhcong);

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Chuyển về Main_hoso
            Intent intent = new Intent(Main_caidat_thanhcong.this, Main_hoso.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}