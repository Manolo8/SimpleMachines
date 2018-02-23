package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.model.Producer;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class IngredientProducer extends Producer<IngredientProduct> {

    public IngredientProducer(Random random) {
        super(random);
    }

    public IngredientProduct isIngredient(ItemStack itemStack) {
        for (IngredientProduct product : getProducts()) {
            if (product.getIngredient().isSimilar(itemStack)) return product;
        }
        return null;
    }

    public IngredientProduct getNextProduct(Inventory inventory) {
        return InventoryUtils.scanProduct(this, inventory);
    }
}
