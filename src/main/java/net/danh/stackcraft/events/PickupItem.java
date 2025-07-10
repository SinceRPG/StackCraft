package net.danh.stackcraft.events;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.utils.CraftCheck;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PickupItem implements Listener {

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e) {
        Bukkit.getScheduler().runTask(StackCraft.getStackCraft(), () -> CraftCheck.craftingCheck(e.getPlayer()));
    }
}
