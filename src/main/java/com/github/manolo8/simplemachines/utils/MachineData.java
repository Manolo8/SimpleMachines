package com.github.manolo8.simplemachines.utils;

import org.bukkit.block.BlockFace;

import java.util.UUID;

public class MachineData {

    private String bluePrintName;
    private SimpleLocation base;
    private BlockFace face;
    private int chunkX;
    private int chunkZ;
    private double burningTime;
    private double speed;
    private UUID owner;
    private UUID world;
    private UUID uuid;
    private double available;


    public void setBluePrintName(String bluePrintName) {
        this.bluePrintName = bluePrintName;
    }

    public void setBase(SimpleLocation base) {
        this.base = base;
    }

    public void setFace(BlockFace face) {
        this.face = face;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public void setBurningTime(double burningTime) {
        this.burningTime = burningTime;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void setWorld(UUID world) {
        this.world = world;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public String getBluePrintName() {
        return bluePrintName;
    }

    public SimpleLocation getBase() {
        return base;
    }

    public BlockFace getFace() {
        return face;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public double getBurningTime() {
        return burningTime;
    }

    public double getSpeed() {
        return speed;
    }

    public UUID getOwner() {
        return owner;
    }

    public UUID getWorld() {
        return world;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getAvailable() {
        return available;
    }
}
