package com.example.laptrinh_mobile;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class QuenMatKhauActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private ImageButton togglePasswordButton;
    private EditText etPhoneNumber;
    private EditText etOtp;
    private EditText etNewPassword;
    private Button btnSendOtp;
    private Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quenmatkhau);

        // Khởi tạo các view bằng findViewById
        btnBack = findViewById(R.id.btnBack);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnComplete = findViewById(R.id.btnComplete);

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
                if (etNewPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                } else {
                    etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordButton.setImageResource(android.R.drawable.ic_menu_view);
                }
                etNewPassword.setSelection(etNewPassword.getText().length());
            }
        });

        // Xử lý nút "Gửi OTP"
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhoneNumber.getText().toString();
                if (phone.isEmpty()) {
                    Toast.makeText(QuenMatKhauActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuenMatKhauActivity.this, "Đã gửi OTP", Toast.LENGTH_SHORT).show();
                    // TODO: Thêm logic gửi OTP thực tế
                }
            }
        });

        // Xử lý nút "Hoàn thành"
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = etPhoneNumber.getText().toString();
                String otp = etOtp.getText().toString();
                String newPassword = etNewPassword.getText().toString();

                if (phone.isEmpty() || otp.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(QuenMatKhauActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuenMatKhauActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                    // TODO: Thêm logic đặt lại mật khẩu thực tế
                }
            }
        });
    }
}