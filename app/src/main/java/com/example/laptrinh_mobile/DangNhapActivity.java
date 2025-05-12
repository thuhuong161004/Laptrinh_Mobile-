package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DangNhapActivity extends AppCompatActivity {

    private ImageButton togglePasswordButton;
    private EditText passwordEditText;
    private TextView forgotPasswordText;
    private TextView createAccountText;
    private Button loginButton;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        // Khởi tạo các view bằng findViewById
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        createAccountText = findViewById(R.id.createAccountText);
        loginButton = findViewById(R.id.loginButton);
        phoneEditText = findViewById(R.id.phoneEditText);

        // Xử lý nút hiển thị/ẩn mật khẩu
        togglePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                } else {
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                }
                passwordEditText.setSelection(passwordEditText.getText().length());
            }
        });

        // Xử lý nút "Quên mật khẩu?"
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this, QuenMatKhauActivity.class));
            }
        });

        // Xử lý nút "Tạo tài khoản"
        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhapActivity.this, DangKyActivity.class));
            }
        });

        // Xử lý nút "Đăng nhập"
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    // TODO: Thêm logic đăng nhập thực tế (kiểm tra với cơ sở dữ liệu hoặc API)
                }
            }
        });
    }
}