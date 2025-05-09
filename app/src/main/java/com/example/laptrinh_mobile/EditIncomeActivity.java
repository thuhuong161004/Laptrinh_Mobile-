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

public class EditIncomeActivity extends AppCompatActivity {

    private EditText etAmount, etNote, etDate;
    private Spinner spinnerCategory;
    private DatabaseHelper dbHelper;
    private long incomeId;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        dbHelper = new DatabaseHelper(this);
        calendar = Calendar.getInstance();

        etAmount = findViewById(R.id.et_amount);
        etNote = findViewById(R.id.et_note);
        etDate = findViewById(R.id.et_date);
        spinnerCategory = findViewById(R.id.spinner_category);

        // Thiết lập Spinner cho danh mục thu nhập
        String[] categories = {"Tiền lương", "Tiền thưởng", "Tiền đầu tư", "Tiền trợ cấp", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Lấy dữ liệu từ Intent
        incomeId = getIntent().getLongExtra("INCOME_ID", -1);
        double amount = getIntent().getDoubleExtra("INCOME_AMOUNT", 0.0);
        String category = getIntent().getStringExtra("INCOME_CATEGORY");
        String date = getIntent().getStringExtra("INCOME_DATE");
        String note = getIntent().getStringExtra("INCOME_NOTE");

        // Hiển thị dữ liệu lên giao diện
        etAmount.setText(String.valueOf(amount));
        etNote.setText(note != null ? note : "");
        etDate.setText(date);

        // Thiết lập danh mục trong Spinner
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(category)) {
                spinnerCategory.setSelection(i);
                break;
            }
        }

        // Thiết lập ngày từ dữ liệu
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            calendar.setTime(dateFormat.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Xử lý chọn ngày
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý nút Cập nhật
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setText("Cập nhật");
        btnSave.setOnClickListener(v -> updateIncome());

        // Xử lý nút Quay lại
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

    private void updateIncome() {
        String amountStr = etAmount.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        String date = etDate.getText().toString();
        String note = etNote.getText().toString();

        if (amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            int rowsAffected = dbHelper.updateIncome(incomeId, amount, category, date, note);
            if (rowsAffected > 0) {
                Toast.makeText(this, "Đã cập nhật thu nhập", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật thu nhập", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tiền không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}