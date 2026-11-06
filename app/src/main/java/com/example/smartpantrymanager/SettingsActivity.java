package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity {

    private AppDatabase db;
    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_ALERTS = "expiry_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        db = AppDatabase.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Bind Views
        SwitchMaterial switchDarkMode = findViewById(R.id.switchDarkMode);
        SwitchMaterial switchNotifications = findViewById(R.id.switchNotifications);
        Button btnClearData = findViewById(R.id.btnClearData);

        // Load saved toggle states
        boolean isDarkMode = preferences.getBoolean(KEY_DARK_MODE, false);
        boolean isAlertsEnabled = preferences.getBoolean(KEY_ALERTS, true);

        switchDarkMode.setChecked(isDarkMode);
        switchNotifications.setChecked(isAlertsEnabled);

        // 1. Functional Dark Mode Switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(KEY_DARK_MODE, isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // 2. Persistent Expiry Alerts Switch
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(KEY_ALERTS, isChecked).apply();
            Toast.makeText(this, isChecked ? "Expiry Alerts Enabled" : "Expiry Alerts Disabled", Toast.LENGTH_SHORT).show();
        });

        // 3. Clear Data Button
        btnClearData.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {
                db.clearAllTables();
            });
            Toast.makeText(this, "Pantry Cleared", Toast.LENGTH_SHORT).show();
        });

        // Bottom Navigation Setup
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.nav_settings);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_recipes) {
                startActivity(new Intent(SettingsActivity.this, SuggestedRecipesActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_settings) {
                return true;
            }
            return false;
        });
    }
}