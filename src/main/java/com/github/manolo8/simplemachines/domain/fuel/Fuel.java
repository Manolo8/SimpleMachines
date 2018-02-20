package com.github.manolo8.simplemachines.domain.fuel;

import org.bukkit.Material;

public class Fuel {

    private Material material;
    private double burnTime;
    private double speed;

    public Fuel(Material material, double burnTime, double speed) {
        this.material = material;
        this.burnTime = burnTime;
        this.speed = speed;
    }

    public Material getMaterial() {
        return material;
    }

    public double getBurnTime() {
        return burnTime;
    }

    public double getSpeed() {
        return speed;
    }
}