package com.github.manolo8.simplemachines.domain.solar;

import java.util.List;

public class SolarFuelling {

    private List<SolarFuel> fuels;

    public List<SolarFuel> getFuels() {
        return fuels;
    }

    public void setFuels(List<SolarFuel> fuels) {
        this.fuels = fuels;
    }

    public SolarFuel getFuel(long time) {
        TimeType type = TimeType.getByTime(time);

        for(SolarFuel fuel : fuels) if(fuel.getTimeType() == type) return fuel;

        return null;
    }
}
