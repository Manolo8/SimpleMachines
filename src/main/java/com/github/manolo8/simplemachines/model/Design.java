package com.github.manolo8.simplemachines.model;

import com.github.manolo8.simplemachines.utils.SimpleBlock;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public abstract class Design {

    protected SimpleBlock[] blocks;
    protected Material wall;
    protected Material divisor;

    public void setWall(Material wall) {
        this.wall = wall;
    }

    public void setDivisor(Material divisor) {
        this.divisor = divisor;
    }

    public SimpleLocation getDepositLoc(BlockFace face, SimpleLocation base) {
        return blocks[1].clone().rotate(face).add(base);
    }

    public SimpleLocation getUpgradeLoc(BlockFace face, SimpleLocation base) {
        return blocks[22].clone().rotate(face).add(base);
    }

    protected abstract void buildDefaults();

    private void fill() {
        for (SimpleBlock block : blocks) block.setMaterial(wall);
    }

    private void divisor() {
        blocks[3].setMaterial(divisor);
        blocks[4].setMaterial(divisor);
        blocks[5].setMaterial(divisor);
        blocks[12].setMaterial(divisor);
        blocks[13].setMaterial(divisor);
        blocks[14].setMaterial(divisor);
        blocks[21].setMaterial(divisor);
        blocks[22].setMaterial(divisor);
        blocks[23].setMaterial(divisor);
    }

    public void build() {
        blocks = new SimpleBlock[27];
        int i = 0;
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    blocks[i] = new SimpleBlock(x, y, z);
                    i++;
                }
            }
        }

        fill();
        divisor();

        blocks[1].setMaterial(Material.CHEST);
        blocks[4].setMaterial(Material.GLASS);
        blocks[13].setMaterial(Material.STONE);
        //Upgrades
        blocks[19].setMaterial(Material.CHEST);
        blocks[22].setMaterial(Material.GLASS);

        buildDefaults();
    }

    public SimpleBlock[] getBlocks(BlockFace face) {
        SimpleBlock[] clone = new SimpleBlock[27];

        for (int i = 0; i < clone.length; i++) {
            clone[i] = blocks[i].clone().rotate(face);
        }

        return clone;
    }
}
