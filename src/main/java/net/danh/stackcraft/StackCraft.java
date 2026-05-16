package net.danh.stackcraft;

import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.cmd.mainCMD.STC_CMD;
import net.danh.stackcraft.events.PlayerEvents;
import net.danh.stackcraft.placeholder.STC_PAPI;
import net.danh.stackcraft.playerdata.PlayerData;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.CraftCheck;
import net.danh.stackcraft.utils.ServerScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class StackCraft extends JavaPlugin {
    private static StackCraft instance;
    private static boolean isMMOItemsInstalled = false;
    private static boolean isItemsAdderInstalled = false;
    private static boolean isOraxenInstalled = false;
    private static boolean isExecutableItemsInstalled = false;
    private static boolean isNexoInstalled = false;
    private static boolean isItemEditInstalled = false;
    private static boolean isMythicInstalled = false;

    public static StackCraft get() {
        return instance;
    }

    public static boolean isMMOItemsInstalled() {
        return isMMOItemsInstalled;
    }

    public static boolean isItemsAdderInstalled() {
        return isItemsAdderInstalled;
    }

    public static boolean isOraxenInstalled() {
        return isOraxenInstalled;
    }

    public static boolean isExecutableItemsInstalled() {
        return isExecutableItemsInstalled;
    }

    public static boolean isNexoInstalled() {
        return isNexoInstalled;
    }

    public static boolean isItemEditInstalled() {
        return isItemEditInstalled;
    }

    public static boolean isMythicInstalled() {
        return isMythicInstalled;
    }

    @Override
    public void onEnable() {
        instance = this;

        Files.loadFiles();

        checkDependencies();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new STC_PAPI().register();
        }

        new STC_CMD();
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);

        Files.reloadFiles();
        CraftCheck.loadCrafting();

        long queueInterval = Math.max(1L, Files.getConfig().getLong("settings.queue_interval_ticks", 5L));
        ServerScheduler.runGlobalTimer(CraftCheck::processQueue, queueInterval, queueInterval);

        if (Files.getConfig().getBoolean("settings.auto_craft_schedule")) {
            long scheduleInterval = Math.max(1L, Files.getConfig().getLong("settings.auto_craft_schedule_interval_ticks", 20L));
            ServerScheduler.runGlobalTimer(() -> Bukkit.getOnlinePlayers().forEach(CraftCheck::addToQueue), scheduleInterval, scheduleInterval);
        }

        if (!SCAPI.isPremium()) {
            getLogger().log(Level.INFO, "Running Free Version - Advanced Hooks Disabled.");
        }
    }

    @Override
    public void onDisable() {
        Files.saveFiles();
        ServerScheduler.cancelTasks();
        Bukkit.getOnlinePlayers().forEach(player -> new PlayerData(player).saveData());
        CraftCheck.clearQueue();
    }

    private void checkDependencies() {
        if (getServer().getPluginManager().isPluginEnabled("MMOItems")) isMMOItemsInstalled = true;
        if (getServer().getPluginManager().isPluginEnabled("ItemEdit")) isItemEditInstalled = true;

        if (SCAPI.isPremium()) {
            if (getServer().getPluginManager().isPluginEnabled("ItemsAdder")) isItemsAdderInstalled = true;
            if (getServer().getPluginManager().isPluginEnabled("Oraxen")) isOraxenInstalled = true;
            if (getServer().getPluginManager().isPluginEnabled("ExecutableItems")) isExecutableItemsInstalled = true;
            if (getServer().getPluginManager().isPluginEnabled("Nexo")) isNexoInstalled = true;
            if (getServer().getPluginManager().isPluginEnabled("MythicMobs")) isMythicInstalled = true;
        }
    }
}
