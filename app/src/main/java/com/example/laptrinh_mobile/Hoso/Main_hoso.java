package com.example.laptrinh_mobile.Hoso;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.laptrinh_mobile.R;
import com.example.laptrinh_mobile.Thongbao.Main_thongbao;

public class Main_hoso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hoso);

        // Tìm LinearLayout của biểu tượng "Thông báo" trong bottom navigation
        LinearLayout navNotifications = findViewById(R.id.navNotifications);
        navNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_thongbao.class);
            startActivity(intent);
            finish();
        });

        // Tìm CardView của "Tài khoản cá nhân"
        CardView taikhoanCard = findViewById(R.id.taikhoancanhan);
        taikhoanCard.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_thongtin.class);
            startActivity(intent);
        });

        // Tìm CardView của "Cài đặt"
        CardView caidatCard = findViewById(R.id.caidat);
        caidatCard.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_caidat.class);
            startActivity(intent);
        });

        // Tìm CardView của "Đieukhoan"
        CardView dieukhoanCard = findViewById(R.id.dieukhoan);
        dieukhoanCard.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_dieukhoan.class);
            startActivity(intent);
        });

        // Tìm CardView của "Trogiup"
        CardView trogiupCard = findViewById(R.id.trogiup);
        trogiupCard.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_trogiup.class);
            startActivity(intent);
        });

        // Tìm CardView của "Đánh giá"
        CardView danhgiaCard = findViewById(R.id.danhgia);
        danhgiaCard.setOnClickListener(v -> {
            Intent intent = new Intent(Main_hoso.this, Main_danhgia.class);
            startActivity(intent);
        });
    }
}