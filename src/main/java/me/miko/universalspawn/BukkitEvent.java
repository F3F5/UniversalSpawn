package me.miko.universalspawn;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class BukkitEvent implements Listener {
    private final UniversalSpawn plugin;
    final FoliaLib foliaLib = UniversalSpawn.getFoliaLib();

    public BukkitEvent(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getConfigManager().isPlayerJoinEvent()) {
            Player player = event.getPlayer();
            foliaLib.getImpl().teleportAsync(player, plugin.getSpawnLocation());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (plugin.getConfigManager().isEntityDamageEvent()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                    return;
                }
                Location spawnLocation = plugin.getSpawnLocation();
                if (spawnLocation != null && spawnLocation.getWorld().equals(player.getWorld())) {
                    foliaLib.getImpl().teleportAsync(player, plugin.getSpawnLocation());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getConfigManager().isPlayerDeathEvent()) {
            Player player = event.getEntity();
            foliaLib.getImpl().runAtEntity(player, wrappedTask -> {
                player.spigot().respawn();
                foliaLib.getImpl().teleportAsync(player, plugin.getSpawnLocation());
            });
        }
    }
}
