package com.example.smartpantrymanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {PantryItem.class, Recipe.class, RecipeIngredient.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PantryDao pantryDao();
    public abstract RecipeDao recipeDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "smart_pantry_db"
                            )
                            .addCallback(seedCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback seedCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase database = INSTANCE;
                if (database == null) return;

                RecipeDao dao = database.recipeDao();

                // 1. Tomato Omelette
                long id = dao.insertRecipe(new Recipe("Tomato Omelette", "1. Whisk eggs.\n2. Chop tomato.\n3. Cook in pan with butter."));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "tomato", 1, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 10, "g"));

                // 2. Fried Rice
                id = dao.insertRecipe(new Recipe("Fried Rice", "1. Heat oil in wok.\n2. Add cooked rice and soy sauce.\n3. Stir in egg."));
                dao.insertIngredient(new RecipeIngredient((int)id, "rice", 200, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 1, "pcs"));

                // 3. Garlic Pasta
                id = dao.insertRecipe(new Recipe("Garlic Pasta", "1. Boil pasta.\n2. Sauté garlic in olive oil.\n3. Toss pasta with garlic oil."));
                dao.insertIngredient(new RecipeIngredient((int)id, "pasta", 200, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "garlic", 2, "clove"));

                // 4. Scrambled Eggs
                id = dao.insertRecipe(new Recipe("Scrambled Eggs", "1. Beat eggs with milk.\n2. Melt butter in pan.\n3. Cook gently on low heat."));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 3, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "milk", 50, "ml"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 10, "g"));

                // 5. Boiled Eggs
                id = dao.insertRecipe(new Recipe("Boiled Eggs", "1. Place eggs in boiling water.\n2. Cook for 7 minutes for soft or 10 for hard.\n3. Cool in ice water."));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 2, "pcs"));

                // 6. Tomato Soup
                id = dao.insertRecipe(new Recipe("Tomato Soup", "1. Chop tomatoes and garlic.\n2. Simmer with butter.\n3. Blend until smooth."));
                dao.insertIngredient(new RecipeIngredient((int)id, "tomato", 4, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "garlic", 1, "clove"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 20, "g"));

                // 7. Rice Porridge
                id = dao.insertRecipe(new Recipe("Rice Porridge", "1. Boil rice in milk on low heat.\n2. Stir regularly until creamy."));
                dao.insertIngredient(new RecipeIngredient((int)id, "rice", 100, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "milk", 300, "ml"));

                // 8. Butter Toast
                id = dao.insertRecipe(new Recipe("Butter Toast", "1. Toast bread slices.\n2. Spread butter evenly."));
                dao.insertIngredient(new RecipeIngredient((int)id, "bread", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 15, "g"));

                // 9. Garlic Bread
                id = dao.insertRecipe(new Recipe("Garlic Bread", "1. Mix minced garlic with butter.\n2. Spread on bread and bake."));
                dao.insertIngredient(new RecipeIngredient((int)id, "bread", 4, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 30, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "garlic", 2, "clove"));

                // 10. French Toast
                id = dao.insertRecipe(new Recipe("French Toast", "1. Whisk egg and milk.\n2. Dip bread.\n3. Fry in butter until golden."));
                dao.insertIngredient(new RecipeIngredient((int)id, "bread", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 1, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "milk", 50, "ml"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 10, "g"));

                // 11. Egg Salad
                id = dao.insertRecipe(new Recipe("Egg Salad", "1. Chop boiled eggs.\n2. Dice tomato.\n3. Mix together softly."));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "tomato", 1, "pcs"));

                // 12. Tomato Rice
                id = dao.insertRecipe(new Recipe("Tomato Rice", "1. Sauté garlic and chopped tomato.\n2. Stir in rice and cook."));
                dao.insertIngredient(new RecipeIngredient((int)id, "rice", 150, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "tomato", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "garlic", 1, "clove"));

                // 13. Cheesy Pasta
                id = dao.insertRecipe(new Recipe("Cheesy Pasta", "1. Boil pasta.\n2. Melt butter and cheese over pasta."));
                dao.insertIngredient(new RecipeIngredient((int)id, "pasta", 200, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "cheese", 50, "g"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 15, "g"));

                // 14. Cheese Toast
                id = dao.insertRecipe(new Recipe("Cheese Toast", "1. Place cheese on bread.\n2. Toast until cheese melts."));
                dao.insertIngredient(new RecipeIngredient((int)id, "bread", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "cheese", 40, "g"));

                // 15. Simple Omelette
                id = dao.insertRecipe(new Recipe("Simple Omelette", "1. Whisk eggs.\n2. Fry in butter until firm."));
                dao.insertIngredient(new RecipeIngredient((int)id, "egg", 2, "pcs"));
                dao.insertIngredient(new RecipeIngredient((int)id, "butter", 10, "g"));
            });
        }
    };
}
