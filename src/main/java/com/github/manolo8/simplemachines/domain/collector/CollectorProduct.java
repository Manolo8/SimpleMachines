package com.github.manolo8.simplemachines.domain.collector;

import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public abstract class CollectorProduct extends Product implements Cloneable {

    protected Material plantItem;

    public CollectorProduct(Material plantItem, ItemStack itemStack, int quantity) {
        this.plantItem = plantItem;
        this.itemStack = itemStack;
        this.quantity = quantity;
    }

    public abstract boolean match(BlockState state);

    public abstract boolean isReady(Block block, BlockState state);

    public abstract ItemStack[] breakAndGetDrops(Block block, BlockState state, Random random);
}
