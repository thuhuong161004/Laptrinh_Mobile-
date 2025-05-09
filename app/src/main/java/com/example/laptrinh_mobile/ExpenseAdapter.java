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

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private List<Expense> expenseList;

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedAmount = formatter.format(expense.getAmount()) + " ₫";
        holder.tvAmount.setText(formattedAmount);
        holder.tvCategory.setText(expense.getCategory());
        holder.tvDate.setText(expense.getDate());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditExpenseActivity.class);
            intent.putExtra("EXPENSE_ID", expense.getId());
            intent.putExtra("EXPENSE_AMOUNT", expense.getAmount());
            intent.putExtra("EXPENSE_CATEGORY", expense.getCategory());
            intent.putExtra("EXPENSE_DATE", expense.getDate());
            intent.putExtra("EXPENSE_NOTE", expense.getNote());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa chi tiêu này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        dbHelper.deleteExpense(expense.getId());
                        expenseList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, expenseList.size());
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
        return expenseList.size();
    }

    public static class Expense {
        private long id;
        private double amount;
        private String category;
        private String date;
        private String note;

        public Expense(long id, double amount, String category, String date, String note) {
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

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvCategory, tvDate;
        Button btnEdit, btnDelete;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDate = itemView.findViewById(R.id.tv_date);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}