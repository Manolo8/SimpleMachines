package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.Material;

public class IngredientProduct extends Product {

    private Material ingredient;

    public Material getIngredient() {
        return ingredient;
    }

    public void setIngredient(Material ingredient) {
        this.ingredient = ingredient;
    }
}
