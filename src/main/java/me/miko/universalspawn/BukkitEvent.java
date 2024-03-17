package me.miko.universalspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitEvent implements Listener {
    private UniversalSpawn plugin;

    public BukkitEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().isOnPlayerJoinEnabled()) {
            Player player = event.getPlayer();
            player.teleport(plugin.getSpawnLocation());
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (plugin.getConfigManager().isOnDamageEnabled()) {
            Entity entity = event.getEntity();
            Location spawnLocation = plugin.getSpawnLocation();
            World configuredWorld = spawnLocation.getWorld();

            if (!(entity instanceof Player) || configuredWorld == null || !entity.getWorld().equals(configuredWorld) || event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                return;
            }

            Player player = (Player) entity;
            player.teleport(spawnLocation);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfigManager().isOnDeathEnabled()) {
            Player player = event.getEntity();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.spigot().respawn();
                player.teleport(plugin.getSpawnLocation());
            }, 1);
        }
    }
}
