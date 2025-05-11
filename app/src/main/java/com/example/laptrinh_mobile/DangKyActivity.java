package com.example.laptrinh_mobile;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DangKyActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton togglePasswordButton;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etPhoneNumber;
    private EditText etOtp;
    private Button btnSendOtp;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangky);

        // Khởi tạo các view bằng findViewById
        btnBack = findViewById(R.id.btnBack);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);

        // Truy cập root layout (RelativeLayout)
        RelativeLayout rootLayout = (RelativeLayout) findViewById(android.R.id.content).getRootView();

        // ScrollView là child thứ 1 của rootLayout (child thứ 0 là btnBack)
        ScrollView scrollView = (ScrollView) rootLayout.getChildAt(1);

        // LinearLayout chính là child duy nhất của ScrollView
        LinearLayout mainLayout = (LinearLayout) scrollView.getChildAt(0);

        // Khởi tạo các view không có id (truy cập gián tiếp qua vị trí trong layout)
        LinearLayout usernameLayout = (LinearLayout) mainLayout.getChildAt(1); // Tên tài khoản
        etUsername = (EditText) usernameLayout.getChildAt(1);

        LinearLayout passwordLayout = (LinearLayout) mainLayout.getChildAt(2); // Mật khẩu
        etPassword = (EditText) passwordLayout.getChildAt(1);

        LinearLayout phoneLayout = (LinearLayout) mainLayout.getChildAt(3); // Số điện thoại
        etPhoneNumber = (EditText) phoneLayout.getChildAt(1);
        btnSendOtp = (Button) phoneLayout.getChildAt(2);

        LinearLayout otpLayout = (LinearLayout) mainLayout.getChildAt(4); // Mã OTP
        etOtp = (EditText) otpLayout.getChildAt(1);

        btnRegister = (Button) mainLayout.getChildAt(5); // Nút "Đăng ký"

        // Xử lý nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Xử lý nút hiển thị/ẩn mật khẩu
        togglePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        // Xử lý nút "Gửi OTP"
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhoneNumber.getText().toString();
                if (phone.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đã gửi OTP", Toast.LENGTH_SHORT).show();
                    // TODO: Thêm logic gửi OTP thực tế
                }
            }
        });

        // Xử lý nút "Đăng ký"
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String otp = etOtp.getText().toString();

                if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || otp.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    // TODO: Thêm logic đăng ký thực tế (lưu vào cơ sở dữ liệu hoặc gọi API)
                }
            }
        });
    }
}