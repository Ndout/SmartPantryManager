package com.example.smartpantrymanager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        // Enable top action bar back arrow (←)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add Pantry Ingredient");
        }

        db = AppDatabase.getInstance(this);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button btnSave = findViewById(R.id.btnSave);

        String[] units = {"pcs", "g", "kg", "ml", "l", "clove", "tbsp", "tsp"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnit.setAdapter(adapter);

        // Keep the save button operational
        btnSave.setOnClickListener(v -> saveIngredient());
    }

    // Handles the top action bar back arrow tap
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Ingredient name is required");
            return;
        }

        if (TextUtils.isEmpty(qtyStr)) {
            etQuantity.setError("Quantity is required");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            return;
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            PantryItem item = new PantryItem(name, quantity, unit, expiry);
            db.pantryDao().insert(item);
            runOnUiThread(() -> {
                Toast.makeText(this, "Ingredient saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}