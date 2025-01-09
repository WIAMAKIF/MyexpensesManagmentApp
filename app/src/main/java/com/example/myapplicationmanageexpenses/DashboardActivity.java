package com.example.myapplicationmanageexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private TextView totalExpensesText, recentExpensesText;
    private FirebaseFirestore db;
    private List<Expense> recentExpensesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        totalExpensesText = findViewById(R.id.totalExpensesText);
        recentExpensesText = findViewById(R.id.recentExpensesText);
        db = FirebaseFirestore.getInstance();

        recentExpensesList = new ArrayList<>();



        fetchRecentExpenses();

        Button viewExpensesButton = findViewById(R.id.buttonViewExpenses);
        Button addExpenseButton = findViewById(R.id.buttonAddExpense);
        Button manageCategoriesButton = findViewById(R.id.buttonManageCategories);

        Button logoutButton = findViewById(R.id.buttonLogout);

        viewExpensesButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ExpenseActivity.class));
        });

        addExpenseButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, AddExpenseActivity.class));
        });

        manageCategoriesButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, CategoryManagementActivity.class));
        });

        fetchTotalExpensesFromSummary();

        logoutButton.setOnClickListener(v -> {
            finish(); // Close the app or redirect to login screen
        });
    }

    private void fetchRecentExpenses() {
        db.collection("Expenses")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        recentExpensesList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            String description = document.getString("description");
                            double amount = document.getDouble("amount");
                            String category = document.getString("category");
                            String date = document.getString("date");

                            recentExpensesList.add(new Expense(description, amount, category, date));
                        }
                        updateRecentExpensesView();
                    } else {
                        recentExpensesText.setText("Error loading recent expenses");
                    }
                });
    }

    private void updateRecentExpensesView() {
        StringBuilder recentExpensesTextBuilder = new StringBuilder("Recent Expenses:\n");
        for (Expense expense : recentExpensesList) {
            recentExpensesTextBuilder.append(expense.getDescription())
                    .append(" - $")
                    .append(expense.getAmount())
                    .append("\n");
        }
        recentExpensesText.setText(recentExpensesTextBuilder.toString());
    }

    private void fetchTotalExpensesFromSummary() {
        db.collection("Summary").document("TotalExpenses")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Double totalExpenses = documentSnapshot.getDouble("totalExpenses");
                        if (totalExpenses != null) {
                            totalExpensesText.setText("Total Expenses: $" + totalExpenses);
                        } else {
                            totalExpensesText.setText("Total Expenses: $0");
                        }
                    } else {
                        totalExpensesText.setText("No data found");
                    }
                })
                .addOnFailureListener(e -> totalExpensesText.setText("Error loading total expenses"));
    }


}
