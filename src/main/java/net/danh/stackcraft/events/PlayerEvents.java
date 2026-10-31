package net.danh.stackcraft.events;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.playerdata.PlayerData;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.CraftCheck;
import net.danh.stackcraft.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e) {
        CraftCheck.addToQueue(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickup(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        if (!e.isCancelled()) {
            CraftCheck.addToQueue(p);
            return;
        }

        if (e.getItem() == null) return;

        // Custom item plugins like ExecutableItems put the item into the player's
        // inventory, remove the item entity from the ground, and cancel the event.
        if (e.getItem().isDead() || !e.getItem().isValid()) {
            CraftCheck.addToQueue(p);
            return;
        }

        if (StackCraft.isExecutableItemsInstalled() && Items.isExecutableItem(e.getItem().getItemStack())) {
            CraftCheck.addToQueue(p);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMobDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            CraftCheck.addToQueue(e.getEntity().getKiller());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new PlayerData(p).loadData();

        Items.toggle.putIfAbsent(p.getUniqueId(), Files.getConfig().getBoolean("default_toggle_item.all", false));

        Items.toggle_craft.forEach((key, val) -> {
            String pKey = Items.getPlayerItemKey(p, key);
            if (!Items.per_toggle_craft.containsKey(pKey)) {
                Items.per_toggle_craft.put(pKey, Files.getConfig().getBoolean("default_toggle_item.per", false));
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PlayerData(e.getPlayer()).saveData();
        Items.removePlayer(e.getPlayer());
        CraftCheck.removeFromQueue(e.getPlayer());
    }
}
