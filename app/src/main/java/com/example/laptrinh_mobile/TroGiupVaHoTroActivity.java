package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TroGiupVaHoTroActivity extends AppCompatActivity {
    Button btnCaiDatTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trogiupvahotro);

        btnCaiDatTaiKhoan = findViewById(R.id.btnCaiDatTaiKhoan); // Đảm bảo ID này có trong XML

        btnCaiDatTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TroGiupVaHoTroActivity.this, CaiDatTaiKhoanActivity.class);
                startActivity(intent);
            }
        });
    }
}
