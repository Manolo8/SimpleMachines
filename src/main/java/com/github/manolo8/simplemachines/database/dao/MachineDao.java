package com.github.manolo8.simplemachines.database.dao;

import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.Machine;

import java.util.List;
import java.util.UUID;

/**
 * O serviço desse DAO terá que ter acesso
 * ao serviço do {@link BluePrint}.
 */
public interface MachineDao {

    /**
     * O serviço deverá ter informações sobre o {@link BluePrint}
     * Para que possa carregar as informações básicas da máquina
     *
     * @param x coordenadas da chunk
     * @param z coordenadas da chunk
     * @return uma lista com todas as máquinas nessa chunk
     */
    List<Machine> loadFromChunk(int x, int z, UUID world);


    /**
     * Salva uma determinada máquina no banco de dados
     * e retorna um UUID único para ser salvo posteriormente
     *
     * @param machine que será salva
     */
    void saveNewMachine(Machine machine);

    /**
     * Salva uma determinada máquina no banco de dados
     * **Apenas salva, retorna caso o UUID não esteja
     * definido
     *
     * @param machine que será salva
     */
    void saveMachine(Machine machine);

    /**
     * Salva uma lista de máquina no banco de dados
     * **Tem mais performance quando for para múltiplas
     *
     * @param machines máquinas que serão salvas no
     *                 no banco de dados
     */
    void saveMachine(List<Machine> machines);

    /**
     * Remove uma máquina do banco de dados
     *
     * @param uuid identificação da máquina
     */
    void deleteMachine(UUID uuid);
}
