package com.github.manolo8.simplemachines.database.dao.impl;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.database.dao.BluePrintDao;
import com.github.manolo8.simplemachines.domain.fuel.Fuel;
import com.github.manolo8.simplemachines.domain.fuel.FuelBluePrint;
import com.github.manolo8.simplemachines.domain.fuel.Fuelling;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientProducer;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientProduct;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.model.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

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
    private Random random;

    public BluePrintDaoImpl(FileConfiguration file, Random random) {
        this.file = file;
        this.random = random;
    }

    @Override
    public List<BluePrint> loadAll() {
        List<BluePrint> bluePrints = new ArrayList<>();

        for (String name : file.getConfigurationSection("machines").getKeys(false)) {
            try {
                bluePrints.add(load(file.getConfigurationSection("machines." + name)));
            } catch (Exception e) {
                SimpleMachines.ERROR("Cant find machine with name " + name);
            }
        }

        return bluePrints;
    }

    private BluePrint load(ConfigurationSection section) throws DataBaseException {
        //Design
        ConfigurationSection designSection = section.getConfigurationSection("design");

        MachineType type = MachineType.getByName(section.getString("type", "fuel"));

        BluePrint bluePrint = type.getMachineBluePrint();
        bluePrint.setType(type);
        Design design = type.getMachineDesign();

        design.setWall(getMaterial(designSection.getString("wall"), Material.STONE, true));
        design.setDivisor(getMaterial(designSection.getString("separator"), Material.COBBLESTONE, true));
        design.build();

        //Custo
        List<ItemStack> buildCost = new ArrayList<>();
        ConfigurationSection buildSection = section.getConfigurationSection("build");
        for (String material : buildSection.getKeys(false)) {
            buildCost.add(new ItemStack(getMaterial(material, Material.DIAMOND), buildSection.getInt(material, 50)));
        }

        //Produção
        bluePrint.setProducer(loadProducer(type, section.getConfigurationSection("produces")));

        //Combustíveis
        if (bluePrint instanceof FuelBluePrint) {
            Fuelling fuelling = new Fuelling();
            List<Fuel> fuels = new ArrayList<>();
            ConfigurationSection fuelSection = section.getConfigurationSection("fuels");
            for (String material : fuelSection.getKeys(false)) {
                fuels.add(new Fuel(getMaterial(material, Material.COAL),
                        fuelSection.getInt(material + ".burn", 50),
                        fuelSection.getDouble(material + ".speed", 1)));
            }
            fuelling.setFuels(fuels);
            ((FuelBluePrint) bluePrint).setFuelling(fuelling);
        }

        //Definindo as variáveis
        bluePrint.setName(section.getName());
        bluePrint.setBuildCost(buildCost);
        bluePrint.setDesign(design);
        bluePrint.setRandom(random);
        bluePrint.setPrice(section.getDouble("price", 0));

        return bluePrint;
    }

    private Producer<? extends Product> loadProducer(MachineType machineType, ConfigurationSection section) {
        switch (machineType) {
            case FUEL:
                return loadProducer(section);
            case INGREDIENT:
                return loadIngredientProducer(section);
            default:
                return null;
        }
    }

    private IngredientProducer loadIngredientProducer(ConfigurationSection section) {
        List<IngredientProduct> products = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            IngredientProduct ingredientProduct = new IngredientProduct();
            ingredientProduct.setMaterial(getMaterial(material, Material.STONE));
            ingredientProduct.setCost(section.getInt(material + ".fuel", 50));
            ingredientProduct.setQuantity(section.getInt(material + ".quantity", 1));
            ingredientProduct.setIngredient(getMaterial(section.getString(material + ".with"), Material.BEDROCK));
            products.add(ingredientProduct);
        }
        return new IngredientProducer(random, products);
    }

    private Producer<? extends Product> loadProducer(ConfigurationSection section) {
        List<Product> products = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            Product product = new Product();
            product.setMaterial(getMaterial(material, Material.STONE));
            product.setCost(section.getInt(material, 50));
            product.setQuantity(1);
            products.add(product);
        }
        return new Producer<>(random, products);
    }

    private Material getMaterial(String name, Material def) {
        return getMaterial(name, def, false);
    }

    /**
     * @param name  Nome do material
     * @param def   material caso o que seja encontrado seja inválido
     * @param solid forçar ser sólido
     * @return o material, caso tudo esteja corredo ou o def caso algo esteja
     * incorreto
     */
    private Material getMaterial(String name, Material def, boolean solid) {
        try {
            Material material = Material.getMaterial(name.toUpperCase());

            if (material == null || (solid && !material.isSolid())) {
                SimpleMachines.ERROR("Material '" + name + "' is not valid");
                return def;
            }
            return material;
        } catch (Exception e) {
            e.printStackTrace();
            SimpleMachines.ERROR("Material '" + name + "' is not valid");
            return def;
        }
    }
}
