package com.example.laptrinh_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ResetPasswordActivity";
    private EditText etOtp, etNewPassword, etConfirmPassword;
    private Button btnResetPassword;
    private FirebaseAuth mAuth;
    private String email, otp;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etOtp = findViewById(R.id.et_otp);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Lấy email và OTP từ Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        otp = intent.getStringExtra("otp");

        btnResetPassword.setOnClickListener(v -> {
            String inputOtp = etOtp.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (inputOtp.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!inputOtp.equals(otp)) {
                Toast.makeText(this, "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            // Đăng nhập lại để xác thực phiên
            String savedPassword = sharedPreferences.getString("password", "");
            if (savedPassword.isEmpty()) {
                Toast.makeText(this, "Không thể xác thực. Vui lòng thử lại hoặc liên hệ hỗ trợ.", Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, savedPassword)
                    .addOnCompleteListener(signInTask -> {
                        if (signInTask.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(updateTask -> {
                                            if (updateTask.isSuccessful()) {
                                                // Cập nhật mật khẩu trong SharedPreferences
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("password", newPassword);
                                                editor.putBoolean("isLoggedIn", false);
                                                editor.apply();

                                                Log.d(TAG, "Đổi mật khẩu thành công");
                                                Toast.makeText(this, "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
                                                Intent intentLogin = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                startActivity(intentLogin);
                                                finish();
                                            } else {
                                                Log.e(TAG, "Đổi mật khẩu thất bại: " + updateTask.getException().getMessage(), updateTask.getException());
                                                Toast.makeText(this, "Đổi mật khẩu thất bại: " + updateTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Log.e(TAG, "Người dùng không tồn tại");
                                Toast.makeText(this, "Người dùng không tồn tại", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e(TAG, "Xác thực thất bại: " + signInTask.getException().getMessage(), signInTask.getException());
                            Toast.makeText(this, "Xác thực thất bại: " + signInTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}