package me.miko.universalspawn;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class UniversalSpawn extends JavaPlugin {

    private Location spawnLocation;
    private Config configManager;
    private static FoliaLib foliaLib;
    private static Logger logger;

    @Override
    public void onEnable() {
        configManager = new Config(this);
        foliaLib = new FoliaLib(this);
        logger = getLogger();

        logger.info("Loading Config");
        saveDefaultConfig();
        reloadConfig();

        configManager.loadSpawnLocation();

        logger.info("Registering Commands");
        getCommand("spawn").setExecutor(new SpawnCommand(this));

        logger.info("Registering Events");
        getServer().getPluginManager().registerEvents(new BukkitEvent(this), this);

        logger.info("Done.");
    }

    @Override
    public void onDisable() {
        if (foliaLib != null) {
            foliaLib.getImpl().cancelAllTasks();
            foliaLib = null;
        }
        logger = null;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Config getConfigManager() {
        return configManager;
    }

    public static FoliaLib getFoliaLib() {
        return foliaLib;
    }
}