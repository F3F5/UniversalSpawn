package me.miko.universalspawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final UniversalSpawn plugin;

    public Config(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    public void loadSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        World world = plugin.getServer().getWorld(config.getString("spawn.world"));
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        plugin.setSpawnLocation(new Location(world, x, y, z, yaw, pitch));
    }

    public void saveSpawnLocation(Location location) {
        FileConfiguration config = plugin.getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        plugin.saveConfig();
    }

    public boolean TeleportOnJoin() {
        return plugin.getConfig().getBoolean("teleport-on-join.enabled", true);
    }

    public boolean TeleportOutOfVoid() {
        return plugin.getConfig().getBoolean("teleport-out-of-void.enabled", true) &&
                plugin.getConfig().getBoolean("teleport-out-of-void.check-height", true);
    }

    public boolean TeleportOnDeath() {
        return plugin.getConfig().getBoolean("teleport-on-death.enabled", true);
    }
}