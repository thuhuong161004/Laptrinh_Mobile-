package com.example.laptrinh_mobile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etLoginInput, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword, tvForgotUsername;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "Loading activity_login layout");
        etLoginInput = findViewById(R.id.et_login_input);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvForgotUsername = findViewById(R.id.tv_forgot_username);

        Log.d(TAG, "etLoginInput is " + (etLoginInput == null ? "null" : "not null"));
        Log.d(TAG, "etPassword is " + (etPassword == null ? "null" : "not null"));
        Log.d(TAG, "btnLogin is " + (btnLogin == null ? "null" : "not null"));
        Log.d(TAG, "tvRegister is " + (tvRegister == null ? "null" : "not null"));
        Log.d(TAG, "tvForgotPassword is " + (tvForgotPassword == null ? "null" : "not null"));
        Log.d(TAG, "tvForgotUsername is " + (tvForgotUsername == null ? "null" : "not null"));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance("https://laptrinh-mobile.firebaseio.com/").getReference("users");
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        if (etLoginInput == null || etPassword == null || btnLogin == null || tvRegister == null || tvForgotPassword == null || tvForgotUsername == null) {
            Log.e(TAG, "One or more UI components are null");
            Toast.makeText(this, "Lỗi giao diện: Không tìm thấy các thành phần cần thiết", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        Log.d(TAG, "Kiểm tra trạng thái đăng nhập - currentUser: " + (currentUser != null) + ", isLoggedIn: " + isLoggedIn);

        if (currentUser != null && isLoggedIn) {
            Log.d(TAG, "Người dùng đã đăng nhập, UID: " + currentUser.getUid());
            try {
                DatabaseManager.getInstance().initialize(this, currentUser.getUid());
                Log.d(TAG, "Đăng nhập tự động thành công với UID: " + currentUser.getUid());
                try {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "MainActivity not found: " + e.getMessage(), e);
                    Toast.makeText(this, "Lỗi: Không tìm thấy MainActivity", Toast.LENGTH_LONG).show();
                }
            } catch (IllegalStateException e) {
                Log.e(TAG, "Lỗi khởi tạo DatabaseManager: " + e.getMessage(), e);
                Toast.makeText(this, "Lỗi khởi tạo cơ sở dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Người dùng chưa đăng nhập, hiển thị màn hình đăng nhập");
            setupLoginScreen();
        }
    }

    private void setupLoginScreen() {
        btnLogin.setOnClickListener(v -> {
            String loginInput = etLoginInput.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (loginInput.isEmpty() || password.isEmpty()) {
                Log.w(TAG, "Thông tin đăng nhập trống");
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Bắt đầu đăng nhập với input: " + loginInput);
            if (Patterns.EMAIL_ADDRESS.matcher(loginInput).matches()) {
                loginWithEmail(loginInput, password);
            } else {
                findEmailByUsername(loginInput, password);
            }
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, EmailInputActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        tvForgotUsername.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RetrieveUsernameActivity.class);
            startActivity(intent);
        });
    }

    private void loginWithEmail(String email, String password) {
        Log.d(TAG, "Đăng nhập với email: " + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            try {
                                DatabaseManager.getInstance().initialize(this, user.getUid());
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();
                                Log.d(TAG, "Đăng nhập thành công với UID: " + user.getUid());
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                try {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } catch (ActivityNotFoundException e) {
                                    Log.e(TAG, "MainActivity not found: " + e.getMessage(), e);
                                    Toast.makeText(this, "Lỗi: Không tìm thấy MainActivity", Toast.LENGTH_LONG).show();
                                }
                            } catch (IllegalStateException e) {
                                Log.e(TAG, "Lỗi khởi tạo DatabaseManager: " + e.getMessage(), e);
                                Toast.makeText(this, "Lỗi khởi tạo cơ sở dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.e(TAG, "FirebaseUser null sau khi đăng nhập thành công");
                            Toast.makeText(this, "Lỗi: Không thể lấy thông tin người dùng", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi đăng nhập không xác định";
                        Log.e(TAG, "Đăng nhập thất bại: " + errorMessage);
                        Toast.makeText(this, "Đăng nhập thất bại: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Đăng nhập thất bại với lỗi: " + e.getMessage(), e);
                    Toast.makeText(this, "Đăng nhập thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void findEmailByUsername(String username, String password) {
        Log.d(TAG, "Tìm email với username: " + username);
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        if (email != null) {
                            Log.d(TAG, "Tìm thấy email: " + email + " cho username: " + username);
                            loginWithEmail(email, password);
                            return;
                        }
                    }
                    Log.w(TAG, "Không tìm thấy email cho username: " + username);
                    Toast.makeText(LoginActivity.this, "Không tìm thấy email cho tên đăng nhập này", Toast.LENGTH_SHORT).show();
                } else {
                    Log.w(TAG, "Tên đăng nhập không tồn tại: " + username);
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập không tồn tại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                String errorMessage = databaseError.getMessage() != null ? databaseError.getMessage() : "Lỗi truy vấn không xác định";
                Log.e(TAG, "Lỗi truy vấn database: " + errorMessage);
                Toast.makeText(LoginActivity.this, "Lỗi truy vấn: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}