package me.miko.universalspawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitEvent implements Listener {
    private UniversalSpawn plugin;

    public BukkitEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().isOnPlayerJoinEnabled()) {
            Player player = event.getPlayer();
            player.teleport(plugin.getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (plugin.getConfigManager().isOnDamageEnabled()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    Location spawnLocation = plugin.getSpawnLocation();
                    if (spawnLocation != null && player.getWorld().getName().equals(spawnLocation.getWorld().getName())) {
                        player.teleport(spawnLocation);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfigManager().isOnDeathEnabled()) {
            Player player = event.getEntity();
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.spigot().respawn();
                player.teleport(plugin.getSpawnLocation());
            }, 1);
        }
    }
}
