package com.github.manolo8.simplemachines.domain.collector.types;

import com.github.manolo8.simplemachines.domain.collector.CollectorProduct;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class MultiPlantCollector extends CollectorProduct {

    public MultiPlantCollector(Material plantItem, ItemStack itemStack, int quantity) {
        super(plantItem, itemStack, quantity);
    }

    @Override
    public boolean match(BlockState state) {
        return plantItem.equals(state.getType());
    }

    @Override
    public boolean isReady(Block block, BlockState state) {
        return block.getRelative(0, -1, 0).getType().equals(plantItem);
    }

    @Override
    public ItemStack[] breakAndGetDrops(Block block, BlockState state, Random random) {
        int dropsQuantity = 0;

        int y = 0;

        for (; y < 255; y++) {
            if (!match(block.getRelative(0, y, 0).getState())) break;
        }

        y += block.getY() - 1;

        block = block.getWorld().getBlockAt(block.getX(), y, block.getZ());

        for (int i = 0; i > -10; i--) {
            Block relative = block.getRelative(0, i, 0);
            BlockState compare = relative.getY() == block.getY() ? state : relative.getState();

            if (match(compare) && match(relative.getRelative(0, -1, 0).getState())) {
                dropsQuantity++;
                relative.setType(Material.AIR);
                compare.setType(Material.AIR);
                block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 40);
            } else break;
        }

        ItemStack drops = itemStack.clone();
        drops.setAmount(quantity * dropsQuantity);

        if (drops.getAmount() > 0) return new ItemStack[]{drops};

        return new ItemStack[0];
    }
}
