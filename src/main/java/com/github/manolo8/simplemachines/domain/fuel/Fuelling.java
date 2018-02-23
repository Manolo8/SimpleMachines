package com.github.manolo8.simplemachines.domain.fuel;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Fuelling {

    private List<Fuel> fuels;

    public List<Fuel> getFuels() {
        return fuels;
    }

    public void setFuels(List<Fuel> fuels) {
        this.fuels = fuels;
    }

    public Fuel getIfFuel(ItemStack itemStack) {
        for (Fuel fuel : fuels) {
            if (fuel.getItemStack().isSimilar(itemStack)) return fuel;
        }
        return null;
    }
}
