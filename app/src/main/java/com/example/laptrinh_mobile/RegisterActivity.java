package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://laptrinh-mobile.firebaseio.com/").getReference("users");

        email = getIntent().getStringExtra("email");
        if (email == null || email.isEmpty()) {
            Log.e(TAG, "Email từ Intent null hoặc trống");
            Toast.makeText(this, "Lỗi: Email không hợp lệ", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Log.w(TAG, "Thông tin đăng ký trống");
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Log.w(TAG, "Mật khẩu không khớp");
                Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Log.w(TAG, "Mật khẩu dưới 6 ký tự");
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Bắt đầu đăng ký với email: " + email);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                try {
                                    DatabaseManager.getInstance().initialize(this, user.getUid());
                                    Log.d(TAG, "Khởi tạo DatabaseManager thành công với UID: " + user.getUid());

                                    String uid = user.getUid();
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("username", username);
                                    userData.put("email", email);

                                    Log.d(TAG, "Lưu dữ liệu người dùng vào database với UID: " + uid);
                                    databaseReference.child(uid).setValue(userData)
                                            .addOnCompleteListener(dbTask -> {
                                                if (dbTask.isSuccessful()) {
                                                    Log.d(TAG, "Lưu dữ liệu người dùng thành công");
                                                    Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập để tiếp tục.", Toast.LENGTH_LONG).show();
                                                    try {
                                                        Log.d(TAG, "Chuyển hướng đến LoginActivity");
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        Log.d(TAG, "startActivity gọi thành công");
                                                        finish();
                                                        Log.d(TAG, "finish() gọi thành công");
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "Lỗi khi chuyển hướng đến LoginActivity: " + e.getMessage(), e);
                                                        Toast.makeText(this, "Lỗi chuyển hướng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    String errorMessage = dbTask.getException() != null ? dbTask.getException().getMessage() : "Lỗi lưu dữ liệu không xác định";
                                                    Log.e(TAG, "Lưu dữ liệu thất bại: " + errorMessage);
                                                    Toast.makeText(this, "Lưu dữ liệu thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                                                    mAuth.signOut();
                                                }
                                            });
                                    // Gửi email xác minh
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(emailTask -> {
                                                if (emailTask.isSuccessful()) {
                                                    Log.d(TAG, "Gửi email xác minh thành công cho: " + email);
                                                    Toast.makeText(this, "Email xác minh đã được gửi. Vui lòng kiểm tra hộp thư!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.e(TAG, "Gửi email xác minh thất bại: " + emailTask.getException().getMessage());
                                                }
                                            });
                                } catch (IllegalStateException e) {
                                    Log.e(TAG, "Lỗi khởi tạo DatabaseManager: " + e.getMessage(), e);
                                    Toast.makeText(this, "Lỗi khởi tạo cơ sở dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Log.e(TAG, "FirebaseUser null sau khi đăng ký thành công");
                                Toast.makeText(this, "Lỗi: Không thể lấy thông tin người dùng", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi đăng ký không xác định";
                            Log.e(TAG, "Đăng ký thất bại: " + errorMessage);
                            Toast.makeText(this, "Đăng ký thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Đăng ký thất bại với lỗi: " + e.getMessage(), e);
                        Toast.makeText(this, "Đăng ký thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}