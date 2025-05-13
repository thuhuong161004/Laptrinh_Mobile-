package com.example.laptrinh_mobile.Thongbao;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.laptrinh_mobile.R;

public class Nhanthongbao extends BroadcastReceiver {

    private static final String CHANNEL_ID = "reminder_channel";
    private static final int NOTIFICATION_ID = 1003;

    @Override
    public void onReceive(Context context, Intent intent) {
        String muc = intent.getStringExtra("muc");

        // Tạo Notification Channel (yêu cầu cho Android 8.0 trở lên)
        createNotificationChannel(context);

        // Tạo Intent để mở Main_thongbao khi nhấp vào thông báo
        Intent notificationIntent = new Intent(context, Main_thongbao.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getNotificationIcon(context))
                .setContentTitle("Nhắc nhở")
                .setContentText("Đã đến giờ nhắc nhở: " + (muc != null ? muc : "Không có nội dung"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Hiển thị thông báo với xử lý ngoại lệ
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.e("Nhanthongbao", "Không có quyền POST_NOTIFICATIONS, không thể hiển thị thông báo");
                return;
            }
            try {
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } catch (SecurityException e) {
                Log.e("Nhanthongbao", "Không thể hiển thị thông báo: " + e.getMessage());
            } catch (Exception e) {
                Log.e("Nhanthongbao", "Lỗi khi hiển thị thông báo: " + e.getMessage());
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Channel";
            String description = "Channel for reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private int getNotificationIcon(Context context) {
        // Kiểm tra xem tài nguyên thongbao1 có tồn tại không
        int resourceId = context.getResources().getIdentifier("thongbao1", "drawable", context.getPackageName());
        if (resourceId != 0) {
            return resourceId;
        }
        return android.R.drawable.ic_dialog_info; // Icon mặc định nếu không tìm thấy
    }
}
