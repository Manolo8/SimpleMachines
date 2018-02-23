package com.github.manolo8.simplemachines.model;

import com.github.manolo8.simplemachines.utils.InventoryUtils;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.Random;
import java.util.UUID;

public abstract class Machine<T extends Product, P extends Producer<T>> {

    protected Random random;
    protected UUID uuid;
    protected UUID owner;
    protected World world;
    protected BlockFace face;
    protected int chunkX;
    protected int chunkZ;

    protected P producer;

    protected SimpleLocation base;
    protected SimpleLocation deposit;

    protected double available;
    protected T current;

    protected BluePrint bluePrint;

    protected boolean full;
    protected boolean working;
    protected boolean wrong;
    protected boolean changed;

    //===============ENCAPSULATION===============

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockFace getFace() {
        return face;
    }

    public void setFace(BlockFace face) {
        this.face = face;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(P producer) {
        this.producer = producer;
    }

    public SimpleLocation getBase() {
        return base;
    }

    public void setBase(SimpleLocation base) {
        this.base = base;
    }

    public SimpleLocation getDeposit() {
        return deposit;
    }

    public void setDeposit(SimpleLocation deposit) {
        this.deposit = deposit;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public Product getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    public BluePrint getBluePrint() {
        return bluePrint;
    }

    public void setBluePrint(BluePrint bluePrint) {
        this.bluePrint = bluePrint;
        this.random = bluePrint.getRandom();
        this.setProducer((P) bluePrint.getProducer());
    }

    public boolean isWorking() {
        return working;
    }

    public boolean isWrong() {
        return wrong;
    }

    public void setWrong(boolean wrong) {
        this.wrong = wrong;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    //===============ENCAPSULATION===============

    //===============METHODS===============

    public boolean matchChunk(int x, int z) {
        return this.chunkX == x && this.chunkZ == z;
    }

    protected Inventory getInventory(SimpleLocation location) {
        Block block = location.getBlock(world);
        BlockState state = block.getState();

        if (!(state instanceof Chest)) {
            wrong = true;
            return null;
        }

        return ((Chest) state).getBlockInventory();
    }

    public void destroy() {
        Block block = getBase().getBlock(getWorld());
        block.setType(Material.AIR);
        block.getWorld().dropItem(block.getLocation(), getBluePrint().getBook());
    }


    public void setWorking(boolean working) {
        this.working = working;

        Block block = getBase().getBlock(world);
        if (working) block.setType(Material.LAVA);
        else block.setType(Material.WATER);
    }

    public boolean isValid() {
        if (base == null || world == null || bluePrint == null) return false;

        Design design = bluePrint.getDesign();
        deposit = design.getDepositLoc(face, base);

        return deposit.getBlock(world).getType() == Material.CHEST;
    }

    public void searchNextProduct() {
        current = producer.getNextProduct();

        if (current == null) setWorking(false);
    }

    public void checkDeposit() {
        Inventory inventory = getInventory(deposit);

        if (inventory == null) return;

        setFull(InventoryUtils.isFull(inventory, current.getItemStack()));
    }

    protected boolean checkStage() {
        return current != null && !full;
    }

    public void canWork() {
        if (current == null) searchNextProduct();
        if (full && current != null) checkDeposit();
        setWorking(checkStage());
    }

    public void productMade(T product) {
        Inventory inventory = getInventory(deposit);

        if (inventory == null) return;

        int overflow = InventoryUtils.giveItem(inventory, product.getItemStack(), product.getQuantity());

        available -= product.getCost();

        if (overflow != 0) {
            setFull(true);
            setWorking(false);
            available += (overflow) * product.getPerQuantity();
            return;
        }

        setFull(false);
        searchNextProduct();
    }

    public boolean canProduce() {
        return current != null && current.getCost() <= available;
    }

    //=============ABSTRACT=============
    public abstract void tick(long amount);
    //=============ABSTRACT=============

    //=============OVERRIDE=============
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Machine)) return false;
        Machine machine = ((Machine) obj);
        return getWorld().equals(machine.getWorld())
                && base.equals(machine.getBase())
                && uuid.equals(machine.getUuid());
    }
    //=============OVERRIDE=============


    //===============METHODS===============
}