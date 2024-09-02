package net.danh.stackcraft.events;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.utils.CraftCheck;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent e) {
        Bukkit.getScheduler().runTask(StackCraft.getStackCraft(), () -> CraftCheck.craftingCheck(e.getPlayer()));
    }
}
