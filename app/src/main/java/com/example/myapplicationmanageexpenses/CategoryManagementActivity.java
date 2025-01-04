package com.example.myapplicationmanageexpenses;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CategoryManagementActivity extends AppCompatActivity {

    private EditText editTextCategoryName, editTextCategoryDescription;
    private Button buttonAddCategory;
    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_management);

        firestore = FirebaseFirestore.getInstance();

        editTextCategoryName = findViewById(R.id.editTextCategoryName);
        editTextCategoryDescription = findViewById(R.id.editTextCategoryDescription);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategories.setAdapter(categoryAdapter);

        fetchCategories();

        buttonAddCategory.setOnClickListener(view -> {
            String name = editTextCategoryName.getText().toString().trim();
            String description = editTextCategoryDescription.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Category newCategory = new Category(name, description);
            firestore.collection("Categories")
                    .add(newCategory)
                    .addOnSuccessListener(documentReference -> {
                        editTextCategoryName.setText("");
                        editTextCategoryDescription.setText("");
                        fetchCategories();
                        Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error adding category", Toast.LENGTH_SHORT).show());
        });
    }

    private void fetchCategories() {
        firestore.collection("Categories")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        categoryList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Category category = document.toObject(Category.class);
                            categoryList.add(category);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }
                });
    }
}
