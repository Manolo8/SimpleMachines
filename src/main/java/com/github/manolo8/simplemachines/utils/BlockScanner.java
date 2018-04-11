package com.github.manolo8.simplemachines.utils;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

public class BlockScanner implements Iterator<Block> {

    private World world;
    private int maxX;
    private int maxY;
    private int maxZ;
    private int minX;
    private int minY;
    private int minZ;
    private int x;
    private int y;
    private int z;

    public BlockScanner(World world, SimpleLocation base, int range, int rangeY) {
        this.world = world;
        maxX = base.x + range;
        maxY = base.y + rangeY;
        maxZ = base.z + range;
        minX = base.x - range;
        minY = base.y - rangeY;
        minZ = base.z - range;
        x = minX;
        y = minY;
        z = minZ;
    }

    @Override
    public boolean hasNext() {
        if (z > maxZ) {
            return false;
        }

        if (x >= maxX) {
            x = minX;
            y++;
        }

        if (y >= maxY) {
            y = minY;
            z++;
        }

        x++;
        return true;
    }

    @Override
    public Block next() {
        return world.getBlockAt(x, y, z);
    }
}
