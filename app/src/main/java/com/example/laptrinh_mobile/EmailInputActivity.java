package com.example.laptrinh_mobile;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;
import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;

public class EmailInputActivity extends AppCompatActivity {

    private static final String TAG = "EmailInputActivity CHRISTIAN";
    private EditText etEmail;
    private Button btnSendOtpEmail;
    private String generatedOtp;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    // Cập nhật thông tin Gmail
    private static final String SMTP_USERNAME = "hh.nguyen2004@gmail.com";
    private static final String SMTP_PASSWORD = "jfsn axsx hqyq wvcl"; // Mật khẩu ứng dụng mới
    private static final int MAX_RETRIES = 2; // Số lần thử lại
    private static final int RETRY_DELAY_MS = 1000; // Thời gian chờ giữa các lần thử (ms)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_input);

        etEmail = findViewById(R.id.et_email);
        btnSendOtpEmail = findViewById(R.id.btn_send_otp_email);

        btnSendOtpEmail.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Log.w(TAG, "Email trống");
                Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                Log.w(TAG, "Email không hợp lệ: " + email);
                Toast.makeText(this, "Email không hợp lệ. Vui lòng nhập email đúng định dạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                Log.w(TAG, "Không có kết nối mạng");
                Toast.makeText(this, "Không có kết nối mạng. Vui lòng kiểm tra kết nối internet!", Toast.LENGTH_LONG).show();
                return;
            }

            generatedOtp = String.format("%06d", new Random().nextInt(999999));
            Log.d(TAG, "Tạo OTP: " + generatedOtp + " cho email: " + email);

            btnSendOtpEmail.setEnabled(false);
            Toast.makeText(this, "Đang gửi OTP...", Toast.LENGTH_SHORT).show();

            new Thread(() -> {
                int attempt = 0;
                boolean success = false;
                Exception lastException = null;

                while (attempt <= MAX_RETRIES && !success) {
                    try {
                        long startTime = System.currentTimeMillis();
                        Log.d(TAG, "Thử gửi email lần " + (attempt + 1) + " đến: " + email);
                        sendOtpEmail(email, generatedOtp, attempt == MAX_RETRIES);
                        success = true;
                        runOnUiThread(() -> {
                            long duration = System.currentTimeMillis() - startTime;
                            Log.d(TAG, "Gửi OTP thành công đến: " + email + " trong " + duration + "ms");
                            Toast.makeText(this, "Mã OTP đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EmailInputActivity.this, VerifyOtpActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("otp", generatedOtp);
                            startActivity(intent);
                            btnSendOtpEmail.setEnabled(true);
                        });
                    } catch (AuthenticationFailedException e) {
                        lastException = e;
                        runOnUiThread(() -> {
                            Log.e(TAG, "Xác thực Gmail thất bại: " + e.getMessage(), e);
                            Toast.makeText(this, "Lỗi xác thực Gmail. Vui lòng kiểm tra App Password!", Toast.LENGTH_LONG).show();
                            btnSendOtpEmail.setEnabled(true);
                        });
                        break; // Không thử lại nếu lỗi xác thực
                    } catch (SSLHandshakeException e) {
                        lastException = e;
                        Log.e(TAG, "Lỗi SSL lần " + (attempt + 1) + ": " + e.getMessage(), e);
                    } catch (UnknownHostException e) {
                        lastException = e;
                        Log.e(TAG, "Không tìm thấy máy chủ email lần " + (attempt + 1) + ": " + e.getMessage(), e);
                    } catch (MessagingException e) {
                        lastException = e;
                        Log.e(TAG, "Lỗi gửi email lần " + (attempt + 1) + ": " + e.getMessage(), e);
                    } catch (IOException e) {
                        lastException = e;
                        Log.e(TAG, "Lỗi mạng lần " + (attempt + 1) + ": " + e.getMessage(), e);
                    } catch (Exception e) {
                        lastException = e;
                        Log.e(TAG, "Lỗi không xác định lần " + (attempt + 1) + ": " + e.getMessage(), e);
                    }

                    if (!success && attempt < MAX_RETRIES) {
                        try {
                            Thread.sleep(RETRY_DELAY_MS);
                        } catch (InterruptedException ie) {
                            Log.e(TAG, "Lỗi khi chờ thử lại: " + ie.getMessage(), ie);
                        }
                        attempt++;
                    }
                }

                if (!success && lastException != null) {
                    final Exception finalException = lastException;
                    runOnUiThread(() -> {
                        Log.e(TAG, "Gửi email thất bại sau " + (MAX_RETRIES + 1) + " lần: " + finalException.getMessage(), finalException);
                        Toast.makeText(this, "Lỗi gửi email: " + finalException.getMessage(), Toast.LENGTH_LONG).show();
                        btnSendOtpEmail.setEnabled(true);
                    });
                }
            }).start();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void sendOtpEmail(String toEmail, String otp, boolean useSsl) throws MessagingException, IOException {
        Log.d(TAG, "Cấu hình SMTP (useSsl=" + useSsl + ") cho email: " + toEmail);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        if (useSsl) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        }
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", useSsl ? "465" : "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Log.d(TAG, "Tạo session SMTP");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                Log.d(TAG, "Xác thực với username: " + SMTP_USERNAME);
                return new javax.mail.PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        session.setDebug(true);

        Log.d(TAG, "Tạo message cho email: " + toEmail);
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Xác minh mã OTP");
        message.setText("Mã OTP của bạn là: " + otp + ". Vui lòng không chia sẻ mã này!");

        Log.d(TAG, "Gửi email đến: " + toEmail);
        Transport.send(message);
        Log.d(TAG, "Hoàn tất gửi email đến: " + toEmail);
    }
}