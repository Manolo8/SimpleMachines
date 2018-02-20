package com.github.manolo8.simplemachines.controller;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.service.MachineService;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import com.github.manolo8.simplemachines.utils.MachineBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BluePrintController implements Runnable {

    private BluePrintService bluePrintService;
    private MachineService machineService;
    private List<MachineBuilder> building;

    public BluePrintController(MachineService machineService,
                               BluePrintService bluePrintService) {
        this.bluePrintService = bluePrintService;
        this.machineService = machineService;
        this.building = new ArrayList<>();
    }

    public boolean addMachineCreation(String name, Player owner, BlockFace blockFace, Location base) {
        BluePrint bluePrint = bluePrintService.getBluePrint(name.substring(3, name.length()));

        if (bluePrint == null) return false;

        Inventory inventory = owner.getInventory();
        if (!InventoryUtils.hasItems(inventory, bluePrint.getBuildCost())) return false;

        InventoryUtils.removeItems(inventory, bluePrint.getBuildCost());

        MachineBuilder builder = new MachineBuilder();
        builder.setBlockFace(blockFace);
        builder.setBluePrint(bluePrint);
        builder.setOwner(owner);
        builder.setLocation(base.add(0, 1, 0));

        builder.prepare();

        building.add(builder);

        return true;
    }

    public boolean isValidMachine(ItemMeta itemMeta) {
        return itemMeta instanceof BookMeta
                && ((BookMeta) itemMeta).hasAuthor()
                && ((BookMeta) itemMeta).getAuthor().equals("SimpleMachines")
                && itemMeta.getDisplayName() != null
                && itemMeta.getDisplayName().startsWith("-> ")
                && itemMeta.getDisplayName().length() > 5;
    }

    public boolean isValidPosition(Location location) {
        Location building = location.clone().add(-1, 2, -1);

        for (int x = 0; x < 3; x++)
            for (int y = 0; y < 3; y++)
                for (int z = 0; z < 3; z++)
                    if (building.clone().add(x, -y, z).getBlock().getType() != Material.AIR) return false;
//                    Material material = building.clone().add(x, -y, z).getBlock().getType();
//                    if (!(material == Material.IRON_BLOCK || material == Material.AIR)) return false;


        return true;
    }

//    public int countIronBlocks(Location location) {
//        Location building = location.clone().add(-1, 2, -1);
//
//        int ironBlocks = 0;
//
//        for (int x = 0; x < 3; x++)
//            for (int y = 0; y < 3; y++)
//                for (int z = 0; z < 3; z++)
//                    if (building.clone().add(x, -y, z).getBlock().getType() == Material.IRON_BLOCK) ironBlocks++;
//
//        return ironBlocks;
//    }

    private Machine fromMachineBuilder(MachineBuilder builder) {
        Machine machine;
        try {
            machine = builder.getBluePrint().getType().getMachine();
        } catch (DataBaseException e) {
            SimpleMachines.ERROR("Machine " + builder.getBluePrint().getName() + " not found.");
            return null;
        }

        machine.setFace(builder.getBlockFace());
        machine.setBase(builder.getBase());
        machine.setWorld(builder.getWorld());
        machine.setAvailable(0);
        Chunk chunk = builder.getChunk();
        machine.setChunkX(chunk.getX());
        machine.setChunkZ(chunk.getZ());
        machine.setBluePrint(builder.getBluePrint());
        machine.setOwner(builder.getOwner().getUniqueId());

        return machine;
    }


    @Override
    public void run() {
        Iterator<MachineBuilder> i = building.iterator();

        while (i.hasNext()) {
            MachineBuilder builder = i.next();

            if (builder.isNotAllowed()) {
                //Remover a máquina e dar o blueprint
                //Não dar os materiais
                i.remove();
            }

            if (builder.isFinished()) {
                //Maquina construida com sucesso!
                //Adicionar ao banco de dados e ao MachineController

                machineService.createNewMachine(fromMachineBuilder(builder));

                i.remove();
            }

            builder.tick();
        }
    }
}
