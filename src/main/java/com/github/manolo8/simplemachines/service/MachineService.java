package com.github.manolo8.simplemachines.service;

import com.github.manolo8.simplemachines.Config;
import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.dao.MachineDao;
import com.github.manolo8.simplemachines.model.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MachineService {

    private MachineDao machineDao;
    private BluePrintService bluePrintService;
    private ChunkIDService chunkIDService;
    private List<Machine> machines;
    private Config config;

    public MachineService(MachineDao machineDao,
                          BluePrintService bluePrintService,
                          ChunkIDService chunkIDService,
                          Config config) {
        this.machineDao = machineDao;
        this.bluePrintService = bluePrintService;
        this.chunkIDService = chunkIDService;
        this.machines = new ArrayList<>();
        this.config = config;
        init();
    }

    private void init() {
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                loadMachines(chunk);
            }
        }
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public Machine getMachine(Location location) {
        return getMachine(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    private Machine getMachine(World world, int x, int y, int z) {
        for (Machine machine : machines) {
            if (!machine.getWorld().equals(world)) continue;
            if (machine.getBase().isInArea(x, y, z, 1)) return machine;
        }
        return null;
    }

    public void createNewMachine(Machine machine) {
        if (!machine.isValid()) return;
        machineDao.saveNewMachine(machine);
        machines.add(machine);
        machine.setChanged(true);
    }

    public void deleteMachine(Machine machine) {
        machine.destroy();
        Iterator<Machine> i = machines.iterator();

        while (i.hasNext()) if (i.next().equals(machine)) i.remove();

        machineDao.deleteMachine(machine.getUuid());
        chunkIDService.removeMachineOnChunk(machine);
    }

    public void loadMachines(Chunk chunk) {
        if (!chunkIDService.hasMachineOnChunk(chunk)) return;
        List<Machine> toLoad = machineDao.loadFromChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());

        for (Machine machine : toLoad) {

            if (machine.isValid()) {
                machines.add(machine);
                machine.setChanged(true);
                continue;
            }
            //Se não for carregada apenas removemos do banco de dados
            //E avisamos no console.
            deleteMachine(machine);
            SimpleMachines.ERROR("Can't load machine with UUID " + machine.getUuid());
        }
    }

    //Quando o servidor ser desativado
    //Chamar está função
    public void saveAllMachines() {
        machineDao.saveMachine(machines);
    }

    public void unloadFromChunk(Chunk chunk) {
        if (!chunkIDService.hasMachineOnChunk(chunk)) return;
        Iterator<Machine> i = machines.iterator();

        World world = chunk.getWorld();
        while (i.hasNext()) {
            Machine machine = i.next();
            if (machine.matchChunk(chunk.getX(), chunk.getZ())
                    && machine.getWorld().equals(world)) {
                i.remove();
                machineDao.saveMachine(machine);
            }
        }
    }
}
