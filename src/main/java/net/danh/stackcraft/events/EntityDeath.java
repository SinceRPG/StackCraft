package net.danh.stackcraft.events;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.utils.CraftCheck;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            Bukkit.getScheduler().runTask(StackCraft.getStackCraft(), () -> CraftCheck.craftingCheck(e.getEntity().getKiller()));
        }
    }
}
