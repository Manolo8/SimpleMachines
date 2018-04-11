package com.github.manolo8.simplemachines.listener;

import com.github.manolo8.simplemachines.Language;
import com.github.manolo8.simplemachines.controller.BluePrintController;
import com.github.manolo8.simplemachines.domain.collector.CollectorMachine;
import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.service.MachineService;
import com.github.manolo8.simplemachines.utils.Face;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("unused")
public class GlobalListener implements Listener {

    private BluePrintController bluePrintController;
    private BluePrintService bluePrintService;
    private MachineService machineService;
    private Language language;

    public GlobalListener(BluePrintController bluePrintController,
                          BluePrintService bluePrintService,
                          MachineService machineService,
                          Language language) {
        this.bluePrintController = bluePrintController;
        this.bluePrintService = bluePrintService;
        this.machineService = machineService;
        this.language = language;
    }

    @EventHandler
    public void playerInteractWithBookEvent(PlayerInteractEvent event) {
        ItemStack hand = event.getPlayer().getItemInHand();

        if (hand.getType() != Material.WRITTEN_BOOK || event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        ItemMeta meta = hand.getItemMeta();
        BlockFace face = Face.getFace(event.getPlayer());

        if (!bluePrintController.isValidMachine(meta)) return;

        Block block = event.getClickedBlock();

        if (block.getType() != Material.IRON_BLOCK) return;

        Location loc = block.getLocation();

//        int ironBlocks = bluePrintController.countIronBlocks(loc);
//
//        if (ironBlocks != 9) {
//            event.getPlayer().sendMessage("§cTem que ter mais " + (9 - ironBlocks) + " blocos de ferro na base!");
//            return;
//        }

        if (!bluePrintController.isValidPosition(loc)) {
            event.getPlayer().sendMessage("§cNão pode haver outros blocos na área!");
            return;
        }

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, event.getPlayer());
        Bukkit.getServer().getPluginManager().callEvent(blockBreakEvent);

        if (blockBreakEvent.isCancelled()) {
            event.getPlayer().sendMessage("§cVocê não tem permissão aqui.");
            return;
        }

        String machineName = meta.getDisplayName();

        if (!bluePrintController.addMachineCreation(machineName, event.getPlayer(), face, event.getClickedBlock().getLocation())) {
            event.getPlayer().sendMessage("§cVocê não tem materiais suficientes!");
            block.setType(Material.IRON_BLOCK);
        } else {
            meta.setDisplayName("§cManual " + machineName);
            hand.setItemMeta(meta);
        }
    }

    @EventHandler
    public void onChunkLoadEvent(ChunkLoadEvent event) {
        machineService.loadMachines(event.getChunk());
    }

    @EventHandler(ignoreCancelled = true)
    public void onChunkUnloadEvent(ChunkUnloadEvent event) {
        machineService.unloadFromChunk(event.getChunk());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent e) {
        Machine machine = machineService.getMachine(e.getBlock().getLocation());
        if (machine == null) return;

        Player player = e.getPlayer();
        ItemStack stack = player.getItemInHand();

        if (stack.getType() != Material.GOLD_PICKAXE) {
            e.setCancelled(true);
            player.sendMessage(language.getString("machine.break.message"));
            return;
        }

        machineService.deleteMachine(machine);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void blockGrowEvent(BlockGrowEvent event) {

        CollectorMachine machine = machineService.getCollectorMachine(event.getBlock());

        if (machine == null) return;

        machine.checkBlock(event.getBlock(), event.getNewState());
    }


    @EventHandler
    public void chest(InventoryMoveItemEvent event) {
        check(event.getDestination());
        check(event.getInitiator());
    }

    @EventHandler
    public void chest(InventoryClickEvent event) {
        check(event.getClickedInventory());

        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            check(event.getInventory());
        }
    }

    private void check(Inventory inventory) {
        if (inventory == null) return;
        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return;
        Location loc = null;

        if (holder instanceof DoubleChest) {
            loc = ((DoubleChest) holder).getLocation();
        }

        if (holder instanceof Chest) {
            loc = ((Chest) holder).getLocation();
        }

        if (loc == null) return;

        Machine machine = machineService.getMachine(loc);

        if (machine == null) return;

        if (!machine.isWorking()) machine.setChanged(true);
    }
}
