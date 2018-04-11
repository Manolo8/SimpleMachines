package com.github.manolo8.simplemachines.domain.collector.types;

import com.github.manolo8.simplemachines.domain.collector.CollectorProduct;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SinglePlantCollector extends CollectorProduct {

    public SinglePlantCollector(Material plantItem, ItemStack itemStack, int quantity) {
        super(plantItem, itemStack, quantity);
    }

    @Override
    public boolean match(BlockState block) {
        return plantItem.equals(block.getType());
    }

    @Override
    public boolean isReady(Block block, BlockState state) {
        return state.getType().equals(plantItem);
    }

    @Override
    public ItemStack[] breakAndGetDrops(Block block, BlockState state, Random random) {
        block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation(), 40);
        block.setType(Material.AIR);
        state.setType(Material.AIR);

        ItemStack drops = itemStack.clone();
        drops.setAmount(quantity == 1 ? 1 : random.nextInt(quantity) + 1);

        return new ItemStack[]{drops};
    }
}
