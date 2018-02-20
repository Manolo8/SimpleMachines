package com.github.manolo8.simplemachines.database.dao;

import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.ChunkID;

import java.util.List;

public interface ChunkIDDao {

    /**
     * Usado para saber em quais chunks tem maquinas
     *
     * @return lista de ChunkID
     */
    List<ChunkID> findAllChunkId() throws DataBaseException;
}
