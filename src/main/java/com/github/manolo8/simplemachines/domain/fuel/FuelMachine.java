package com.github.manolo8.simplemachines.domain.fuel;

import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.model.Producer;
import com.github.manolo8.simplemachines.model.Product;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class FuelMachine<F extends Product, T extends Producer<F>> extends Machine<F, T> {

    protected SimpleLocation fuelDeposit;
    protected boolean noFuel;
    protected double burningTime;
    protected double speed;

    //============ENCAPSULATION============
    public SimpleLocation getFuelDeposit() {
        return fuelDeposit;
    }

    public void setFuelDeposit(SimpleLocation fuelDeposit) {
        this.fuelDeposit = fuelDeposit;
    }

    public double getBurningTime() {
        return burningTime;
    }

    public void setBurningTime(double burningTime) {
        this.burningTime = burningTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isNoFuel() {
        return noFuel;
    }

    public void setNoFuel(boolean noFuel) {
        this.noFuel = noFuel;
    }

    //============ENCAPSULATION============

    //===============METHODS===============
    protected void burnFuel() {
        Inventory inventory = getInventory(fuelDeposit);

        if (inventory == null) {
            return;
        }

        Fuel fuel = InventoryUtils.getFuel(((FuelBluePrint) bluePrint).getFuelling(), inventory);

        if (fuel == null) {
            setNoFuel(true);
            setWorking(false);
            return;
        }

        setNoFuel(false);
        speed = fuel.getSpeed();
        burningTime = fuel.getBurnTime();
    }

    public void consume(long amount) {
        double quantity = ((double) amount) * speed;

        if ((burningTime - quantity) < 0) {
            available += burningTime;
            burningTime = 0;
            speed = 0;
            return;
        }

        burningTime -= quantity;
        available += quantity;
    }

    //=============OVERRIDE=============
    @Override
    public boolean checkStage() {
        return super.checkStage() && !noFuel;
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        FuelDesign design = (FuelDesign) bluePrint.getDesign();
        fuelDeposit = design.getFuelDepositLoc(face, base);

        return fuelDeposit.getBlock(world).getType() == Material.CHEST;
    }

    @Override
    public void canWork() {
        if (noFuel) burnFuel();
        super.canWork();
    }

    @Override
    public void tick(long amount) {
        if (burningTime == 0) burnFuel();
        if (burningTime != 0) consume(amount);
        if (canProduce()) productMade(current);
    }
    //=============OVERRIDE=============

    //===============METHODS===============
}
