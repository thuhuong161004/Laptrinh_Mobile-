package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_editthongtin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_editthongtin);

        // Tìm các EditText
        EditText editFullname = findViewById(R.id.edit_fullname);
        EditText editEmail = findViewById(R.id.edit_email);
        EditText editPhone = findViewById(R.id.edit_phone);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        editFullname.setText(intent.getStringExtra("fullname"));
        editEmail.setText(intent.getStringExtra("email"));
        editPhone.setText(intent.getStringExtra("phone"));

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Tìm Button của nút "Lưu"
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> {
            // Tạo Intent để gửi dữ liệu về Main_thongtincanhan
            Intent resultIntent = new Intent();
            resultIntent.putExtra("fullname", editFullname.getText().toString());
            resultIntent.putExtra("email", editEmail.getText().toString());
            resultIntent.putExtra("phone", editPhone.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}