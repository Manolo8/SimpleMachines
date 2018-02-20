package com.github.manolo8.simplemachines.utils;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class SimpleBlock extends SimpleLocation {

    private Material material;

    public SimpleBlock() {
        super(0, 0, 0);
    }

    public SimpleBlock(int x, int y, int z) {
        super(x, y, z);
    }

    public SimpleBlock(int x, int y, int z, Material material) {
        super(x, y, z);
        this.material = material;
    }

    public SimpleBlock rotate(BlockFace face) {
        switch (face) {
            case EAST:
                break;
            case WEST:
                x *= -1;
                break;
            case NORTH:
                int q = x;
                x = z;
                z = q * -1;
                break;
            case SOUTH:
                int t = x;
                x = z * -1;
                z = t;
                break;
        }
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public SimpleLocation getByBase(SimpleLocation base) {
        return base.add(this.x, this.y, this.z);
    }

    @Override
    public SimpleBlock clone() {
        return new SimpleBlock(x, y, z, material);
    }
}
