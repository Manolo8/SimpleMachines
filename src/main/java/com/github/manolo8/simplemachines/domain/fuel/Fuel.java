package com.github.manolo8.simplemachines.domain.fuel;

import org.bukkit.inventory.ItemStack;

public class Fuel {

    private ItemStack itemStack;
    private double burnTime;
    private double speed;

    public Fuel(ItemStack itemStack, double burnTime, double speed) {
        this.itemStack = itemStack;
        this.burnTime = burnTime;
        this.speed = speed;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getBurnTime() {
        return burnTime;
    }

    public double getSpeed() {
        return speed;
    }
}