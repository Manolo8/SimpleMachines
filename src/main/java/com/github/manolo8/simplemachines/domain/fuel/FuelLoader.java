package com.github.manolo8.simplemachines.domain.fuel;

import com.github.manolo8.simplemachines.model.BluePrintLoader;
import com.github.manolo8.simplemachines.model.Producer;
import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FuelLoader extends BluePrintLoader<FuelBluePrint, FuelDesign, Producer<Product>> {

    public FuelLoader(Random random) {
        super(random);
    }

    @Override
    public FuelBluePrint load(ConfigurationSection section) {
        FuelBluePrint fuelBluePrint = new FuelBluePrint();

        FuelDesign design = new FuelDesign();
        getDesign(section.getConfigurationSection("design"), design);

        Producer<Product> producer = new Producer<>(random);
        getProducer(section.getConfigurationSection("produces"), producer);

        Fuelling fuelling = new Fuelling();
        getFuelling(section.getConfigurationSection("fuels"), fuelling);

        List<ItemStack> buildCost = new ArrayList<>();
        getBuildCost(section.getConfigurationSection("build"), buildCost);

        fuelBluePrint.setPrice(section.getDouble("price"));
        fuelBluePrint.setDesign(design);
        fuelBluePrint.setFuelling(fuelling);
        fuelBluePrint.setProducer(producer);
        fuelBluePrint.setBuildCost(buildCost);

        return fuelBluePrint;
    }

    @Override
    public boolean match(String type) {
        return type.equals("fuel");
    }

    private void getFuelling(ConfigurationSection section, Fuelling fuelling) {
        List<Fuel> fuels = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            fuels.add(new Fuel(getItemStack(material, Material.COAL),
                    section.getInt(material + ".burn", 50),
                    section.getDouble(material + ".speed", 1)));
        }
        fuelling.setFuels(fuels);
    }
}
