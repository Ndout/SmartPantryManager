package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.concurrent.Executors;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Enable top action bar back arrow (←)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Recipe Details");
        }

        TextView tvTitle = findViewById(R.id.tvRecipeTitle);
        TextView tvIngredients = findViewById(R.id.tvIngredientsList);
        TextView tvInstructions = findViewById(R.id.tvInstructions);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            finish();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            Recipe recipe = null;
            List<Recipe> all = db.recipeDao().getAllRecipesSync();
            for (Recipe r : all) {
                if (r.getId() == recipeId) {
                    recipe = r;
                    break;
                }
            }

            if (recipe != null) {
                List<RecipeIngredient> ingredients = db.recipeDao().getIngredientsForRecipeSync(recipeId);
                StringBuilder sb = new StringBuilder();
                if (ingredients != null) {
                    for (RecipeIngredient ing : ingredients) {
                        sb.append("• ").append(ing.getIngredientName())
                                .append(" (").append(ing.getRequiredQuantity())
                                .append(" ").append(ing.getUnit()).append(")\n");
                    }
                }

                Recipe finalRecipe = recipe;
                String ingredientsText = sb.toString().trim();

                // Safely update UI on the main thread with null checks
                runOnUiThread(() -> {
                    if (tvTitle != null) {
                        tvTitle.setText(finalRecipe.getName());
                    }
                    if (tvInstructions != null) {
                        tvInstructions.setText(finalRecipe.getInstructions());
                    }
                    if (tvIngredients != null) {
                        tvIngredients.setText(ingredientsText);
                    }
                });
            }
        });
    }

    // Handles the top action bar back arrow tap
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
