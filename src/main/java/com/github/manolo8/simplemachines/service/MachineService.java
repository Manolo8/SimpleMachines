package com.github.manolo8.simplemachines.service;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.dao.MachineDao;
import com.github.manolo8.simplemachines.domain.collector.CollectorMachine;
import com.github.manolo8.simplemachines.model.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MachineService {

    private MachineDao machineDao;
    private ChunkIDService chunkIDService;
    private List<Machine> machines;
    private List<CollectorMachine> collectorMachines;

    public MachineService(MachineDao machineDao,
                          ChunkIDService chunkIDService) {
        this.machineDao = machineDao;
        this.chunkIDService = chunkIDService;
        this.machines = new ArrayList<>();
        this.collectorMachines = new ArrayList<>();
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

    public List<CollectorMachine> getCollectorMachines() {
        return collectorMachines;
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

    public void loadedMachine(Machine machine) {
        machines.add(machine);

        if (machine instanceof CollectorMachine) {
            collectorMachines.add((CollectorMachine) machine);
        }
    }

    public void unloadedMachine(Machine machine) {
        machineDao.saveMachine(machine);

        if (machine instanceof CollectorMachine) {
            collectorMachines.remove(machine);
        }

        Iterator<Machine> i = machines.iterator();

        while (i.hasNext()) {
            Machine loop = i.next();
            if (loop.equals(machine)) {
                i.remove();
                break;
            }
        }
    }

    public void createNewMachine(Machine machine) {
        if (!machine.isValid()) return;
        machineDao.saveNewMachine(machine);
        loadedMachine(machine);
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
                loadedMachine(machine);
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

        World world = chunk.getWorld();

        for (Machine machine : machines) {
            if (machine.matchChunk(chunk.getX(), chunk.getZ())
                    && machine.getWorld().equals(world)) {
                unloadedMachine(machine);
            }
        }
    }

    public CollectorMachine getCollectorMachine(Block block) {
        for(CollectorMachine machine : collectorMachines) {
            if(machine.isInArea(block) && machine.isWorking()) return machine;
        }

        return null;
    }
}
