package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IngredientProduct extends Product {

    private ItemStack ingredient;

    public ItemStack getIngredient() {
        return ingredient;
    }

    public void setIngredient(ItemStack ingredient) {
        this.ingredient = ingredient;
    }
}
