package com.example.myapplicationmanageexpenses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ExpenseDetailActivity extends AppCompatActivity {
    private EditText editTextDescription, editTextAmount, editTextCategory, editTextDate;
    private Button buttonSaveExpense;
    private ProgressDialog progressDialog;

    private FirebaseFirestore db;
    private String expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        // Initialize views
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextCategory = findViewById(R.id.editTextCategory);
        editTextDate = findViewById(R.id.editTextDate);
        buttonSaveExpense = findViewById(R.id.buttonSaveExpense);

        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        // Get expense ID and details from Intent
        Intent intent = getIntent();
        expenseId = intent.getStringExtra("expenseId");
        editTextDescription.setText(intent.getStringExtra("description"));
        editTextAmount.setText(String.valueOf(intent.getDoubleExtra("amount", 0.0)));
        editTextCategory.setText(intent.getStringExtra("category"));
        editTextDate.setText(intent.getStringExtra("date"));

        // Save updated expense
        buttonSaveExpense.setOnClickListener(view -> saveUpdatedExpense());
    }

    private void saveUpdatedExpense() {
        String description = editTextDescription.getText().toString().trim();
        String amountStr = editTextAmount.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();

        if (description.isEmpty() || amountStr.isEmpty() || category.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Updating expense...");
        progressDialog.show();

        Expense updatedExpense = new Expense(description, amount, category, date);

        db.collection("Expenses").document(expenseId)
                .set(updatedExpense)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Expense updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Return to the previous activity
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to update expense", Toast.LENGTH_SHORT).show();
                });
    }
}
