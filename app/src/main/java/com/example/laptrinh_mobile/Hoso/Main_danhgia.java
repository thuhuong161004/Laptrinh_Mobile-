package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_danhgia extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE_RATING = 1001;
    private RatingBar ratingBar;
    private EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_danhgia);

        // Tìm các thành phần giao diện
        ImageButton backButton = findViewById(R.id.back_button);
        inputText = findViewById(R.id.inputText);
        ratingBar = findViewById(R.id.ratingBar);
        Button btnEvaluate = findViewById(R.id.btnEvaluate);

        // Xử lý nút quay lại
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Main_danhgia.this, Main_hoso.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // Xử lý nút "Lưu"
        btnEvaluate.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = inputText.getText().toString().trim();

            // Kiểm tra dữ liệu
            if (rating == 0) {
                Toast.makeText(Main_danhgia.this, "Vui lòng chọn số sao!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chuyển sang Main_danhgia_save
            Intent intent = new Intent(Main_danhgia.this, Main_luudanhgia.class);
            intent.putExtra("rating", rating);
            intent.putExtra("comment", comment);
            startActivityForResult(intent, REQUEST_CODE_SAVE_RATING);
        });

        // Nhận dữ liệu từ savedInstanceState (nếu có)
        if (savedInstanceState != null) {
            float savedRating = savedInstanceState.getFloat("saved_rating", 0);
            ratingBar.setRating(savedRating);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_RATING && resultCode == RESULT_OK && data != null) {
            float savedRating = data.getFloatExtra("saved_rating", 0);
            ratingBar.setRating(savedRating);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putFloat("saved_rating", ratingBar.getRating());
    }
}