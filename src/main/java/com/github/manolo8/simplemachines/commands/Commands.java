package com.github.manolo8.simplemachines.commands;

import com.github.manolo8.simplemachines.Language;
import com.github.manolo8.simplemachines.commands.annotation.CommandMapping;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
import com.github.manolo8.simplemachines.utils.replace.Replace;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author Willian
 */
@SuppressWarnings("unused")
public class Commands {

    private final BluePrintService bluePrintService;
    private final Economy economy;
    private final Language language;
    private final Map<String, ItemStack> customItems;

    public Commands(BluePrintService bluePrintService,
                    Economy economy,
                    Map<String, ItemStack> customItems,
                    Language language) {
        this.bluePrintService = bluePrintService;
        this.economy = economy;
        this.language = language;
        this.customItems = customItems;
    }

    @CommandMapping(command = "machine",
            subCommand = "command.machine.buy",
            args = 2,
            usage = "command.machine.buy.usage")
    public void buy(Player author, String[] args) {
        BluePrint bluePrint = bluePrintService.getBluePrint(args[1]);

        if (bluePrint == null) {
            author.sendMessage(language.getString("command.machine.blueprint.not.found"));
            return;
        }

        if (!author.hasPermission("machines.buy." + bluePrint.getName())) {
            author.sendMessage(language.getString("command.no.permission"));
            return;
        }

        if (InventoryUtils.isFull(author.getInventory(), new ItemStack(Material.WRITTEN_BOOK))) {
            author.sendMessage(language.getString("command.inventory.full"));
            return;
        }

        if (!economy.withdrawPlayer(author, bluePrint.getPrice()).transactionSuccess()) {
            author.sendMessage(language.getReplacer("command.no.sufficient.money").setValue("money", bluePrint.getPrice()).build());
            return;
        }

        author.sendMessage(language.getReplacer("command.machine.buy.success").setValue("name", bluePrint.getName()).build());
        author.getInventory().addItem(bluePrint.getBook());
    }

    @CommandMapping(command = "machine",
            subCommand = "command.machine.list",
            usage = "command.machine.list.usage")
    public void list(Player author, String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(language.getString("command.machine.list.header")).append("\n");

        Replace replace = language.getReplacer("command.machine.list.body");

        for (BluePrint bluePrint : bluePrintService.getBluePrints()) {
            replace.setValue("name", bluePrint.getName())
                    .setValue("price", bluePrint.getPrice()).build(builder).append("\n");
        }

        author.sendMessage(builder.toString());
    }

    @CommandMapping(command = "customitem",
            subCommand = "command.customitem.get",
            args = 2,
            usage = "command.customitem.get.usage")
    public void customItemGet(Player author, String[] args) {

        ItemStack stack = customItems.get(args[1].toUpperCase());

        if (stack != null) {
            author.getInventory().addItem(stack.clone());
            return;
        }
        author.sendMessage("§cItem not found.");
    }

    @CommandMapping(command = "customitem",
            subCommand = "command.customitem.list",
            usage = "command.customitem.list.usage")
    public void customItemList(Player author, String[] args) {

        StringBuilder builder = new StringBuilder();
        builder.append("§aCustom items:\n");

        for (String str : customItems.keySet()) {
            builder.append("§a->").append(str).append("\n");
        }
        author.sendMessage(builder.toString());
    }
}
