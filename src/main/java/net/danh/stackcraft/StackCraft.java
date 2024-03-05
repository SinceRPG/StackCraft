package net.danh.stackcraft;

import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.cmd.mainCMD.STC_CMD;
import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.events.BlockBreak;
import net.danh.stackcraft.events.CommandPreprocess;
import net.danh.stackcraft.events.JoinQuit;
import net.danh.stackcraft.playerdata.PlayerData;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class StackCraft extends JavaPlugin {
    private static StackCraft stackCraft;
    private static boolean isMMOItemsInstalled = false;
    private static boolean isItemsAdderInstalled = false;
    private static boolean isOraxenInstalled = false;
    private static boolean isExecutableItemsInstalled = false;
    private static boolean isMythicInstalled = false;

    public static StackCraft getStackCraft() {
        return stackCraft;
    }

    public static boolean isIsMMOItemsInstalled() {
        return isMMOItemsInstalled;
    }

    public static boolean isIsItemsAdderInstalled() {
        return isItemsAdderInstalled;
    }

    public static boolean isIsOraxenInstalled() {
        return isOraxenInstalled;
    }

    public static boolean isExecutableItemsInstalled() {
        return isExecutableItemsInstalled;
    }

    public static boolean isIsMythicInstalled() {
        return isMythicInstalled;
    }

    @Override
    public void onEnable() {
        stackCraft = this;
        SimpleConfigurationManager.register(stackCraft);
        Files.loadFiles();
        registerEvents(new BlockBreak(), new JoinQuit(), new CommandPreprocess());
        new STC_CMD();
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("MMOItems") != null) {
            isMMOItemsInstalled = true;
            getLogger().log(Level.INFO, "Compatible with MMOItems");
        }
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("ItemsAdder") != null) {
            isItemsAdderInstalled = true;
            getLogger().log(Level.INFO, "Compatible with ItemsAdder");
        }
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("Oraxen") != null) {
            isOraxenInstalled = true;
            getLogger().log(Level.INFO, "Compatible with Oraxen");
        }
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("ExecutableItems") != null) {
            isExecutableItemsInstalled = true;
            getLogger().log(Level.INFO, "Compatible with ExecutableItems");
        }
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            isMythicInstalled = true;
            getLogger().log(Level.INFO, "Compatible with MythicMobs");
        }
        for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
            String id = Files.getConfig().getString("toggle." + item_list + ".command.alias");
            boolean register = Files.getConfig().getBoolean("toggle." + item_list + ".command.register");
            if (register) {
                new SmallToggle(id, item_list).addCommand();
            }
            Items.toggle_craft.put(item_list, item_list);
            Items.full_toggle_craft.put(id, item_list);
        }
        if (!SCAPI.isPremium()) {
            getLogger().log(Level.INFO, "You are using the non-premium version so you CANNOT use some features such as:");
            getLogger().log(Level.INFO, "- Being able to craft custom items (MMOItems, ItemsAdder, Oraxen, ExecutableItems)");
            getLogger().log(Level.INFO, "- Being able to toggle on/off craft per item");
            getLogger().log(Level.INFO, "- Being able to toggle autocraft by put item toggle craft in inventory");
        }
    }

    @Override
    public void onDisable() {
        Files.saveFiles();
        Bukkit.getOnlinePlayers().forEach(player -> new PlayerData(player).saveData());
    }

    private void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, stackCraft);
            getLogger().info("Registered Listener " + listener);
        });
    }
}
