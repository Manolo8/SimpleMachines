package com.github.manolo8.simplemachines.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.UUID;

public class SimpleLocation implements Cloneable {

    protected int x;
    protected int y;
    protected int z;

    public SimpleLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SimpleLocation(Location location) {
        this.x = (int) location.getX();
        this.y = (int) location.getY();
        this.z = (int) location.getZ();
    }

    public static SimpleLocation fromString(String string) {
        try {
            String[] pos = string.split(" ");
            return new SimpleLocation(Integer.valueOf(pos[0]), Integer.valueOf(pos[1]), Integer.valueOf(pos[2]));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isInArea(int x, int y, int z, int s) {
        return (y >= this.y - s && y <= this.y + s)
                && (z >= this.z - s && z <= this.z + s)
                && (x >= this.x - s && x <= this.x + s);
    }

    public boolean isInArea(int x, int y, int z, int XZ, int Y) {
        return (y >= this.y - Y && y <= this.y + Y)
                && (z >= this.z - XZ && z <= this.z + XZ)
                && (x >= this.x - XZ && x <= this.x + XZ);
    }

    public Block getBlock(UUID world) {
        return getBlock(Bukkit.getWorld(world));
    }

    public Block getBlock(World world) {
        return world.getBlockAt(x, y, z);
    }

    public SimpleLocation add(int x, int y, int z) {
        return new SimpleLocation(this.x + x, this.y + y, this.z + z);
    }

    public SimpleLocation add(int x, int z) {
        return new SimpleLocation(this.x + x, this.y, this.z + z);
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SimpleLocation location = (SimpleLocation) object;
        return x == location.x &&
                y == location.y &&
                z == location.z;
    }

    public SimpleLocation add(SimpleLocation location) {
        return add(location.x, location.y, location.z);
    }

    public Location getLocation(World world) {
        return new Location(world, x, y, z);
    }
}
