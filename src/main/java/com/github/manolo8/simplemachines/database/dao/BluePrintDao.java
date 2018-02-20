package com.github.manolo8.simplemachines.database.dao;

import com.github.manolo8.simplemachines.model.BluePrint;

import java.util.List;

/**
 * Apenas carrega todas os planos de construção que
 * estão no sistema para serem usados posteriormente
 * *** Carrega apenas quando o servidor inicia
 */
public interface BluePrintDao {

    List<BluePrint> loadAll();
}
