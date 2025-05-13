package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

public class Main_thongtin extends AppCompatActivity {

    private TextView fullnameTextView, emailTextView, phoneTextView;
    private ActivityResultLauncher<Intent> editInfoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_thongtin);

        // Tìm các TextView
        fullnameTextView = findViewById(R.id.personal_info_card)
                .findViewById(R.id.edit_fullname);
        emailTextView = findViewById(R.id.personal_info_card)
                .findViewById(R.id.edit_email);
        phoneTextView = findViewById(R.id.personal_info_card)
                .findViewById(R.id.edit_phone);

        // Khởi tạo ActivityResultLauncher để nhận kết quả từ Main_editthongtincanhan
        editInfoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        // Cập nhật TextView với dữ liệu mới
                        fullnameTextView.setText("Họ và tên: " + data.getStringExtra("fullname"));
                        emailTextView.setText("Email: " + data.getStringExtra("email"));
                        phoneTextView.setText("SDT: " + data.getStringExtra("phone"));
                    }
                });

        // Tìm ImageButton của nút quay lại
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Tìm Button của nút "Chỉnh sửa"
        Button editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> {
            // Truyền thông tin hiện tại sang Main_editthongtincanhan
            Intent intent = new Intent(Main_thongtin.this, Main_editthongtin.class);
            intent.putExtra("fullname", fullnameTextView.getText().toString().replace("Họ và tên: ", ""));
            intent.putExtra("email", emailTextView.getText().toString().replace("Email: ", ""));
            intent.putExtra("phone", phoneTextView.getText().toString().replace("SDT: ", ""));
            editInfoLauncher.launch(intent);
        });
    }
}