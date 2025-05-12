package com.example.laptrinh_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.laptrinh_mobile.R;

public class LuuTaiKhoanActivity extends Activity {

    private EditText edtTen, edtEmail, edtSDT;
    private Button btnLuu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luutaikhoan);

        edtTen = findViewById(R.id.edtTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        btnLuu = findViewById(R.id.btnLuu);

        // Nhận dữ liệu cũ từ Intent
        Intent intent = getIntent();
        String ten = intent.getStringExtra("ten");
        String email = intent.getStringExtra("email");
        String sdt = intent.getStringExtra("sdt");

        edtTen.setText(ten);
        edtEmail.setText(email);
        edtSDT.setText(sdt);

        btnLuu.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("ten", edtTen.getText().toString());
            resultIntent.putExtra("email", edtEmail.getText().toString());
            resultIntent.putExtra("sdt", edtSDT.getText().toString());

            setResult(RESULT_OK, resultIntent);
            finish(); // Quay lại HoSoActivity
        });
    }
}
