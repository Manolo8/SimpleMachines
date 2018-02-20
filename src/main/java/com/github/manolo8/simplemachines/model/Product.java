package com.github.manolo8.simplemachines.model;

import org.bukkit.Material;

public class Product {

    protected Material material;
    protected int quantity;
    protected double cost;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
