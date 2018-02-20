package com.github.manolo8.simplemachines.utils;

import com.github.manolo8.simplemachines.model.BluePrint;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.MaterialData;

public class MachineBuilder {

    private BluePrint bluePrint;
    private SimpleLocation base;
    private Player owner;
    private boolean notAllowed;
    private boolean finished;
    private BlockFace blockFace;
    private int index;
    private World world;
    private Chunk chunk;
    private SimpleBlock[] blocks;

    public MachineBuilder() {
        index = 0;
    }

    public boolean isNotAllowed() {
        return notAllowed;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setBlockFace(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public void setLocation(Location location) {
        this.world = location.getWorld();
        this.chunk = location.getChunk();
        this.base = new SimpleLocation(location);
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void prepare() {
        //Retorna um blone por conta de haver outras máquinas com outras
        //Posições sendo criadas ao mesmo tempo
        this.blocks = bluePrint.getDesign().getBlocks(getBlockFace());
    }

    public World getWorld() {
        return world;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public SimpleLocation getBase() {
        return base;
    }

    public BluePrint getBluePrint() {
        return bluePrint;
    }

    public void setBluePrint(BluePrint bluePrint) {
        this.bluePrint = bluePrint;
    }

    public Player getOwner() {
        return owner;
    }

    private byte getFace(BlockFace face) {
        switch (face) {
            case NORTH:
                return 0x2;

            case SOUTH:
                return 0x3;

            case WEST:
                return 0x4;

            case EAST:
            default:
                return 0x5;
        }
    }

    public void tick() {
        if (index >= blocks.length) {
            finished = true;
        }

        if (finished || notAllowed) return;
        SimpleBlock simpleBlock = blocks[index];
        Block block = simpleBlock.getByBase(base).getBlock(world);

//        block.setType(Material.STONE);
//
//        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, owner);
//        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);
//
//        if (!(!blockBreakEvent.isCancelled() || (block.getType() != Material.STONE))) {
//            notAllowed = true;
//            return;
//        }

        block.setType(simpleBlock.getMaterial());
        MaterialData data = block.getState().getData();

        if (simpleBlock.getMaterial() == Material.CHEST) {
            data.setData(getFace(blockFace));
            block.getState().setData(data);
        }

        index++;
    }
}
