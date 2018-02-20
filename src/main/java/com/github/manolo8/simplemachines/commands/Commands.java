package com.github.manolo8.simplemachines.commands;

import com.github.manolo8.simplemachines.commands.annotation.CommandMapping;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.utils.InventoryUtils;
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

    public Commands(BluePrintService bluePrintService,
                    Economy economy) {
        this.bluePrintService = bluePrintService;
        this.economy = economy;
    }

    @CommandMapping(command = "comprar",
            superCommand = "machine",
            args = 2,
            usage = "/maquinas comprar <nome>")
    public void comprar(Player author, String[] args) {
        BluePrint bluePrint = bluePrintService.getBluePrint(args[1]);

        if (bluePrint == null) {
            author.sendMessage("§cO plano não foi encontrado!");
            author.sendMessage("§cUse /maquinas listar para ver todas!");
            return;
        }

        if (!author.hasPermission("machines.buy." + bluePrint.getName())) {
            author.sendMessage("§cVocê não tem permissão para isso!");
            return;
        }

        if (InventoryUtils.isFull(author.getInventory(), Material.WRITTEN_BOOK)) {
            author.sendMessage("§cO seu inventário está cheio!");
            return;
        }

        if (!economy.withdrawPlayer(author, bluePrint.getPrice()).transactionSuccess()) {
            author.sendMessage("§cVocê precisa de " + bluePrint.getPrice() + " para comprar!");
            return;
        }

        author.sendMessage("§aVocê comprou o plano por " + bluePrint.getPrice() + "!");
        author.getInventory().addItem(bluePrint.getBook());
    }

    @CommandMapping(command = "listar",
            superCommand = "machine",
            args = 1,
            usage = "/maquinas listar")
    public void listar(Player author, String[] args) {
        author.sendMessage("§aLista de planos de construção:");
        for (BluePrint bluePrint : bluePrintService.getBluePrints()) {
            author.sendMessage("§b" + bluePrint.getName() + " -> " + bluePrint.getPrice());
        }
    }

}
