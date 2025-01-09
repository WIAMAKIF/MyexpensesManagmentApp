package com.example.myapplicationmanageexpenses;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationmanageexpenses.Expense;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseActivity extends AppCompatActivity {
    private Button addingBtn;
    private EditText description, amount, date;
    private Spinner categorySpinner;
    private ProgressDialog mDialog;
    private FirebaseFirestore db;
    private List<String> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        mDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();

        initializeUI();
        fetchCategories();
    }

    private void initializeUI() {
        description = findViewById(R.id.description);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        categorySpinner = findViewById(R.id.categorySpinner); // Update your layout with a Spinner
        addingBtn = findViewById(R.id.AddingBtn);

        addingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();
            }
        });
    }

    private void fetchCategories() {
        mDialog.setMessage("Loading categories...");
        mDialog.show();

        db.collection("Categories")
                .get()
                .addOnCompleteListener(task -> {
                    mDialog.dismiss();
                    if (task.isSuccessful() && task.getResult() != null) {
                        categoryList.clear();
                        for (var document : task.getResult()) {
                            String category = document.getString("name");
                            if (category != null) {
                                categoryList.add(category);
                            }
                        }

                        if (categoryList.isEmpty()) {
                            categoryList.add("No categories available");
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(getApplicationContext(), "Error loading categories", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addExpense() {
        String desc = description.getText().toString().trim();
        String am = amount.getText().toString().trim();
        String categ = categorySpinner.getSelectedItem().toString();
        String dat = date.getText().toString().trim();

        if (desc.isEmpty() || am.isEmpty() || categ.isEmpty() || dat.isEmpty() || categ.equals("No categories available")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_LONG).show();
            return;
        }

        mDialog.setMessage("Processing...");
        mDialog.show();

        try {
            Double amountValue = Double.parseDouble(am);
            Expense expense = new Expense(desc, amountValue, categ, dat);

            db.collection("Expenses")
                    .add(expense)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Expense added successfully!", Toast.LENGTH_LONG).show();
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Error adding expense", Toast.LENGTH_LONG).show();
                    })
                    .addOnCompleteListener(task -> mDialog.dismiss());
        } catch (NumberFormatException e) {
            mDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Invalid amount format", Toast.LENGTH_LONG).show();
        }
    }

    private void clearFields() {
        description.setText("");
        amount.setText("");
        date.setText("");
        categorySpinner.setSelection(0);
    }
}
