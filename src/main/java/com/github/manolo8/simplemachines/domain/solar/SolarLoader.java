package com.github.manolo8.simplemachines.domain.solar;

import com.github.manolo8.simplemachines.model.BluePrintLoader;
import com.github.manolo8.simplemachines.model.Producer;
import com.github.manolo8.simplemachines.model.Product;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolarLoader extends BluePrintLoader<SolarBluePrint, SolarDesign, Producer<Product>> {

    public SolarLoader(Random random) {
        super(random);
    }

    @Override
    public SolarBluePrint load(ConfigurationSection section) {
        SolarBluePrint solarBluePrint = new SolarBluePrint();

        SolarDesign design = new SolarDesign();
        getDesign(section.getConfigurationSection("design"), design);

        Producer<Product> producer = new Producer<>(random);
        getProducer(section.getConfigurationSection("produces"), producer);

        SolarFuelling fuelling = new SolarFuelling();
        getFuelling(section.getConfigurationSection("fuels"), fuelling);

        List<ItemStack> buildCost = new ArrayList<>();
        getBuildCost(section.getConfigurationSection("build"), buildCost);

        solarBluePrint.setPrice(section.getDouble("price"));
        solarBluePrint.setDesign(design);
        solarBluePrint.setFuelling(fuelling);
        solarBluePrint.setProducer(producer);
        solarBluePrint.setBuildCost(buildCost);

        return solarBluePrint;
    }

    @Override
    public boolean match(String type) {
        return type.equals("solar");
    }

    private void getFuelling(ConfigurationSection section, SolarFuelling fuelling) {
        List<SolarFuel> fuels = new ArrayList<>();
        for (String fuelType : section.getKeys(false))
            fuels.add(new SolarFuel(TimeType.valueOf(fuelType.toUpperCase()), section.getDouble(fuelType, 0.5)));
        fuelling.setFuels(fuels);
    }
}
