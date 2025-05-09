package com.example.laptrinh_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    private Context context;
    private List<Income> incomeList;

    public IncomeAdapter(Context context, List<Income> incomeList) {
        this.context = context;
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_income, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Income income = incomeList.get(position);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedAmount = formatter.format(income.getAmount()) + " ₫";
        holder.tvAmount.setText(formattedAmount);
        holder.tvCategory.setText(income.getCategory());
        holder.tvDate.setText(income.getDate());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditIncomeActivity.class);
            intent.putExtra("INCOME_ID", income.getId());
            intent.putExtra("INCOME_AMOUNT", income.getAmount());
            intent.putExtra("INCOME_CATEGORY", income.getCategory());
            intent.putExtra("INCOME_DATE", income.getDate());
            intent.putExtra("INCOME_NOTE", income.getNote());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa thu nhập này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.deleteIncome(income.getId());
                        incomeList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, incomeList.size());
                        if (context instanceof MainActivity) {
                            ((MainActivity) context).refreshFragment();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }

    public static class Income {
        private long id;
        private double amount;
        private String category;
        private String date;
        private String note;

        public Income(long id, double amount, String category, String date, String note) {
            this.id = id;
            this.amount = amount;
            this.category = category;
            this.date = date;
            this.note = note;
        }

        public long getId() {
            return id;
        }

        public double getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        public String getDate() {
            return date;
        }

        public String getNote() {
            return note;
        }
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory, tvDate;
        Button btnEdit, btnDelete;

        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}