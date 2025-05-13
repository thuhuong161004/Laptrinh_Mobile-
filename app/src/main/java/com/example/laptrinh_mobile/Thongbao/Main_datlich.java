package com.example.laptrinh_mobile.Thongbao;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.laptrinh_mobile.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Main_datlich extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_REMINDER = 1001;

    private TextView selectedDateView = null; // Lưu TextView của ngày được chọn trước đó
    private int selectedDay = -1; // Lưu ngày được chọn
    private int selectedMonth = 5; // Mặc định là tháng 5 (1-12)
    private int selectedYear = 2025; // Mặc định là năm 2025
    private Spinner monthSpinner; // Spinner cho tháng
    private TextView yearTextView; // TextView cho năm
    private TextView monthYearTextView; // TextView hiển thị "Tháng X YYYY"
    private GridLayout calendarGrid; // GridLayout chứa lịch

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_datlich);

        // Xử lý nút quay lại (mũi tên)
        ImageButton backButton = findViewById(R.id.back_button);
        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        } else {
            Toast.makeText(this, "Không tìm thấy nút quay lại!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý nút "dấu cộng"
        ImageButton addButton = findViewById(R.id.button_add);
        if (addButton != null) {
            addButton.setOnClickListener(v -> {
                if (selectedDay == -1) {
                    Toast.makeText(this, "Vui lòng chọn một ngày trước!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Main_datlich.this, Main_tgdatlich.class);
                intent.putExtra("selectedDay", selectedDay);
                intent.putExtra("selectedMonth", selectedMonth);
                intent.putExtra("selectedYear", selectedYear);
                startActivityForResult(intent, REQUEST_CODE_ADD_REMINDER);
            });
        } else {
            Toast.makeText(this, "Không tìm thấy nút thêm!", Toast.LENGTH_SHORT).show();
        }

        // Khởi tạo TextView hiển thị "Tháng X YYYY"
        monthYearTextView = findViewById(R.id.month_year_textview);
        if (monthYearTextView != null) {
            monthYearTextView.setText("Tháng " + selectedMonth + " " + selectedYear);
        }

        // Khởi tạo Spinner cho tháng
        monthSpinner = findViewById(R.id.month_spinner);
        if (monthSpinner != null) {
            setupMonthSpinner();
        } else {
            Toast.makeText(this, "Không tìm thấy Spinner tháng!", Toast.LENGTH_SHORT).show();
        }

        // Khởi tạo TextView cho năm
        yearTextView = findViewById(R.id.year_textview);
        if (yearTextView != null) {
            yearTextView.setText(String.valueOf(selectedYear));
            yearTextView.setOnClickListener(v -> showYearPicker());
        } else {
            Toast.makeText(this, "Không tìm thấy TextView năm!", Toast.LENGTH_SHORT).show();
        }

        // Lấy GridLayout chứa các ngày
        calendarGrid = findViewById(R.id.calendar_grid);
        if (calendarGrid != null) {
            updateCalendar(); // Cập nhật lịch lần đầu
        } else {
            Toast.makeText(this, "GridLayout calendar_grid không được tìm thấy!", Toast.LENGTH_SHORT).show();
        }
    }

    // Cài đặt Spinner cho tháng
    private void setupMonthSpinner() {
        List<String> months = new ArrayList<>();
        months.add("Tháng 1");
        months.add("Tháng 2");
        months.add("Tháng 3");
        months.add("Tháng 4");
        months.add("Tháng 5");
        months.add("Tháng 6");
        months.add("Tháng 7");
        months.add("Tháng 8");
        months.add("Tháng 9");
        months.add("Tháng 10");
        months.add("Tháng 11");
        months.add("Tháng 12");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        // Đặt tháng mặc định là tháng 5
        monthSpinner.setSelection(selectedMonth - 1);

        // Xử lý sự kiện chọn tháng
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMonth = position + 1;
                Toast.makeText(Main_datlich.this, "Tháng được chọn: " + selectedMonth, Toast.LENGTH_SHORT).show();
                updateCalendar();
                monthYearTextView.setText("Tháng " + selectedMonth + " " + selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn
            }
        });
    }

    // Chọn năm bằng DatePickerDialog
    private void showYearPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedYear = year;
            yearTextView.setText(String.valueOf(selectedYear));
            Toast.makeText(Main_datlich.this, "Năm được chọn: " + selectedYear, Toast.LENGTH_SHORT).show();
            updateCalendar();
            monthYearTextView.setText("Tháng " + selectedMonth + " " + selectedYear);
        }, selectedYear, selectedMonth - 1, 1);
        datePickerDialog.show();
    }

    // Cập nhật lịch dựa trên tháng và năm được chọn
    private void updateCalendar() {
        // Xóa các ngày cũ (giữ lại tiêu đề ngày trong tuần: S, M, T, W, T, F, S)
        for (int i = calendarGrid.getChildCount() - 1; i >= 7; i--) {
            calendarGrid.removeViewAt(i);
        }

        // Tính toán lịch
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Ánh xạ ngày đầu tiên vào GridLayout (điều chỉnh vì GridLayout bắt đầu từ Chủ nhật)
        int startPosition = firstDayOfWeek - 1;

        // Thêm các ô trống trước ngày đầu tiên
        for (int i = 0; i < startPosition; i++) {
            TextView emptyView = new TextView(this);
            emptyView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)));
            emptyView.setGravity(android.view.Gravity.CENTER);
            emptyView.setText("");
            emptyView.setTextSize(16);
            calendarGrid.addView(emptyView);
        }

        // Thêm các ngày trong tháng
        for (int day = 1; day <= daysInMonth; day++) {
            TextView dateView = new TextView(this);
            dateView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)));
            dateView.setGravity(android.view.Gravity.CENTER);
            dateView.setText(String.valueOf(day));
            dateView.setTextSize(16);

            // Thêm sự kiện nhấn cho ngày
            final int finalDay = day;
            dateView.setOnClickListener(v -> {
                if (selectedDateView != null) {
                    selectedDateView.setBackgroundResource(android.R.color.transparent);
                }
                dateView.setBackgroundResource(R.color.holo_green_light);
                selectedDateView = dateView;
                selectedDay = finalDay;
                Toast.makeText(Main_datlich.this, "Ngày được chọn: " + selectedDay + "/" + selectedMonth + "/" + selectedYear, Toast.LENGTH_SHORT).show();
            });

            calendarGrid.addView(dateView);
        }
    }

    // Kiểm tra xem text có phải là tiêu đề ngày trong tuần (S, M, T, W, T, F, S) không
    private boolean isDayOfWeek(String text) {
        return text.equals("S") || text.equals("M") || text.equals("T") || text.equals("W") || text.equals("F");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_REMINDER && resultCode == RESULT_OK && data != null) {
            selectedDay = data.getIntExtra("selectedDay", selectedDay);
            selectedMonth = data.getIntExtra("selectedMonth", selectedMonth);
            selectedYear = data.getIntExtra("selectedYear", selectedYear);

            // Cập nhật lịch và giao diện
            monthSpinner.setSelection(selectedMonth - 1);
            yearTextView.setText(String.valueOf(selectedYear));
            monthYearTextView.setText("Tháng " + selectedMonth + " " + selectedYear);
            updateCalendar();

            // Tìm và highlight ngày được chọn
            for (int i = 7; i < calendarGrid.getChildCount(); i++) {
                TextView dateView = (TextView) calendarGrid.getChildAt(i);
                if (dateView.getText().toString().equals(String.valueOf(selectedDay))) {
                    if (selectedDateView != null) {
                        selectedDateView.setBackgroundResource(android.R.color.transparent);
                    }
                    dateView.setBackgroundResource(R.color.holo_green_light);
                    selectedDateView = dateView;
                    break;
                }
            }
        }
    }
}
