package com.github.manolo8.simplemachines.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class Face {
    public static BlockFace getFace(Player player) {
        float yaw = player.getLocation().getYaw();
        //Como assim? O yaw não era negativo '-'
        if (yaw < 0) yaw += 360;
        if (yaw < 45) return BlockFace.SOUTH;
        else if (yaw < 135) return BlockFace.WEST;
        else if (yaw < 225) return BlockFace.NORTH;
        else if (yaw < 315) return BlockFace.EAST;
        else return BlockFace.SOUTH;
    }
}
