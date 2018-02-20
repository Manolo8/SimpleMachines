package com.github.manolo8.simplemachines.service;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.dao.ChunkIDDao;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.ChunkID;
import com.github.manolo8.simplemachines.model.Machine;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Serviço usado pela MachineService
public class ChunkIDService {

    private final ChunkIDDao chunkIDDao;
    private final List<ChunkID> chunkIDS;

    public ChunkIDService(ChunkIDDao chunkIDDao) {
        this.chunkIDS = new ArrayList<>();
        this.chunkIDDao = chunkIDDao;
        try {
            chunkIDS.addAll(chunkIDDao.findAllChunkId());
        } catch (DataBaseException e) {
            SimpleMachines.ERROR(e.getMessage(), e.getStackTrace());
        }
    }

    public boolean hasMachineOnChunk(Chunk chunk) {
        return hasMachineOnChunk(chunk.getX(), chunk.getZ(), chunk.getWorld().getUID());
    }

    /**
     * Usado para verificar se há maquinas na chunk
     * caso tenha, a MachineService irá efetuar uma
     * query SQL para pegar a máquina.
     *
     * @param x     chunkX
     * @param z     chunkZ
     * @param world chunkWorld
     * @return true se há máquinas na chunk ou false
     */
    private boolean hasMachineOnChunk(int x, int z, UUID world) {
        for (ChunkID chunkID : chunkIDS) if (chunkID.match(x, z, world)) return true;
        return false;
    }

    public void addMachineOnChunk(Machine machine) {
        addMachineOnChunk(machine.getChunkX(), machine.getChunkZ(), machine.getWorld().getUID());
    }

    /**
     * @param x     chunkX
     * @param z     chunkZ
     * @param world chunkWorld
     */
    private void addMachineOnChunk(int x, int z, UUID world) {
        for (ChunkID chunkID : chunkIDS)
            if (chunkID.match(x, z, world)) {
                chunkID.setQuantity(chunkID.getQuantity() + 1);
                return;
            }

        chunkIDS.add(new ChunkID(x, z, world));
    }

    public void removeMachineOnChunk(Machine machine) {
        removeMachineOnChunk(machine.getChunkX(), machine.getChunkZ(), machine.getWorld().getUID());
    }

    private void removeMachineOnChunk(int x, int z, UUID world) {
        for (ChunkID chunkID : chunkIDS) {
            if (!chunkID.match(x, z, world)) continue;

            //Diminui a quantidade de máquinas na chunk em 1
            chunkID.setQuantity(chunkID.getQuantity() - 1);

            //Se a quantidade de máquinas na chunk for 0 remove do
            //sistema
            if (chunkID.getQuantity() == 0) chunkIDS.remove(chunkID);
            //Quebra o for para não dar erro '-' e por que
            //Não tem nescessidade de continuar
            break;
        }
    }
}

