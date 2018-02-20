package com.github.manolo8.simplemachines.domain.fuel;

import com.github.manolo8.simplemachines.model.Design;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class FuelDesign extends Design {

    public SimpleLocation getFuelDepositLoc(BlockFace face, SimpleLocation base) {
        return blocks[7].clone().rotate(face).add(base);
    }

    @Override
    protected void buildDefaults() {
        //Deposito combustivel
        blocks[7].setMaterial(Material.CHEST);
    }
}
