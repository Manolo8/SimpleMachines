package com.github.manolo8.simplemachines.domain.solar;

import com.github.manolo8.simplemachines.model.Design;
import com.github.manolo8.simplemachines.utils.SimpleLocation;
import org.bukkit.block.BlockFace;

public class SolarDesign extends Design {

    @Override
    protected void buildDefaults() {
        blocks[6].setMaterial(divisor);
        blocks[7].setMaterial(divisor);
        blocks[8].setMaterial(divisor);
        blocks[15].setMaterial(divisor);
        blocks[16].setMaterial(divisor);
        blocks[17].setMaterial(divisor);
        blocks[24].setMaterial(divisor);
        blocks[25].setMaterial(divisor);
        blocks[26].setMaterial(divisor);
    }
}
