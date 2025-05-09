package com.example.laptrinh_mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SavingsTracker {

    /**
     * Tính phần trăm thời gian đã trôi qua từ khi bắt đầu đến ngày hoàn thành
     * @param startDate ngày bắt đầu mục tiêu (định dạng dd/MM/yyyy)
     * @param endDate ngày hoàn thành mục tiêu (định dạng dd/MM/yyyy)
     * @return phần trăm thời gian đã trôi qua (0-100)
     */
    public static int calculateTimeProgress(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            Date today = Calendar.getInstance().getTime();

            if (start == null || end == null) return 0;

            long totalDuration = end.getTime() - start.getTime();
            long elapsedDuration = today.getTime() - start.getTime();

            if (totalDuration <= 0) return 100;

            double percentage = (double) elapsedDuration / totalDuration * 100;
            return (int) Math.min(100, Math.max(0, percentage));

        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * Tính phần trăm số tiền đã tiết kiệm được dựa trên số dư tài khoản và mục tiêu
     * @param initialBalance số tiền ban đầu trong tài khoản
     * @param targetAmount tổng số tiền mục tiêu muốn đạt được
     * @param currentBalance số tiền hiện có trong tài khoản
     * @return phần trăm đã tiết kiệm (0-100)
     */
    public static int calculateMoneyProgress(long initialBalance, long targetAmount, long currentBalance) {
        if (targetAmount <= initialBalance) return 100;

        // Số tiền cần tiết kiệm thêm để đạt mục tiêu
        long amountToSave = targetAmount - initialBalance;

        // Số tiền đã tiết kiệm thêm được
        long amountSaved = currentBalance - initialBalance;

        if (amountSaved < 0) amountSaved = 0;

        // Tính phần trăm tiến độ tiết kiệm
        double percentage = (double) amountSaved / amountToSave * 100;
        return (int) Math.min(100, Math.max(0, percentage));
    }

    /**
     * Tạo gợi ý tiết kiệm dựa trên thu nhập, mục tiêu và tiến độ
     * @param monthlyIncome thu nhập hàng tháng của người dùng
     * @param targetAmount tổng số tiền mục tiêu muốn đạt được
     * @param initialBalance số tiền ban đầu trong tài khoản
     * @param currentBalance số tiền hiện có
     * @param timeProgress phần trăm thời gian đã trôi qua
     * @return gợi ý định dạng chuỗi
     */
    public static String generateRecommendation(long monthlyIncome, long targetAmount,
                                                long initialBalance, long currentBalance, int timeProgress) {
        StringBuilder recommendation = new StringBuilder();

        // Gợi ý tiết kiệm 20% thu nhập mỗi tháng
        recommendation.append("• Tiết kiệm 20% thu nhập mỗi tháng.\n");

        // Tính phần trăm thời gian còn lại
        long amountToSave = targetAmount - initialBalance;
        long remaining = amountToSave - (currentBalance - initialBalance);
        if (remaining < 0) remaining = 0;

        int remainingTimePercent = 100 - timeProgress;

        if (remainingTimePercent > 0 && remaining > 0) {
            recommendation.append("• Tăng 5% số tiền tiết kiệm so với tháng trước.\n");
        }

        // Gợi ý về thời gian hoàn thành
        recommendation.append("• Thời gian hoàn thành còn lại ")
                .append(remainingTimePercent)
                .append("% để hoàn thành mục tiêu");

        return recommendation.toString();
    }


}