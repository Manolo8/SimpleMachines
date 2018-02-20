package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.domain.fuel.FuelMachine;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class IngredientMachine extends FuelMachine<IngredientProduct, IngredientProducer> {

    protected SimpleLocation ingredientDeposit;

    //===============METHODS===============

    //=============OVERRIDE=============
    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        IngredientDesign design = (IngredientDesign) bluePrint.getDesign();
        ingredientDeposit = design.getIngredientDepositLoc(face, base);
        return ingredientDeposit.getBlock(world).getType() == Material.CHEST;
    }

    @Override
    public void searchNextProduct() {
        Inventory inventory = getInventory(ingredientDeposit);

        if (inventory == null) {
            setWorking(false);
            return;
        }

        current = producer.getNextProduct(inventory);

        if (current == null) setWorking(false);
    }

    @Override
    public boolean canProduce() {
        if (!super.canProduce()) return false;
        Inventory inventory = getInventory(ingredientDeposit);
        boolean hasItem = InventoryUtils.removeItem(inventory, current.getIngredient());

        if (!hasItem) {
            current = null;
            setWorking(false);
        }

        return ingredientDeposit != null && hasItem;
    }
    //=============OVERRIDE=============

    //===============METHODS===============
}
