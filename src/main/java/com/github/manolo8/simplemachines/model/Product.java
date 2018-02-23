package com.github.manolo8.simplemachines.model;

import org.bukkit.inventory.ItemStack;

public class Product {

    protected ItemStack itemStack;
    protected int quantity;
    protected double cost;

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public double getPerQuantity() {
        return cost / (double) quantity;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
