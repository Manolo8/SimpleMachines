package com.github.manolo8.simplemachines.domain.ingredient;

import com.github.manolo8.simplemachines.domain.fuel.Fuel;
import com.github.manolo8.simplemachines.domain.fuel.FuelBluePrint;
import com.github.manolo8.simplemachines.domain.fuel.Fuelling;
import com.github.manolo8.simplemachines.model.BluePrintLoader;
import com.github.manolo8.simplemachines.model.Machine;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngredientLoader extends BluePrintLoader<FuelBluePrint, IngredientDesign, IngredientProducer> {

    public IngredientLoader(Random random) {
        super(random);
    }

    @Override
    public FuelBluePrint load(ConfigurationSection section) {
        FuelBluePrint fuelBluePrint = new FuelBluePrint() {
            //Só precisa dar override nisso que é gg '-'
            @Override
            public Machine newInstance() {
                IngredientMachine ingredientMachine = new IngredientMachine();
                ingredientMachine.setBluePrint(this);
                return ingredientMachine;
            }

            @Override
            public String getSuper() {
                return "ingredient";
            }
        };

        IngredientDesign design = new IngredientDesign();
        getDesign(section.getConfigurationSection("design"), design);

        IngredientProducer producer = new IngredientProducer(random);
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
        return type.equals("ingredient");
    }

    @Override
    protected void getProducer(ConfigurationSection section, IngredientProducer ingredientProducer) {
        List<IngredientProduct> products = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            IngredientProduct ingredientProduct = new IngredientProduct();
            ingredientProduct.setMaterial(getMaterial(material, Material.STONE));
            ingredientProduct.setCost(section.getInt(material + ".fuel", 50));
            ingredientProduct.setQuantity(section.getInt(material + ".quantity", 1));
            ingredientProduct.setIngredient(getMaterial(section.getString(material + ".with"), Material.BEDROCK));
            products.add(ingredientProduct);
        }
        ingredientProducer.setProducts(products);
    }

    private void getFuelling(ConfigurationSection section, Fuelling fuelling) {
        List<Fuel> fuels = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            fuels.add(new Fuel(getMaterial(material, Material.COAL),
                    section.getInt(material + ".burn", 50),
                    section.getDouble(material + ".speed", 1)));
        }
        fuelling.setFuels(fuels);
    }
}
