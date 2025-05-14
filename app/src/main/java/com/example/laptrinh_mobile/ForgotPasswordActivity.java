package com.example.laptrinh_mobile;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private EditText etEmail;
    private Button btnSendResetEmail;
    private TextView tvBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.et_email);
        btnSendResetEmail = findViewById(R.id.btn_send_reset_email);
        tvBack = findViewById(R.id.tv_back);
        mAuth = FirebaseAuth.getInstance();

        btnSendResetEmail.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Log.w(TAG, "Email trống");
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.w(TAG, "Email không hợp lệ: " + email);
                Toast.makeText(this, "Email không hợp lệ. Vui lòng nhập email đúng định dạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Gửi yêu cầu reset mật khẩu cho: " + email);
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Gửi email reset thành công");
                            Toast.makeText(this, "Liên kết khôi phục mật khẩu đã được gửi đến email của bạn. Vui lòng kiểm tra (bao gồm Spam/Junk).", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                            Log.e(TAG, "Gửi email reset thất bại: " + errorMessage, task.getException());
                            Toast.makeText(this, "Gửi email thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
        });

        tvBack.setOnClickListener(v -> {
            Log.d(TAG, "Người dùng nhấn Quay lại");
            finish();
        });
    }
}