package com.example.myapplicationmanageexpenses;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> categoryList;

    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.textViewCategoryName.setText(category.getName());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();


        // Edit button logic
        holder.buttonEditCategory.setOnClickListener(v -> {
            Context context = v.getContext();
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null);
            EditText editTextName = dialogView.findViewById(R.id.editTextEditCategoryName);
            EditText editTextDescription = dialogView.findViewById(R.id.editTextEditCategoryDescription);

            editTextName.setText(category.getName());
            editTextDescription.setText(category.getDescription());

            new AlertDialog.Builder(context)
                    .setTitle("Edit Category")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String updatedName = editTextName.getText().toString().trim();
                        String updatedDescription = editTextDescription.getText().toString().trim();

                        if (!updatedName.isEmpty() && !updatedDescription.isEmpty()) {
                            firestore.collection("Categories")
                                    .document(category.getId())
                                    .update("name", updatedName, "description", updatedDescription)
                                    .addOnSuccessListener(aVoid -> {
                                        category.setName(updatedName);
                                        category.setDescription(updatedDescription);
                                        notifyItemChanged(position);
                                        Toast.makeText(context, "Category updated", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Error updating category", Toast.LENGTH_SHORT).show()
                                    );
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        // Delete button logic
        holder.buttonDeleteCategory.setOnClickListener(v -> {
            // Logic for deleting a category
            firestore.collection("Categories")
                    .document(category.getId()) // Ensure Category has a unique ID
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        categoryList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "Category deleted", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(v.getContext(), "Error deleting category", Toast.LENGTH_SHORT).show()
                    );
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName, textViewCategoryDescription;
        Button buttonEditCategory, buttonDeleteCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.CategoryName);
            buttonEditCategory = itemView.findViewById(R.id.editCategoryButton);
            buttonDeleteCategory = itemView.findViewById(R.id.deleteCategoryButton);
        }
    }
}
