package com.github.manolo8.simplemachines.utils.book;

import com.github.manolo8.simplemachines.domain.fuel.Fuel;
import com.github.manolo8.simplemachines.domain.fuel.FuelBluePrint;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientProduct;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.MachineType;
import com.github.manolo8.simplemachines.model.Product;
import com.github.manolo8.simplemachines.utils.replace.Replace;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BookFactory {

    private Map<String, Replace> formatters;
    private Map<String, String> materials;
    private List<Replace> pages;
    private Replace loreReplace;
    private int a = 0;

    public BookFactory(File file) throws IOException {
        formatters = new HashMap<>();
        materials = new HashMap<>();
        pages = new ArrayList<>();

        List<Node> nodes = new NodeFactory().findNodes(file);

        for (Node node : nodes) {
            if (node.getKey().equals("formatter")) {
                Replace replace = new Replace(node.getTemplateAsString()).compile();
                formatters.put(node.getValue(), replace);
                continue;
            }

            if (node.getKey().equals("page")) {
                Replace replace = new Replace(node.getTemplateAsString()).compile();
                pages.add(replace);
                continue;
            }

            if (node.getKey().equals("lore")) {
                loreReplace = new Replace(node.getTemplateAsString()).compile();
                continue;
            }

            if (node.getKey().equals("material")) {
                String[] template = node.getTemplate();
                materials.put(node.getValue(), template.length > 0 ? template[0] : "WRONG");
            }
        }
    }

    public ItemStack generateBook(BluePrint bluePrint) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) itemStack.getItemMeta();
        meta.setDisplayName("-> " + bluePrint.getName());
        meta.setAuthor("SimpleMachines");

        StringBuilder builder = new StringBuilder();

        Replace costReplace = formatters.get("cost");
        for (ItemStack stack : bluePrint.getBuildCost()) {
            builder.append(costReplace
                    .setValue("material", getName(stack))
                    .setValue("quantity", stack.getAmount()));
        }

        String cost = builder.toString();
        builder.setLength(0);

        if (bluePrint.getType() == MachineType.FUEL) {
            Replace productionFormatter = formatters.get("production_fuel");
            for (Product product : bluePrint.getProducer().getProducts()) {
                builder.append(productionFormatter
                        .setValue("material", getName(product.getMaterial()))
                        .setValue("ticks", product.getCost()));
            }
        } else if (bluePrint.getType() == MachineType.INGREDIENT) {
            Replace productionFormatter = formatters.get("production_ingredient");
            for (Product product : bluePrint.getProducer().getProducts()) {
                builder.append(productionFormatter
                        .setValue("material", getName(product.getMaterial()))
                        .setValue("with", getName(((IngredientProduct) product).getIngredient()))
                        .setValue("ticks", product.getCost()));
            }
        }

        String production = builder.toString();

        builder.setLength(0);

        if (bluePrint instanceof FuelBluePrint) {
            Replace fuelReplace = formatters.get("fuel");
            for (Fuel fuel : ((FuelBluePrint) bluePrint).getFuelling().getFuels()) {
                builder.append(fuelReplace
                        .setValue("material", getName(fuel.getMaterial()))
                        .setValue("speed", fuel.getSpeed())
                        .setValue("ticks", fuel.getBurnTime()));
            }
        }

        String fuel = builder.toString();

        builder.setLength(0);

        String[] lore = loreReplace.setValue("cost", cost).build().split("\n");

        meta.setLore(Arrays.asList(lore));

        List<String> pgs = new ArrayList<>();

        for (Replace page : pages) {
            String str = page.setValue("production", production)
                    .setValue("cost", cost)
                    .setValue("fuel", fuel)
                    .setValue("name", bluePrint.getName())
                    .build();
            StringBuilder builder2 = new StringBuilder();
            int lines = 0;
            char lc = '.';
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);

                if (lines == 12) {
                    lines = 0;
                    pgs.add(builder2.toString());
                    builder2.setLength(0);
                }

                builder2.append(c);
                if (c == '\n') {
                    lines++;
                }
            }
            if (builder2.length() != 0) pgs.add(builder2.toString());
        }

        meta.setPages(pgs);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private String getName(ItemStack itemStack) {
        return getName(itemStack.getType());
    }

    private String getName(Material material) {
        String type = material.name().toLowerCase();
        return materials.getOrDefault(type, type);
    }
}
