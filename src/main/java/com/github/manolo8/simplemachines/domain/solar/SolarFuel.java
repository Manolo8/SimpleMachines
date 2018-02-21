package com.github.manolo8.simplemachines.domain.solar;

public class SolarFuel {

    private TimeType timeType;
    private double quantity;

    public SolarFuel(TimeType timeType, double quantity) {
        this.timeType = timeType;
        this.quantity = quantity;
    }

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
