package com.example.myapplicationmanageexpenses;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Button manageCategoriesButton;
    private Button managePreferencesButton;
    private Button changeThemeButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        manageCategoriesButton = findViewById(R.id.buttonManageCategories);
        managePreferencesButton = findViewById(R.id.buttonManagePreferences);
        changeThemeButton = findViewById(R.id.buttonChangeTheme);
        logoutButton = findViewById(R.id.buttonLogoutSettings);

        manageCategoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Category Management
            }
        });

        managePreferencesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manage User Preferences
            }
        });

        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change App Theme
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle Logout
            }
        });
    }
}
