package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private AppDatabase db;
    private RecipeAdapter adapter;
    private LinearLayout layoutEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        db = AppDatabase.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.rvSuggestedRecipes);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(new ArrayList<>(), recipe -> {
            // Intent to open details activity (built in next step)
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMatchingRecipes();
    }

    private void loadMatchingRecipes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<PantryItem> pantryItems = db.pantryDao().getAllItemsSync();
            List<Recipe> allRecipes = db.recipeDao().getAllRecipesSync();
            List<Recipe> matchedRecipes = new ArrayList<>();

            for (Recipe recipe : allRecipes) {
                List<RecipeIngredient> reqIngredients = db.recipeDao().getIngredientsForRecipeSync(recipe.getId());
                if (IngredientMatcher.canMakeRecipe(reqIngredients, pantryItems)) {
                    matchedRecipes.add(recipe);
                }
            }

            runOnUiThread(() -> {
                adapter.setRecipes(matchedRecipes);
                if (matchedRecipes.isEmpty()) {
                    layoutEmptyState.setVisibility(View.VISIBLE);
                } else {
                    layoutEmptyState.setVisibility(View.GONE);
                }
            });
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setSelectedItemId(R.id.nav_recipes);
        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_pantry) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_recipes) {
                return true;
            }
            return false;
        });
    }
}
