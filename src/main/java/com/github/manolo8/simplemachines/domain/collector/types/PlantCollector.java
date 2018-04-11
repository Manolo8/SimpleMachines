package com.github.manolo8.simplemachines.domain.collector.types;

import com.github.manolo8.simplemachines.domain.collector.CollectorProduct;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import java.util.Random;

public class PlantCollector extends CollectorProduct {

    public PlantCollector(Material plantItem, ItemStack itemStack, int quantity) {
        super(plantItem, itemStack, quantity);
    }

    @Override
    public boolean match(BlockState block) {
        return plantItem.equals(block.getType());
    }

    @Override
    public boolean isReady(Block block, BlockState state) {
        Crops crops = (Crops) state.getData();

        return crops.getState().equals(CropState.TALL) || crops.getState().equals(CropState.RIPE);
    }

    @Override
    public ItemStack[] breakAndGetDrops(Block block, BlockState state, Random random) {
        block.setType(plantItem);

        state.setData(new Crops(plantItem, CropState.SEEDED));
        state.update();

        ItemStack stack = itemStack.clone();
        stack.setAmount(random.nextInt(quantity) + 1);
        return new ItemStack[]{stack};
    }
}
