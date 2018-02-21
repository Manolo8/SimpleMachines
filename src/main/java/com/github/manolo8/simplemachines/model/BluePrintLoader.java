package com.github.manolo8.simplemachines.model;

import com.github.manolo8.simplemachines.SimpleMachines;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BluePrintLoader<T extends BluePrint, D extends Design, P extends Producer> {

    protected final Random random;

    public BluePrintLoader(Random random) {
        this.random = random;
    }

    public abstract T load(ConfigurationSection section);

    public abstract boolean match(String type);

    public void getDesign(ConfigurationSection section, D d) {
        d.setWall(getMaterial(section.getString("wall"), Material.STONE, true));
        d.setDivisor(getMaterial(section.getString("separator"), Material.COBBLESTONE, true));
        d.build();
    }

    protected void getProducer(ConfigurationSection section, P p) {
        List<Product> products = new ArrayList<>();
        for (String material : section.getKeys(false)) {
            Product product = new Product();
            product.setMaterial(getMaterial(material, Material.STONE));
            product.setCost(section.getInt(material, 50));
            product.setQuantity(1);
            products.add(product);
        }
        p.setProducts(products);
    }

    protected void getBuildCost(ConfigurationSection section, List<ItemStack> list) {
        for (String material : section.getKeys(false))
            list.add(new ItemStack(getMaterial(material, Material.DIAMOND), section.getInt(material, 50)));
    }

    protected Material getMaterial(String name, Material def) {
        return getMaterial(name, def, false);
    }

    /**
     * @param name  Nome do material
     * @param def   material caso o que seja encontrado seja inválido
     * @param solid forçar ser sólido
     * @return o material, caso tudo esteja corredo ou o def caso algo esteja
     * incorreto
     */
    protected Material getMaterial(String name, Material def, boolean solid) {
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
