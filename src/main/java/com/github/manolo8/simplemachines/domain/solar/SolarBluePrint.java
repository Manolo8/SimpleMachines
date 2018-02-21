package com.github.manolo8.simplemachines.domain.solar;

import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.Machine;

public class SolarBluePrint extends BluePrint {

    protected SolarFuelling fuelling;

    public SolarFuelling getFuelling() {
        return fuelling;
    }

    public void setFuelling(SolarFuelling fuelling) {
        this.fuelling = fuelling;
    }

    @Override
    public Machine newInstance() {
        SolarMachine solarMachine = new SolarMachine();
        solarMachine.setBluePrint(this);
        return solarMachine;
    }

    @Override
    public String getSuper() {
        return "solar";
    }
}
