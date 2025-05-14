package com.example.laptrinh_mobile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditExpenseActivity extends AppCompatActivity {
    private EditText etAmount, etDescription, etDate;
    private Spinner spinnerCategory;
    private DatabaseHelper dbHelper;
    private Calendar calendar;
    private int expenseId; // Sửa từ long sang int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        try {
            dbHelper = DatabaseManager.getInstance().getDatabaseHelper();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        calendar = Calendar.getInstance();
        etAmount = findViewById(R.id.et_amount);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        spinnerCategory = findViewById(R.id.spinner_category);

        expenseId = getIntent().getIntExtra("expenseId", -1); // Sửa từ getLongExtra sang getIntExtra
        double amount = getIntent().getDoubleExtra("amount", 0.0);
        String category = getIntent().getStringExtra("category");
        String date = getIntent().getStringExtra("date");
        String description = getIntent().getStringExtra("description");

        String[] categories = {"Ăn uống", "Di chuyển", "Mua sắm", "Hóa đơn", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        etAmount.setText(String.valueOf(amount));
        etDescription.setText(description);
        etDate.setText(date);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            calendar.setTime(dateFormat.parse(date));
        } catch (Exception e) {
            Toast.makeText(this, "Ngày không hợp lệ, sử dụng ngày hiện tại", Toast.LENGTH_SHORT).show();
            calendar = Calendar.getInstance();
        }

        etDate.setOnClickListener(v -> showDatePickerDialog());

        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setText("Cập nhật");
        btnSave.setOnClickListener(v -> saveExpense());

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            updateDateField();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateField() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(dateFormat.format(calendar.getTime()));
    }

    private void saveExpense() {
        String amountStr = etAmount.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String description = etDescription.getText().toString();

        if (amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (expenseId == -1) {
                Toast.makeText(this, "ID chi tiêu không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            int rowsAffected = dbHelper.updateExpense(expenseId, amount, category, date, description);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Đã cập nhật chi tiêu", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật chi tiêu", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}