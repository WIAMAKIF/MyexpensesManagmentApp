package com.example.myapplicationmanageexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    private Button buttonDashboard, buttonExpenses, buttonCategories, buttonAddExpense, buttonSettings, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Initialize buttons
        buttonDashboard = findViewById(R.id.buttonDashboard);
        buttonExpenses = findViewById(R.id.buttonExpenses);
        buttonCategories = findViewById(R.id.buttonCategories);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);

        buttonLogout = findViewById(R.id.buttonLogout);

        // Set OnClickListeners for navigation buttons
        buttonDashboard.setOnClickListener(view -> startActivity(new Intent(WelcomeScreen.this, DashboardActivity.class)));
        buttonExpenses.setOnClickListener(view -> startActivity(new Intent(WelcomeScreen.this, ExpenseActivity.class)));
        buttonCategories.setOnClickListener(view -> startActivity(new Intent(WelcomeScreen.this, CategoryManagementActivity.class)));
        buttonAddExpense.setOnClickListener(view -> startActivity(new Intent(WelcomeScreen.this, AddExpenseActivity.class)));

        buttonLogout.setOnClickListener(view -> logout());
    }

    private void logout() {
        // Perform logout actions
        // E.g., clearing user session or tokens, navigating to Login screen
        Intent intent = new Intent( WelcomeScreen.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Finish current activity
    }
}
