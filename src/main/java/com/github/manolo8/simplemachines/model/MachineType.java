package com.github.manolo8.simplemachines.model;

import com.github.manolo8.simplemachines.domain.fuel.FuelBluePrint;
import com.github.manolo8.simplemachines.domain.fuel.FuelDesign;
import com.github.manolo8.simplemachines.domain.fuel.FuelMachine;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientDesign;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientMachine;
import com.github.manolo8.simplemachines.exception.DataBaseException;

public enum MachineType {
    FUEL("fuel"), INGREDIENT("ingredient");

    private String name;

    MachineType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Machine getMachine() throws DataBaseException {
        switch (name) {
            case "fuel":
                return new FuelMachine();
            case "ingredient":
                return new IngredientMachine();
            default:
                throw new DataBaseException("O tipo de máquina " + name + " não foi encontrado");
        }
    }

    public Design getMachineDesign() throws DataBaseException {
        switch (name) {
            case "fuel":
                return new FuelDesign();
            case "ingredient":
                return new IngredientDesign();
            default:
                throw new DataBaseException("O tipo de máquina " + name + " não foi encontrado");
        }
    }

    public BluePrint getMachineBluePrint() throws DataBaseException {
        switch (name) {
            case "fuel":
                return new FuelBluePrint();
            case "ingredient":
                return new FuelBluePrint();
            default:
                throw new DataBaseException("O tipo de máquina " + name + " não foi encontrado");
        }
    }

    public static MachineType getByName(String name) {
        return valueOf(name.toUpperCase());
    }
}
