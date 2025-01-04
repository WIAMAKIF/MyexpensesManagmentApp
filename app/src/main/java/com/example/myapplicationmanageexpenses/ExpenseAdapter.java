package com.example.myapplicationmanageexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;

    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    public ExpenseAdapter(){}

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.descriptionTextView.setText(expense.getDescription());
        holder.dateTextView.setText(expense.getDate());
        holder.categoryTextView.setText(expense.getCategory());
        holder.amountTextView.setText("$" + expense.getAmount());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionTextView, dateTextView, categoryTextView, amountTextView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.textViewDescription);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            amountTextView = itemView.findViewById(R.id.textViewAmount);
        }
    }
}
