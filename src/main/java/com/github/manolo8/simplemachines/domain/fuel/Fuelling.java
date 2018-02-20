package com.github.manolo8.simplemachines.domain.fuel;

import org.bukkit.Material;

import java.util.List;

public class Fuelling {

    private List<Fuel> fuels;

    public List<Fuel> getFuels() {
        return fuels;
    }

    public void setFuels(List<Fuel> fuels) {
        this.fuels = fuels;
    }

    public Fuel getIfFuel(Material material) {
        for (Fuel fuel : fuels) {
            if (fuel.getMaterial().equals(material)) return fuel;
        }
        return null;
    }
}
