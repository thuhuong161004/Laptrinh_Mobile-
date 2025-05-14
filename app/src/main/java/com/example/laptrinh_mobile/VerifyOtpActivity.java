package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class VerifyOtpActivity extends AppCompatActivity {

    private static final String TAG = "VerifyOtpActivity";
    private EditText etOtp;
    private Button btnVerifyOtp;
    private TextView tvResendOtp;
    private String expectedOtp;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        etOtp = findViewById(R.id.et_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        tvResendOtp = findViewById(R.id.tv_resend_otp);

        // Lấy email và OTP từ Intent
        email = getIntent().getStringExtra("email");
        expectedOtp = getIntent().getStringExtra("otp");

        if (email == null || expectedOtp == null) {
            Log.e(TAG, "Email hoặc OTP từ Intent null");
            Toast.makeText(this, "Lỗi: Dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnVerifyOtp.setOnClickListener(v -> {
            String enteredOtp = etOtp.getText().toString().trim();

            if (enteredOtp.isEmpty()) {
                Log.w(TAG, "OTP trống");
                Toast.makeText(this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredOtp.equals(expectedOtp)) {
                Log.d(TAG, "Xác minh OTP thành công cho email: " + email);
                Toast.makeText(this, "Xác minh thành công cho email " + email + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VerifyOtpActivity.this, RegisterActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            } else {
                Log.w(TAG, "OTP không khớp: Nhập " + enteredOtp + ", Kỳ vọng " + expectedOtp);
                Toast.makeText(this, "Mã OTP không đúng. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        tvResendOtp.setOnClickListener(v -> {
            Log.d(TAG, "Yêu cầu gửi lại OTP cho email: " + email);
            Intent intent = new Intent(VerifyOtpActivity.this, EmailInputActivity.class);
            startActivity(intent);
            finish();
        });
    }
}