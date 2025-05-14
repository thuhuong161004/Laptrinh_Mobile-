package com.example.laptrinh_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {

    private Button btnLogout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnLogout = findViewById(R.id.btn_logout);
        mAuth = FirebaseAuth.getInstance();

        btnLogout.setOnClickListener(v -> {
            try {
                mAuth.signOut();
                DatabaseManager.getInstance().clear();
                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi khi đăng xuất: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}