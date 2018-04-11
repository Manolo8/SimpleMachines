package com.github.manolo8.simplemachines.utils.item;

import com.github.manolo8.simplemachines.Config;
import com.github.manolo8.simplemachines.SimpleMachines;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private HashMap<String, Enchantment> map;

    public ItemFactory() {
        map = new HashMap<>();
        map.put("PROTECTION", Enchantment.PROTECTION_ENVIRONMENTAL);
        map.put("BLAST_PROTECTION", Enchantment.PROTECTION_EXPLOSIONS);
        map.put("FIRE_PROTECTION", Enchantment.PROTECTION_FIRE);
        map.put("PROJECTILE_PROTECTION", Enchantment.PROTECTION_PROJECTILE);
        map.put("UNBREAKING", Enchantment.DURABILITY);
        map.put("SHARPNESS", Enchantment.DAMAGE_ALL);
        map.put("BANE_OF_ARTHROPODS", Enchantment.DAMAGE_ARTHROPODS);
        map.put("SMITE", Enchantment.DAMAGE_UNDEAD);
        map.put("POWER", Enchantment.ARROW_DAMAGE);
        map.put("FLAME", Enchantment.ARROW_FIRE);
        map.put("PUNCH", Enchantment.ARROW_KNOCKBACK);
        map.put("INFINITY", Enchantment.ARROW_INFINITE);
        map.put("FEATHER_FALLING", Enchantment.PROTECTION_FALL);
        map.put("FORTUNE", Enchantment.LOOT_BONUS_BLOCKS);
        map.put("LOOTING", Enchantment.LOOT_BONUS_MOBS);
        map.put("EFFICIENCY", Enchantment.DIG_SPEED);
    }

    private ItemStack create(ConfigurationSection section) {
        Material material = Material.getMaterial(section.getString("type").toUpperCase());
        if (material == null) return null;

        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();

        if (section.contains("enchantments"))
            addEnchantments(section.getConfigurationSection("enchantments"), meta);
        if (section.contains("display"))
            meta.setDisplayName(section.getString("display").replaceAll("&", "§"));
        if (section.contains("lore"))
            meta.setLore(loadLore(section));

        stack.setItemMeta(meta);
        return stack;
    }

    private List<String> loadLore(ConfigurationSection section) {
        List<String> adapted = new ArrayList<>();

        for (String string : section.getStringList("lore")) {
            adapted.add(string.replaceAll("&", "§"));
        }

        return adapted;
    }

    private void addEnchantments(ConfigurationSection section, ItemMeta meta) {
        for (String str : section.getKeys(false)) {
            Enchantment enchantment;

            enchantment = map.get(str.toUpperCase());
            if (enchantment == null) enchantment = Enchantment.getByName(str.toUpperCase());

            if (enchantment == null) {
                SimpleMachines.ERROR("The enchantment named " + str + " not found!");
                continue;
            }

            meta.addEnchant(enchantment, section.getInt(str, 1), true);
        }
    }

    public Map<String, ItemStack> create(Config config) {
        ConfigurationSection section = config.getConfig().getConfigurationSection("custom_items");

        Map<String, ItemStack> customItems = new HashMap<>();

        for (String str : section.getKeys(false)) {
            customItems.put(str.toUpperCase(), create(section.getConfigurationSection(str)));
        }

        return customItems;
    }
}
