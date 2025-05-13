package com.example.laptrinh_mobile.Thongbao;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.laptrinh_mobile.R;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class Main_tgdatlich extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_REMINDER = 1001;
    private static final int ALARM_REQUEST_CODE = 1002;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    private ActivityResultLauncher<Intent> selectDateLauncher;

    private EditText editNgay, editThoiGian, editMuc;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private int selectedHour;
    private int selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tgdatlich);

        // Kiểm tra và yêu cầu quyền POST_NOTIFICATIONS (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Khởi tạo ActivityResultLauncher để thay thế startActivityForResult
        selectDateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        selectedDay = data.getIntExtra("selectedDay", selectedDay);
                        selectedMonth = data.getIntExtra("selectedMonth", selectedMonth);
                        selectedYear = data.getIntExtra("selectedYear", selectedYear);
                        String date = String.format("%02d-%02d-%d", selectedDay, selectedMonth, selectedYear);
                        editNgay.setText(date);
                    }
                });

        // Lấy thời gian hiện tại từ hệ thống
        Calendar currentCalendar = Calendar.getInstance();
        selectedDay = currentCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonth = currentCalendar.get(Calendar.MONTH) + 1; // MONTH bắt đầu từ 0
        selectedYear = currentCalendar.get(Calendar.YEAR);
        selectedHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = currentCalendar.get(Calendar.MINUTE);

        // Nhận dữ liệu từ Main_datlich (ưu tiên dữ liệu từ Intent nếu có)
        Intent intent = getIntent();
        selectedDay = intent.getIntExtra("selectedDay", selectedDay);
        selectedMonth = intent.getIntExtra("selectedMonth", selectedMonth);
        selectedYear = intent.getIntExtra("selectedYear", selectedYear);

        // Hiển thị ngày/tháng/năm và giờ/phút hiện tại trong EditText
        editNgay = findViewById(R.id.edit_ngay);
        editThoiGian = findViewById(R.id.edit_thoi_gian);
        editMuc = findViewById(R.id.edit_muc);
        String date = String.format("%02d-%02d-%d", selectedDay, selectedMonth, selectedYear);
        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
        editNgay.setText(date);
        editThoiGian.setText(time);

        // Thêm TimePickerDialog cho editThoiGian
        editThoiGian.setOnClickListener(v -> {
            Calendar currentTime = Calendar.getInstance();
            int hour = currentTime.get(Calendar.HOUR_OF_DAY);
            int minute = currentTime.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, min) -> {
                selectedHour = hourOfDay;
                selectedMinute = min;
                editThoiGian.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
            }, hour, minute, true);
            timePickerDialog.show();
        });

        // Xử lý nút "Hủy"
        MaterialButton buttonHuy = findViewById(R.id.button_huy);
        if (buttonHuy != null) {
            buttonHuy.setOnClickListener(v -> {
                Intent intentHuy = new Intent(Main_tgdatlich.this, Main_datlich.class);
                startActivity(intentHuy);
                finish(); // Đóng activity hiện tại
            });
        } else {
            Toast.makeText(this, "Không tìm thấy nút Hủy!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý nút "Lưu"
        MaterialButton buttonLuu = findViewById(R.id.button_luu);
        if (buttonLuu != null) {
            buttonLuu.setOnClickListener(v -> {
                if (selectedDay == -1 || editThoiGian.getText().toString().isEmpty() || editMuc.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra và lập lịch thông báo
                if (scheduleNotification()) {
                    // Chuyển sang màn hình thành công
                    Intent intentLuu = new Intent(Main_tgdatlich.this, Main_datthanhcong.class);
                    intentLuu.putExtra("selectedDay", selectedDay);
                    intentLuu.putExtra("selectedMonth", selectedMonth);
                    intentLuu.putExtra("selectedYear", selectedYear);
                    startActivity(intentLuu);
                    finish(); // Đóng activity hiện tại
                }
            });
        } else {
            Toast.makeText(this, "Không tìm thấy nút Lưu!", Toast.LENGTH_SHORT).show();
        }

        // Xử lý nút hình lịch để quay lại chọn ngày
        ImageView calendarIcon = findViewById(R.id.calendar_icon);
        if (calendarIcon != null) {
            calendarIcon.setOnClickListener(v -> {
                Intent returnIntent = new Intent(Main_tgdatlich.this, Main_datlich.class);
                returnIntent.putExtra("selectedDay", selectedDay);
                returnIntent.putExtra("selectedMonth", selectedMonth);
                returnIntent.putExtra("selectedYear", selectedYear);
                selectDateLauncher.launch(returnIntent);
            });
        } else {
            Toast.makeText(this, "Không tìm thấy nút lịch!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Quyền thông báo đã được cấp!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền thông báo để hoạt động!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean scheduleNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Toast.makeText(this, "Không thể truy cập AlarmManager!", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra quyền lập lịch thông báo chính xác (Android 12+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            Toast.makeText(this, "Ứng dụng cần quyền để lập lịch thông báo chính xác!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(intent);
            return false;
        }

        Intent intent = new Intent(this, Nhanthongbao.class);
        intent.putExtra("muc", editMuc.getText().toString());

        // Thiết lập thời gian thông báo
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth - 1, selectedDay, selectedHour, selectedMinute, 0);
        long triggerTime = calendar.getTimeInMillis();

        // Kiểm tra nếu thời gian đã qua, đặt cho ngày tiếp theo
        if (triggerTime < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
            triggerTime = calendar.getTimeInMillis();
        }

        // Tạo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Lập lịch thông báo với xử lý SecurityException
        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            Toast.makeText(this, "Thông báo đã được lập lịch!", Toast.LENGTH_SHORT).show();
            return true;
        } catch (SecurityException e) {
            Toast.makeText(this, "Không có quyền lập lịch thông báo chính xác!", Toast.LENGTH_LONG).show();
            Intent permissionIntent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
            startActivity(permissionIntent);
            return false;
        }
    }
}
