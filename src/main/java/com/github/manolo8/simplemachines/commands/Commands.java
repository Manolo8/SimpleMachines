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

/**
 * @author Willian
 */
@SuppressWarnings("unused")
public class Commands {

    private final BluePrintService bluePrintService;
    private final Economy economy;
    private final Language language;

    public Commands(BluePrintService bluePrintService,
                    Economy economy,
                    Language language) {
        this.bluePrintService = bluePrintService;
        this.economy = economy;
        this.language = language;
    }

    @CommandMapping(command = "machine",
            subCommand = "command.machine.buy",
            args = 2,
            usage = "command.machine.buy.usage")
    public void comprar(Player author, String[] args) {
        BluePrint bluePrint = bluePrintService.getBluePrint(args[1]);

        if (bluePrint == null) {
            author.sendMessage(language.getString("command.machine.blueprint.not.found"));
            return;
        }

        if (!author.hasPermission("machines.buy." + bluePrint.getName())) {
            author.sendMessage(language.getString("command.no.permission"));
            return;
        }

        if (InventoryUtils.isFull(author.getInventory(), Material.WRITTEN_BOOK)) {
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
            args = 1,
            usage = "command.machine.list.usage")
    public void listar(Player author, String[] args) {
        StringBuilder builder = new StringBuilder();
        builder.append(language.getString("command.machine.list.header")).append("\n");

        Replace replace = language.getReplacer("command.machine.list.body");

        for (BluePrint bluePrint : bluePrintService.getBluePrints()) {
            replace.setValue("name", bluePrint.getName())
                    .setValue("price", bluePrint.getPrice()).build(builder).append("\n");
        }

        author.sendMessage(builder.toString());
    }

}
