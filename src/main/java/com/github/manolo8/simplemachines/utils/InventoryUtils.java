package com.github.manolo8.simplemachines.utils;

import com.github.manolo8.simplemachines.domain.fuel.Fuel;
import com.github.manolo8.simplemachines.domain.fuel.Fuelling;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientProducer;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientProduct;
import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryUtils {

    public static Fuel getFuel(Fuelling deposit, Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];
            if (stack == null) continue;
            Material material = stack.getType();
            Fuel fuel = deposit.getIfFuel(material);

            if (fuel != null) {
                if (stack.getAmount() == 1) {
                    inventory.clear(i);
                } else {
                    stack.setAmount(stack.getAmount() - 1);
                }
                return fuel;
            }
        }

        return null;
    }

    public static boolean isFull(Inventory inventory, Material material) {
        if (inventory == null) return false;

        ItemStack is = new ItemStack(material);
        int stackSize = is.getMaxStackSize();

        ItemStack[] contents = inventory.getContents();
        for (ItemStack stack : contents) {
            if (stack == null) return false;
            else if (stack.isSimilar(is) && (stackSize - stack.getAmount()) > 0) return false;
        }

        return true;
    }

    public static int giveItem(Inventory inventory, Material material, int amount) {
        ItemStack is = new ItemStack(material);
        int stackSize = is.getMaxStackSize();

        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack stack = contents[i];

            if (stack == null) {
                if (amount > stackSize) {
                    inventory.setItem(i, new ItemStack(material, stackSize));
                    amount -= stackSize;
                    continue;
                }
                inventory.setItem(i, new ItemStack(material, amount));
                return 0;
            }

            int stackAmount = stack.getAmount();
            int free = stackSize - stackAmount;

            if (stack.isSimilar(is) && free > 0) {
                if (amount > free) {
                    stack.setAmount(stackAmount + free);
                    amount -= free;
                    continue;
                }
                stack.setAmount(stackAmount + amount);
                return 0;
            }
        }
        return amount;
    }

    public static boolean hasItems(Inventory inventory, List<ItemStack> items) {
        int[] quantity = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            ItemStack loop = items.get(i);
            quantity[i] = loop.getAmount();
        }

        if (inventory == null) return false;

        ItemStack[] contents = inventory.getContents();
        for (ItemStack stack : contents) {
            if (stack == null) continue;

            for (int i = 0; i < items.size(); i++) {
                ItemStack loop = items.get(i);
                if (!stack.isSimilar(loop)) continue;
                if (quantity[i] == 0) continue;
                int haveAmount = stack.getAmount();
                int amount = loop.getAmount();
                if (haveAmount > amount) {
                    quantity[i] = 0;
                } else {
                    quantity[i] = amount - haveAmount;
                }
            }
        }

        for (int i : quantity) if (i > 0) return false;

        return true;
    }

    public static void removeItems(Inventory inventory, List<ItemStack> items) {
        int[] quantity = new int[items.size()];
        for (int i = 0; i < items.size(); i++) {
            ItemStack loop = items.get(i);
            quantity[i] = loop.getAmount();
        }

        if (inventory == null) return;

        ItemStack[] contents = inventory.getContents();
        for (int a = 0; a < contents.length; a++) {
            ItemStack stack = contents[a];
            if (stack == null) continue;

            for (int i = 0; i < items.size(); i++) {
                ItemStack loop = items.get(i);
                if (!stack.isSimilar(loop)) continue;
                if (quantity[i] == 0) continue;
                int haveAmount = stack.getAmount();
                int amount = loop.getAmount();
                if (haveAmount > amount) {
                    quantity[i] = 0;
                    stack.setAmount(haveAmount - amount);
                } else {
                    inventory.clear(a);
                    quantity[i] = amount - haveAmount;
                }
            }
        }
    }

    public static IngredientProduct scanProduct(IngredientProducer producer, Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        for (ItemStack stack : contents) {
            if (stack == null) continue;
            IngredientProduct product = producer.isIngredient(stack.getType());
            if (product != null) return product;
        }

        return null;
    }

    public static boolean removeItem(Inventory inventory, Material ingredient) {
        ItemStack[] contents = inventory.getContents();
        ItemStack material = new ItemStack(ingredient);

        for (int a = 0; a < contents.length; a++) {
            ItemStack stack = contents[a];
            if (stack == null) continue;

            if (!stack.isSimilar(material)) continue;

            if (stack.getAmount() > 1) stack.setAmount(stack.getAmount() - 1);
            else inventory.clear(a);
            return true;
        }
        return false;
    }
}
