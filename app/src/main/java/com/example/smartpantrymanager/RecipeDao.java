package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insertRecipe(Recipe recipe);

    @Insert
    void insertIngredient(RecipeIngredient ingredient);

    @Query("SELECT * FROM recipes")
    List<Recipe> getAllRecipesSync();

    @Query("SELECT * FROM recipe_ingredients WHERE recipeId = :recipeId")
    List<RecipeIngredient> getIngredientsForRecipeSync(int recipeId);
}
