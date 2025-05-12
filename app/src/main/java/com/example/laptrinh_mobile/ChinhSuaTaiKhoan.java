package com.example.laptrinh_mobile;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ChinhSuaTaiKhoan extends Activity{
    private TextView edtTen, edtEmail, edtSDT;
    private Button btnChinhSua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chinhsuataikhoan);

        edtTen = findViewById(R.id.edtTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtSDT = findViewById(R.id.edtSDT);
        btnChinhSua = findViewById(R.id.btnChinhSua);

        // Thiết lập dữ liệu mặc định
        edtTen.setText("Họ và tên: Nguyễn Văn A");
        edtEmail.setText("Email: vana@gmail.com");
        edtSDT.setText("SDT: 0914275320");

        btnChinhSua.setOnClickListener(v -> {
            Intent intent = new Intent(ChinhSuaTaiKhoan.this, com.example.laptrinh_mobile.LuuTaiKhoanActivity.class);
            intent.putExtra("ten", "Nguyễn Văn A");
            intent.putExtra("email", "vana@gmail.com");
            intent.putExtra("sdt", "0914275320");
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String tenMoi = data.getStringExtra("ten");
            String emailMoi = data.getStringExtra("email");
            String sdtMoi = data.getStringExtra("sdt");

            edtTen.setText("Họ và tên: " + tenMoi);
            edtEmail.setText("Email: " + emailMoi);
            edtSDT.setText("SDT: " + sdtMoi);
        }
    }


}
