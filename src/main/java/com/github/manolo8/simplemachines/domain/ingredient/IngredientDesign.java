package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.domain.fuel.FuelDesign;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class IngredientDesign extends FuelDesign {

    @Override
    public SimpleLocation getFuelDepositLoc(BlockFace face, SimpleLocation base) {
        return blocks[3].clone().rotate(face).add(base);
    }

    public SimpleLocation getIngredientDepositLoc(BlockFace face, SimpleLocation base) {
        return blocks[5].clone().rotate(face).add(base);
    }

    @Override
    protected void buildDefaults() {
        //Deposito combustivel
        blocks[3].setMaterial(Material.CHEST);
        blocks[0].setMaterial(Material.COAL_BLOCK);
        //Deposito minerios/items
        blocks[5].setMaterial(Material.CHEST);
        blocks[2].setMaterial(Material.IRON_ORE);
        blocks[6].setMaterial(Material.STEP);
        blocks[8].setMaterial(Material.STEP);
    }
}
