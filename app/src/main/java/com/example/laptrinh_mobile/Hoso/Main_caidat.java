package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_caidat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_caidat);

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Tìm các EditText và Button
        EditText currentPassword = findViewById(R.id.current_password);
        EditText newPassword = findViewById(R.id.new_password);
        EditText confirmPassword = findViewById(R.id.confirm_password);
        Button saveButton = findViewById(R.id.save_button);

        // Xử lý sự kiện nhấn nút "Lưu"
        saveButton.setOnClickListener(v -> {
            String currentPass = currentPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            // Kiểm tra các trường nhập liệu
            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(Main_caidat.this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
            } else if (newPass.length() < 6) {
                Toast.makeText(Main_caidat.this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(confirmPass)) {
                Toast.makeText(Main_caidat.this, "Mật khẩu mới và xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            } else {
                // Chuyển sang màn hình thông báo thành công
                Intent intent = new Intent(Main_caidat.this, Main_caidat_thanhcong.class);
                startActivity(intent);
                // Kết thúc Main_caidat để không quay lại bằng nút back
                finish();
            }
        });
    }
}