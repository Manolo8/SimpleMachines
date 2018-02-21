package com.github.manolo8.simplemachines.domain.solar;

public enum TimeType {
    DAY,NIGHT;

    public static TimeType getByTime(long time) {
        if(time < 12300 || time > 23850) return DAY;
        return NIGHT;
    }
}
