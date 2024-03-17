package me.miko.universalspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalSpawn extends JavaPlugin {

    private Location spawnLocation;
    private Config configManager;

    @Override
    public void onEnable() {

        saveDefaultConfig();
        reloadConfig();

        configManager = new Config(this);
        configManager.loadSpawnLocation();

        getCommand("spawn").setExecutor(new SpawnCommand(this));

        Bukkit.getPluginManager().registerEvents(new BukkitEvent(this), this);

        getLogger().info("UniversalSpawn has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("UniversalSpawn has been disabled!");
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
}