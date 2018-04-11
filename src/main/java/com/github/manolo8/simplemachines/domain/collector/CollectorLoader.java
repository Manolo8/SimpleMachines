package com.github.manolo8.simplemachines.domain.collector;

import com.github.manolo8.simplemachines.SimpleMachines;
import com.github.manolo8.simplemachines.domain.collector.types.MultiPlantCollector;
import com.github.manolo8.simplemachines.domain.collector.types.PlantCollector;
import com.github.manolo8.simplemachines.domain.collector.types.SinglePlantCollector;
import com.github.manolo8.simplemachines.domain.fuel.Fuel;
import com.github.manolo8.simplemachines.domain.fuel.FuelDesign;
import com.github.manolo8.simplemachines.domain.fuel.Fuelling;
import com.github.manolo8.simplemachines.model.BluePrintLoader;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CollectorLoader extends BluePrintLoader<CollectorBluePrint, FuelDesign, CollectorProducer> {

    private static Map<String, CollectorProduct> products;

    static {
        products = new HashMap<>();
        products.put("potato", new PlantCollector(Material.POTATO, new ItemStack(Material.POTATO_ITEM), 3));
        products.put("carrot", new PlantCollector(Material.CARROT, new ItemStack(Material.CARROT_ITEM), 4));
        products.put("wheat", new PlantCollector(Material.CROPS, new ItemStack(Material.WHEAT), 1));
        products.put("sugar", new MultiPlantCollector(Material.SUGAR_CANE_BLOCK, new ItemStack(Material.SUGAR_CANE), 1));
        products.put("cactus", new MultiPlantCollector(Material.CACTUS, new ItemStack(Material.CACTUS), 1));
        products.put("melon", new SinglePlantCollector(Material.MELON_BLOCK, new ItemStack(Material.MELON), 9));
        products.put("pumpkin", new SinglePlantCollector(Material.PUMPKIN, new ItemStack(Material.PUMPKIN), 1));
    }

    public CollectorLoader(Random random, Map<String, ItemStack> map) {
        super(random, map);
    }

    @Override
    public CollectorBluePrint load(ConfigurationSection section) {
        CollectorBluePrint collectorBluePrint = new CollectorBluePrint();

        FuelDesign design = new FuelDesign();
        getDesign(section.getConfigurationSection("design"), design);

        CollectorProducer producer = new CollectorProducer(random);
        getProducer(section, producer);

        Fuelling fuelling = new Fuelling();
        getFuelling(section.getConfigurationSection("fuels"), fuelling);

        List<ItemStack> buildCost = new ArrayList<>();
        getBuildCost(section.getConfigurationSection("build"), buildCost);

        collectorBluePrint.setPrice(section.getDouble("price"));
        collectorBluePrint.setFuelling(fuelling);
        collectorBluePrint.setBuildCost(buildCost);
        collectorBluePrint.setProducer(producer);
        collectorBluePrint.setDesign(design);
        collectorBluePrint.setRange(section.getInt("range.xz", 10));
        collectorBluePrint.setRangeY(section.getInt("range.y", 10));
        collectorBluePrint.setRandom(random);

        return collectorBluePrint;
    }

    @Override
    public boolean match(String type) {
        return type.equals("collector");
    }

    @Override
    protected void getProducer(ConfigurationSection section, CollectorProducer collectorProducer) {
        List<CollectorProduct> collect = new ArrayList<>();
        List<String> blocks = section.getStringList("collect");

        for (String block : blocks) {
            CollectorProduct product = products.get(block.toLowerCase());
            if (product == null) {
                SimpleMachines.ERROR("Collectible item '" + block + "' not found!");
                continue;
            }

            collect.add(product);
        }

        collectorProducer.setProducts(collect);
    }

    private void getFuelling(ConfigurationSection section, Fuelling fuelling) {
        List<Fuel> fuels = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            fuels.add(new Fuel(getItemStack(material, Material.COAL),
                    section.getInt(material + ".burn", 50),
                    section.getDouble(material + ".efficiency", 1)));
        }
        fuelling.setFuels(fuels);
    }
}
