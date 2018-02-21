package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.utils.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Random;

public class IngredientProducer extends com.github.manolo8.simplemachines.model.Producer<IngredientProduct> {

    public IngredientProducer(Random random) {
        super(random);
    }

    public IngredientProduct isIngredient(Material material) {
        for (IngredientProduct product : getProducts()) {
            if (product.getIngredient() == material) return product;
        }
        return null;
    }

    public IngredientProduct getNextProduct(Inventory inventory) {
        return InventoryUtils.scanProduct(this, inventory);
    }
}
