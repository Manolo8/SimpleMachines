package com.github.manolo8.simplemachines.domain.collector;

import com.github.manolo8.simplemachines.domain.fuel.FuelMachine;
import com.github.manolo8.simplemachines.utils.BlockScanner;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CollectorMachine extends FuelMachine<CollectorProduct, CollectorProducer> {

    private BlockScanner blockScanner;
    private int xzRange;
    private int yRange;

    //===============METHODS===============
    public boolean isInArea(Block block) {
        return world.equals(block.getWorld()) && base.isInArea(block.getX(), block.getY(), block.getZ(), xzRange, yRange);
    }

    public void createBlockScanner() {
        CollectorBluePrint bluePrint = (CollectorBluePrint) getBluePrint();
        xzRange = bluePrint.getRange();
        yRange = bluePrint.getRangeY();
        blockScanner = new BlockScanner(world, base, xzRange, yRange);
    }

    public void runScanner(long amount) {
        for (int i = 0; i < amount * 25; i++) {
            if (!blockScanner.hasNext()) {
                blockScanner = null;
                break;
            }
            Block block = blockScanner.next();
            checkBlock(block, block.getState());
        }
    }

    public void checkBlock(Block block, BlockState newState) {
        CollectorProduct product = getProducer().getIfCollectible(newState);

        if (product == null) return;
        if (!product.isReady(block, newState)) return;

        toInventory(product.breakAndGetDrops(block, newState, random));
    }

    public void toInventory(ItemStack[] items) {
        Inventory inventory = getInventory(deposit);

        if (inventory == null) return;

        for (ItemStack stack : items) {
            int overflow = InventoryUtils.giveItem(inventory, stack, stack.getAmount());

            if (overflow != 0) {
                setFull(true);
                setWorking(false);
                return;
            }
        }

        setFull(false);
    }

    //=============OVERRIDE=============
    @Override
    public boolean checkStage() {
        return !noFuel && !full;
    }

    @Override
    public void searchNextProduct() {
    }

    @Override
    public void canWork() {
        if (full) checkDeposit();
        if (noFuel) burnFuel();
        setWorking(checkStage());
        if (working) createBlockScanner();
    }

    @Override
    public void consume(long amount) {
        if ((burningTime - amount) < 0) {
            burningTime = 0;
            speed = 0;
            return;
        }

        burningTime -= amount;
    }

    @Override
    public void tick(long amount) {
        if (blockScanner != null) runScanner(amount);
        if (burningTime != 0) consume(amount);
        if (burningTime == 0) burnFuel();
    }
    //=============OVERRIDE=============

    //===============METHODS===============
}
