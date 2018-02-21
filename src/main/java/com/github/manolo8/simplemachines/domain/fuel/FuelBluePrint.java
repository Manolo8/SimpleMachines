package com.github.manolo8.simplemachines.domain.fuel;

import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.Machine;

public class FuelBluePrint extends BluePrint {

    protected Fuelling fuelling;

    public Fuelling getFuelling() {
        return fuelling;
    }

    public void setFuelling(Fuelling fuelling) {
        this.fuelling = fuelling;
    }

    public Machine newInstance() {
        FuelMachine fuelMachine = new FuelMachine();
        fuelMachine.setBluePrint(this);
        return fuelMachine;
    }
}
