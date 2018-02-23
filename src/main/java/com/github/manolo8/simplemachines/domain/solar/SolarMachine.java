package com.github.manolo8.simplemachines.domain.solar;

import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.model.Producer;
import com.github.manolo8.simplemachines.model.Product;

public class SolarMachine<F extends Product, T extends Producer<F>> extends Machine<F, T> {

    private SolarFuel currentFuel;

    //===============METHODS===============

    public void findFuel() {
        currentFuel = ((SolarBluePrint) bluePrint).getFuelling().getFuel(world.getTime());
    }

    public void generateFuel(long amount) {
        available += currentFuel.getQuantity() * amount;
    }

    //=============OVERRIDE=============
    @Override
    public void tick(long amount) {
        findFuel();
        if (currentFuel != null) generateFuel(amount);
        if (canProduce()) productMade(current);
    }
    //=============OVERRIDE=============

    //===============METHODS===============
}
