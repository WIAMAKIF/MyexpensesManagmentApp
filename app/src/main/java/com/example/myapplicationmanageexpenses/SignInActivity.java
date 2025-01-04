package com.example.myapplicationmanageexpenses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private EditText nameField;
    private EditText signUpEmailField;
    private EditText signUpPasswordField;
    private Button signUpButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        nameField = findViewById(R.id.nameField);
        signUpEmailField = findViewById(R.id.signUpEmailField);
        signUpPasswordField = findViewById(R.id.signUpPasswordField);
        signUpButton = findViewById(R.id.signUpButton);

        auth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(view -> {
            String name = nameField.getText().toString().trim();
            String email = signUpEmailField.getText().toString().trim();
            String password = signUpPasswordField.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                // Navigate to WelcomeScreen
                                Intent intent = new Intent(SignInActivity.this, WelcomeScreen.class);
                                startActivity(intent);
                                finish(); // Close the SignInActivity
                            } else {
                                Toast.makeText(SignInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
