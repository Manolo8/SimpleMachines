package com.github.manolo8.simplemachines.database.dao.impl;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.dao.BluePrintDao;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.BluePrintLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BluePrintDaoImpl implements BluePrintDao {

    /**
     * Arquivo de configuração onde estão
     * armazenados, dentre outras coisas,
     * todos os planos de construção
     */
    private FileConfiguration file;
    private List<BluePrintLoader> bluePrintLoaders;
    private Random random;

    public BluePrintDaoImpl(FileConfiguration file, Random random, List<BluePrintLoader> bluePrintLoaders) {
        this.file = file;
        this.random = random;
        this.bluePrintLoaders = bluePrintLoaders;
    }

    @Override
    public List<BluePrint> loadAll() {
        List<BluePrint> bluePrints = new ArrayList<>();

        for (String name : file.getConfigurationSection("machines").getKeys(false)) {
            try {
                BluePrint bluePrint = load(file.getConfigurationSection("machines." + name));
                bluePrint.setName(name);
                bluePrints.add(bluePrint);
            } catch (Exception e) {
                SimpleMachines.ERROR("Cant find machine with name " + name + "\n probably you make a wrong configuration!");
            }
        }

        return bluePrints;
    }

    private BluePrintLoader findLoader(String type) {
        type = type.toLowerCase();
        for (BluePrintLoader loader : bluePrintLoaders) {
            if (loader.match(type)) return loader;
        }

        return null;
    }

    private BluePrint load(ConfigurationSection section) throws DataBaseException {
        BluePrintLoader loader = findLoader(section.getString("type", "fuel"));

        if (loader == null) throw new DataBaseException("");

        return loader.load(section);
    }
}
