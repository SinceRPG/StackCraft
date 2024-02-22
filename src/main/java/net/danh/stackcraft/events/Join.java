package net.danh.stackcraft.events;

import net.danh.stackcraft.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Items.toggle.containsKey(p)) {
            Items.toggle.put(p, false);
        }
    }
}
