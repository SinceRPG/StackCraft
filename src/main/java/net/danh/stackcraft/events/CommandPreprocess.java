package net.danh.stackcraft.events;

import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.utils.Items;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreprocess implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCommandExecute(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().substring(1);
        if (Items.full_toggle_craft.containsKey(cmd)) {
            new SmallToggle(cmd, Items.full_toggle_craft.get(cmd)).execute(event.getPlayer(), event.getMessage().replace("/", "").split(" "));
            event.setCancelled(true);
        }
    }

}
