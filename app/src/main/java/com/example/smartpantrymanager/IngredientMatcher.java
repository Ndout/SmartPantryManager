package com.example.smartpantrymanager;

import java.util.List;

public class IngredientMatcher {

    public static String normalizeName(String raw) {
        if (raw == null) return "";
        String clean = raw.trim().toLowerCase();

        // Plural normalization (Section 2.3 requirement)
        if (clean.endsWith("es") && clean.length() > 3) {
            clean = clean.substring(0, clean.length() - 2);
        } else if (clean.endsWith("s") && !clean.endsWith("ss") && clean.length() > 3) {
            clean = clean.substring(0, clean.length() - 1);
        }
        return clean;
    }

    public static boolean canMakeRecipe(List<RecipeIngredient> requiredIngredients, List<PantryItem> pantryItems) {
        for (RecipeIngredient req : requiredIngredients) {
            boolean matched = false;
            String reqName = normalizeName(req.getIngredientName());

            for (PantryItem pItem : pantryItems) {
                String pantryName = normalizeName(pItem.getName());

                if (reqName.equals(pantryName)) {
                    double convertedQty = convertUnit(pItem.getQuantity(), pItem.getUnit(), req.getUnit());
                    if (convertedQty >= req.getRequiredQuantity()) {
                        matched = true;
                        break;
                    }
                }
            }

            // Strict rule: If a single ingredient is missing or insufficient, reject recipe
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    private static double convertUnit(double qty, String fromUnit, String toUnit) {
        if (fromUnit == null || toUnit == null || fromUnit.equalsIgnoreCase(toUnit)) {
            return qty;
        }
        if (fromUnit.equalsIgnoreCase("kg") && toUnit.equalsIgnoreCase("g")) return qty * 1000;
        if (fromUnit.equalsIgnoreCase("g") && toUnit.equalsIgnoreCase("kg")) return qty / 1000;
        if (fromUnit.equalsIgnoreCase("l") && toUnit.equalsIgnoreCase("ml")) return qty * 1000;
        if (fromUnit.equalsIgnoreCase("ml") && toUnit.equalsIgnoreCase("l")) return qty / 1000;

        return qty;
    }
}