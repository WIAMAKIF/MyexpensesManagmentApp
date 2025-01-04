package com.example.myapplicationmanageexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView totalExpenseTextView;
    private RecyclerView expensesRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private Button addExpenseButton;
    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalExpenseTextView = findViewById(R.id.textViewTotalExpense);
        expensesRecyclerView = findViewById(R.id.recyclerViewExpenses);
        addExpenseButton = findViewById(R.id.buttonAddExpense);

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        expensesRecyclerView.setAdapter(expenseAdapter);

        firestore = FirebaseFirestore.getInstance();

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        fetchExpenses();

        addExpenseButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });
    }


    private void fetchExpenses() {
        firestore.collection("Expenses")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        expenseList.clear();
                        int total = 0;
                        for (DocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            String date = document.getString("date");
                            String category = document.getString("category");
                            double amount = document.getDouble("amount");

                            expenseList.add(new Expense(description, amount, category, date));
                            total += amount;
                        }
                        expenseAdapter.notifyDataSetChanged();
                        totalExpenseTextView.setText("Total Expense: $" + total);
                    } else {
                        Log.e("MainActivity", "Error fetching expenses", task.getException());
                    }
                });
    }
}
