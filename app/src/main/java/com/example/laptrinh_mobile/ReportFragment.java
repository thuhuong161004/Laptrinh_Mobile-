package com.example.laptrinh_mobile;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class ReportFragment extends Fragment {

    private RadioGroup rgChartType;
    private RadioButton rbBarChart, rbPieChart;
    private BarChart barChart;
    private PieChart pieChart;
    private TextView tvReportTitle;
    private Button btnFilter;
    private DatabaseHelper dbHelper;
    private boolean showingExpenses = true; // true: Chi tiêu, false: Thu nhập
    private String startDate = "";
    private String endDate = "";
    private String displayStartDate = "";
    private String displayEndDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        rgChartType = view.findViewById(R.id.rg_chart_type);
        rbBarChart = view.findViewById(R.id.rb_bar_chart);
        rbPieChart = view.findViewById(R.id.rb_pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        pieChart = view.findViewById(R.id.pie_chart);
        tvReportTitle = view.findViewById(R.id.tv_report_title);
        btnFilter = view.findViewById(R.id.btn_filter);

        // Sử dụng DatabaseManager để lấy DatabaseHelper
        try {
            dbHelper = DatabaseManager.getInstance().getDatabaseHelper();
        } catch (IllegalStateException e) {
            if (getContext() != null) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể truy cập cơ sở dữ liệu: " + e.getMessage())
                        .setPositiveButton("OK", (dialog, which) -> requireActivity().finish())
                        .show();
            }
            return view;
        }

        // Cấu hình ban đầu cho biểu đồ
        setupCharts();

        // Mặc định hiển thị biểu đồ chi tiêu
        loadChartData(true, "", "");

        // Xử lý chọn loại biểu đồ
        rgChartType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_bar_chart) {
                barChart.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
            } else if (checkedId == R.id.rb_pie_chart) {
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
            }
            loadChartData(showingExpenses, startDate, endDate);
        });

        // Xử lý nút Lọc để hiển thị DatePicker
        btnFilter.setOnClickListener(v -> showDatePickerDialog());

        return view;
    }

    public void toggleData() {
        showingExpenses = !showingExpenses;
        loadChartData(showingExpenses, startDate, endDate);
        // Cập nhật menu toolbar
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).invalidateOptionsMenu();
        }
    }

    private void setupCharts() {
        // Cấu hình BarChart
        barChart.getDescription().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45f);

        // Định dạng giá trị trên BarChart (thêm đơn vị VND)
        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.0f VND", value);
            }
        });
        barChart.getAxisRight().setEnabled(false);

        // Cấu hình PieChart
        pieChart.getDescription().setEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void loadChartData(boolean isExpenses, String startDate, String endDate) {
        try {
            // Lấy dữ liệu từ DatabaseHelper
            Map<String, Double> summary = isExpenses
                    ? dbHelper.getExpenseSummary(startDate.isEmpty() ? "" : startDate, endDate.isEmpty() ? "" : endDate)
                    : dbHelper.getIncomeSummary(startDate.isEmpty() ? "" : startDate, endDate.isEmpty() ? "" : endDate);
            String dateRange = isExpenses ? dbHelper.getExpenseDateRange() : dbHelper.getIncomeDateRange();

            // Cập nhật tiêu đề với khoảng thời gian
            tvReportTitle.setText((isExpenses ? "Báo cáo Chi tiêu" : "Báo cáo Thu nhập") +
                    (startDate.isEmpty() && endDate.isEmpty() ? " (" + dateRange + ")" : " (Từ " + displayStartDate + " đến " + displayEndDate + ")"));

            // Kiểm tra nếu không có dữ liệu
            if (summary.isEmpty()) {
                Toast.makeText(getContext(), "Không có dữ liệu trong khoảng thời gian này", Toast.LENGTH_SHORT).show();
                barChart.setData(null);
                pieChart.setData(null);
                barChart.invalidate();
                pieChart.invalidate();
                return;
            }

            // Chuẩn bị dữ liệu cho biểu đồ
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            ArrayList<PieEntry> pieEntries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            int index = 0;
            for (Map.Entry<String, Double> entry : summary.entrySet()) {
                barEntries.add(new BarEntry(index, entry.getValue().floatValue()));
                pieEntries.add(new PieEntry(entry.getValue().floatValue(), entry.getKey() + " (" + String.format("%.0f VND", entry.getValue()) + ")"));
                labels.add(entry.getKey());
                index++;
            }

            // Cấu hình biểu đồ cột
            BarDataSet barDataSet = new BarDataSet(barEntries, isExpenses ? "Chi tiêu (VND)" : "Thu nhập (VND)");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);

            // Đặt nhãn danh mục cho trục X
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
            barChart.invalidate();

            // Cấu hình biểu đồ tròn
            PieDataSet pieDataSet = new PieDataSet(pieEntries, isExpenses ? "Chi tiêu (VND)" : "Thu nhập (VND)");
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData pieData = new PieData(pieDataSet);
            pieData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.format("%.0f VND", value);
                }
            });
            pieChart.setData(pieData);
            pieChart.invalidate();
        } catch (Exception e) {
            if (getContext() != null) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Lỗi")
                        .setMessage("Không thể tải dữ liệu biểu đồ: " + e.getMessage())
                        .setPositiveButton("OK", null)
                        .show();
            }
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialogStart = new DatePickerDialog(
                getContext(),
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    // Định dạng hiển thị: DD/MM/YYYY
                    displayStartDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, yearSelected);
                    // Định dạng gửi vào cơ sở dữ liệu: YYYY-MM-DD
                    startDate = String.format("%d-%02d-%02d", yearSelected, monthOfYear + 1, dayOfMonth);

                    DatePickerDialog datePickerDialogEnd = new DatePickerDialog(
                            getContext(),
                            (view1, yearEnd, monthEnd, dayEnd) -> {
                                // Định dạng hiển thị: DD/MM/YYYY
                                displayEndDate = String.format("%02d/%02d/%d", dayEnd, monthEnd + 1, yearEnd);
                                // Định dạng gửi vào cơ sở dữ liệu: YYYY-MM-DD
                                endDate = String.format("%d-%02d-%02d", yearEnd, monthEnd + 1, dayEnd);
                                loadChartData(showingExpenses, startDate, endDate);
                            },
                            year,
                            month,
                            day
                    );
                    datePickerDialogEnd.show();
                },
                year,
                month,
                day
        );
        datePickerDialogStart.show();
    }
}