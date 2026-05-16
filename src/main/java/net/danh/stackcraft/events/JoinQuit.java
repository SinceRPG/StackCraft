package net.danh.stackcraft.events;

import net.danh.stackcraft.playerdata.PlayerData;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        new PlayerData(e.getPlayer()).loadData();
        if (!Items.toggle.containsKey(p.getUniqueId())) {
            Items.setGlobalToggle(p, Files.getConfig().getBoolean("default_toggle_item.all", false));
        }
        Items.toggle_craft.forEach((s, s2) -> {
            if (!Items.per_toggle_craft.containsKey(Items.getPlayerItemKey(p, s))) {
                Items.setPerToggle(p, s, Files.getConfig().getBoolean("default_toggle_item.per", false));
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PlayerData(e.getPlayer()).saveData();
        Items.removePlayer(e.getPlayer());
    }
}
