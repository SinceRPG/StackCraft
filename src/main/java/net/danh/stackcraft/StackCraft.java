package net.danh.stackcraft;

import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.cmd.mainCMD.STC_CMD;
import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.events.BlockBreak;
import net.danh.stackcraft.events.CommandPreprocess;
import net.danh.stackcraft.events.EntityDeath;
import net.danh.stackcraft.events.JoinQuit;
import net.danh.stackcraft.placeholder.STC_PAPI;
import net.danh.stackcraft.playerdata.PlayerData;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.CraftCheck;
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
    private static boolean isNexoInstalled = false;
    private static boolean isItemEditInstalled = false;
    private static boolean isMythicInstalled = false;
    private static boolean isAutoCraftSchedule = false;

    public static StackCraft getStackCraft() {
        return stackCraft;
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

    public static boolean isMythicInstalled() {
        return isMythicInstalled;
    }

    public static boolean isNexoInstalled() {
        return isNexoInstalled;
    }

    public static boolean isItemEditInstalled() {
        return isItemEditInstalled;
    }

    public static boolean isIsAutoCraftSchedule() {
        return isAutoCraftSchedule;
    }

    public static void setIsAutoCraftSchedule(boolean isAutoCraftSchedule) {
        StackCraft.isAutoCraftSchedule = isAutoCraftSchedule;
    }

    @Override
    public void onEnable() {
        stackCraft = this;
        SimpleConfigurationManager.register(stackCraft);
        Files.loadFiles();
        Files.reloadFiles();
        registerEvents(new BlockBreak(), new EntityDeath(), new JoinQuit(), new CommandPreprocess());
        new STC_CMD();
        if (getServer().getPluginManager().getPlugin("MMOItems") != null) {
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
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("Nexo") != null) {
            isNexoInstalled = true;
            getLogger().log(Level.INFO, "Compatible with Nexo");
        }
        if (getServer().getPluginManager().getPlugin("ItemEdit") != null) {
            isItemEditInstalled = true;
            getLogger().log(Level.INFO, "Compatible with ItemEdit");
        }
        if (SCAPI.isPremium() && getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            isMythicInstalled = true;
            getLogger().log(Level.INFO, "Compatible with MythicMobs");
        }
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new STC_PAPI().register();
            getLogger().log(Level.INFO, "Compatible with PlaceholderAPI");
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
            getLogger().log(Level.INFO, "- Being able to craft custom items (ItemsAdder, Oraxen, ExecutableItems, MythicMobs)");
            getLogger().log(Level.INFO, "- Being able to toggle on/off craft per item");
        }
        isAutoCraftSchedule = Files.getConfig().getBoolean("settings.auto_craft_schedule");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(stackCraft, () -> {
            if (isAutoCraftSchedule) {
                getServer().getOnlinePlayers().forEach(CraftCheck::craftingCheck);
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        Files.reloadFiles();
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
