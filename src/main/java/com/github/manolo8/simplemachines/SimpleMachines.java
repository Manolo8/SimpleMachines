package com.github.manolo8.simplemachines;

import com.github.manolo8.simplemachines.commands.CommandController;
import com.github.manolo8.simplemachines.commands.Commands;
import com.github.manolo8.simplemachines.controller.BluePrintController;
import com.github.manolo8.simplemachines.controller.MachineController;
import com.github.manolo8.simplemachines.database.DataBaseBuild;
import com.github.manolo8.simplemachines.database.dao.BluePrintDao;
import com.github.manolo8.simplemachines.database.dao.ChunkIDDao;
import com.github.manolo8.simplemachines.database.dao.MachineDao;
import com.github.manolo8.simplemachines.database.dao.impl.BluePrintDaoImpl;
import com.github.manolo8.simplemachines.database.dao.impl.MachineDaoSQL;
import com.github.manolo8.simplemachines.domain.fuel.FuelLoader;
import com.github.manolo8.simplemachines.domain.ingredient.IngredientLoader;
import com.github.manolo8.simplemachines.domain.solar.SolarLoader;
import com.github.manolo8.simplemachines.exception.DataBaseException;
import com.github.manolo8.simplemachines.listener.GlobalListener;
import com.github.manolo8.simplemachines.model.BluePrint;
import com.github.manolo8.simplemachines.model.BluePrintLoader;
import com.github.manolo8.simplemachines.service.BluePrintService;
import com.github.manolo8.simplemachines.service.ChunkIDService;
import com.github.manolo8.simplemachines.service.MachineService;
import com.github.manolo8.simplemachines.utils.book.BookFactory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SimpleMachines extends JavaPlugin {

    private static Logger logger;
    private Config config;
    private Random random;
    private Economy economy;
    private BookFactory bookFactory;
    private MachineDao machineDao;
    private ChunkIDService chunkIDService;
    private MachineService machineService;
    private BluePrintService bluePrintService;
    private MachineController machineController;
    private BluePrintController bluePrintController;
    private DataBaseBuild dataBaseBuild;

    private List<BluePrint> bluePrints;

    @Override
    public void onEnable() {
        this.config = new Config(this);
        this.random = new Random();
        logger = getLogger();

        if (!startFactories()) return;
        if (!setupEconomy()) return;
        if (!startDatabase()) return;
        startControllers();
        startDefaults();
        startCommandManager();
        checkForUpdates();
    }

    @Override
    public void onDisable() {
        if (machineService != null) machineService.saveAllMachines();
        if (dataBaseBuild != null) dataBaseBuild.close();
    }

    private List<BluePrintLoader> findLoaders() {
        List<BluePrintLoader> bluePrintLoaders = new ArrayList<>();
        bluePrintLoaders.add(new SolarLoader(random));
        bluePrintLoaders.add(new IngredientLoader(random));
        bluePrintLoaders.add(new FuelLoader(random));
        return bluePrintLoaders;
    }

    public boolean startFactories() {
        File file = new File(getDataFolder(), "book.txt");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            copy(getResource("book.txt"), file);
        }

        try {
            bookFactory = new BookFactory(file);
        } catch (IOException e) {
            ERROR(e.getMessage(), e.getStackTrace());
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        return true;
    }

    private boolean startDatabase() {
        try {
            BluePrintDao bluePrintDao = new BluePrintDaoImpl(getConfig(), random, findLoaders());
            bluePrints = bluePrintDao.loadAll();
            dataBaseBuild = new DataBaseBuild();
            dataBaseBuild.buildByConfig(this, config);
            bluePrintService = new BluePrintService(bluePrints, bookFactory);
            machineDao = new MachineDaoSQL(dataBaseBuild, bluePrintService);
            chunkIDService = new ChunkIDService((ChunkIDDao) machineDao);
            machineService = new MachineService(machineDao, bluePrintService, chunkIDService, config);

        } catch (DataBaseException e) {
            ERROR("Could not load the database. Something is wrong! The plugin will be disabled", e.getStackTrace());
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            economy = null;
            getLogger().info("This plugin need vault to start!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().info("This plugin need vault to start!");
            Bukkit.getPluginManager().disablePlugin(this);
            economy = null;
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private void startControllers() {
        machineController = new MachineController(machineService, random);
        bluePrintController = new BluePrintController(machineService, bluePrintService);
    }

    private void startDefaults() {
        getServer().getScheduler().runTaskTimer(this, bluePrintController, 5, 5);
        getServer().getScheduler().runTaskTimer(this, machineController, 20, 20);
        getServer().getPluginManager().registerEvents(new GlobalListener(bluePrintController, bluePrintService, machineService), this);
    }

    private void startCommandManager() {
        Commands commands = new Commands(bluePrintService, economy);
        CommandController commandController = new CommandController(commands);
        getCommand("machines").setExecutor(commandController);
    }

    private void checkForUpdates() {
        getLogger().info("Checking for updates...");
        try {
            String address = "http://hecato.com/test2.php?plugin=" +
                    this.getDescription().getName() + "&version="
                    + this.getDescription().getVersion();

            URL url = new URL(address);
            URLConnection conn = null;
            conn = url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder builder = new StringBuilder();

            String ln;
            while ((ln = reader.readLine()) != null) {
                builder.append(ln);
            }

            String[] values = builder.toString().split(";");

            double version = Double.parseDouble(values[0]);
            double currentVersion = Double.parseDouble(this.getDescription().getVersion());

            if (currentVersion >= version) {
                getLogger().info("The plugin is updated");
            } else {
                getLogger().warning("-------------------------------------------------------");
                getLogger().warning("Have a new version of the plugin, download in:");
                getLogger().warning(values[1]);
                getLogger().warning("-------------------------------------------------------");
            }

        } catch (Exception e) {
            e.printStackTrace();
            getLogger().info("Can't check for updates...");
        }
    }

    public static void ERROR(String message) {
        ERROR(message, null);
    }

    public static void ERROR(String message, StackTraceElement[] stackTraceElements) {
        logger.warning("");
        logger.warning("");
        logger.warning("====================================================");
        logger.warning("===          ERROR REPORT SIMPLEMACHINES:        ===");
        logger.warning("====================================================");
        logger.warning("= MESSAGE => ");
        for (String string : message.split("\n")) logger.warning(string);
        if (stackTraceElements != null) {
            logger.warning("StackTrace: ");
            for (StackTraceElement element : stackTraceElements) logger.warning(element.toString());
        }
        logger.warning("====================================================");
        logger.warning("===       ERROR REPORT SIMPLEMACHINES END.       ===");
        logger.warning("====================================================");
    }

    //https://bukkit.org/threads/bukkits-yaml-configuration-tutorial.42770/
    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
