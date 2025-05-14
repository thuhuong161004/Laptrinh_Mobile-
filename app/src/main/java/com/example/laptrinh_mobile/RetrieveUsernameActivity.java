package com.example.laptrinh_mobile;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrieveUsernameActivity extends AppCompatActivity {

    private static final String TAG = "RetrieveUsernameActivity";
    private EditText etEmail;
    private Button btnRetrieveUsername;
    private TextView tvUsernameResult;
    private TextView tvBack;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_username);

        etEmail = findViewById(R.id.et_email);
        btnRetrieveUsername = findViewById(R.id.btn_retrieve_username);
        tvUsernameResult = findViewById(R.id.tv_username_result);
        tvBack = findViewById(R.id.tv_back);
        databaseReference = FirebaseDatabase.getInstance("https://laptrinh-mobile.firebaseio.com/").getReference("users");

        btnRetrieveUsername.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Log.w(TAG, "Email trống");
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Log.w(TAG, "Email không hợp lệ: " + email);
                Toast.makeText(this, "Email không hợp lệ. Vui lòng nhập email đúng định dạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Tìm username cho email: " + email);
            databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String username = userSnapshot.child("username").getValue(String.class);
                            if (username != null) {
                                Log.d(TAG, "Tìm thấy username: " + username + " cho email: " + email);
                                tvUsernameResult.setText("Tên đăng nhập của bạn: " + username);
                                tvUsernameResult.setVisibility(TextView.VISIBLE);
                                return;
                            }
                        }
                        Log.w(TAG, "Không tìm thấy username cho email: " + email);
                        Toast.makeText(RetrieveUsernameActivity.this, "Không tìm thấy username cho email này", Toast.LENGTH_LONG).show();
                    } else {
                        Log.w(TAG, "Email không tồn tại: " + email);
                        Toast.makeText(RetrieveUsernameActivity.this, "Email không tồn tại", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    String errorMessage = databaseError.getMessage() != null ? databaseError.getMessage() : "Lỗi truy vấn không xác định";
                    Log.e(TAG, "Lỗi truy vấn database: " + errorMessage);
                    Toast.makeText(RetrieveUsernameActivity.this, "Lỗi truy vấn: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        tvBack.setOnClickListener(v -> {
            Log.d(TAG, "Người dùng nhấn Quay lại");
            finish();
        });
    }
}