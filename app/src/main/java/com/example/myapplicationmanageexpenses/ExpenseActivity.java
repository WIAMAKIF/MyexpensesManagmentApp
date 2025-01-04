package com.example.myapplicationmanageexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {

    private RecyclerView expenseRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private FirebaseFirestore db;
    private TextView totalExpenseTextView;
    private Button addExpenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseRecyclerView = findViewById(R.id.recyclerViewExpenses);  // Corrected ID
        totalExpenseTextView = findViewById(R.id.textViewTotalExpense);  // Corrected ID
        addExpenseButton = findViewById(R.id.buttonAddExpense);  // Corrected ID

        db = FirebaseFirestore.getInstance();
        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(this, expenseList);

        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        expenseRecyclerView.setAdapter(expenseAdapter);

        fetchExpenses();

        addExpenseButton.setOnClickListener(view -> {
            startActivity(new Intent(ExpenseActivity.this, AddExpenseActivity.class));
        });
    }

    private void fetchExpenses() {
        db.collection("Expenses")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        expenseList.clear();
                        double total = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            double amount = document.getDouble("amount");
                            String category = document.getString("category");
                            String date = document.getString("date");

                            expenseList.add(new Expense(description, amount, category, date));
                            total += amount;
                        }
                        expenseAdapter.notifyDataSetChanged();
                        totalExpenseTextView.setText("Total Expense: $" + total);
                    } else {
                        totalExpenseTextView.setText("Error fetching expenses");
                    }
                });
    }
}
