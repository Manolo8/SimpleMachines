package com.github.manolo8.simplemachines.domain.collector;

import com.github.manolo8.simplemachines.domain.fuel.FuelBluePrint;
import com.github.manolo8.simplemachines.model.Machine;

public class CollectorBluePrint extends FuelBluePrint {

    private int range;
    private int rangeY;

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getRangeY() {
        return rangeY;
    }

    public void setRangeY(int rangeY) {
        this.rangeY = rangeY;
    }

    @Override
    public Machine newInstance() {
        CollectorMachine machine = new CollectorMachine();
        machine.setBluePrint(this);
        return machine;
    }

    @Override
    public String getSuper() {
        return "collector";
    }
}
