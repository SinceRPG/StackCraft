package net.danh.stackcraft.events;

import net.danh.stackcraft.playerdata.PlayerData;
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
        if (!Items.toggle.containsKey(p)) {
            Items.toggle.put(p, false);
        }
        Items.toggle_craft.forEach((s, s2) -> {
            if (!Items.per_toggle_craft.containsKey(p.getName() + "_" + s)) {
                Items.per_toggle_craft.put(p.getName() + "_" + s, true);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        new PlayerData(e.getPlayer()).saveData();
    }
}
