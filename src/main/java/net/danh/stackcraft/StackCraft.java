package net.danh.stackcraft;

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

public final class StackCraft extends JavaPlugin {
    private static StackCraft stackCraft;

    public static StackCraft getStackCraft() {
        return stackCraft;
    }

    @Override
    public void onEnable() {
        stackCraft = this;
        SimpleConfigurationManager.register(stackCraft);
        Files.loadFiles();
        registerEvents(new BlockBreak(), new JoinQuit(), new CommandPreprocess());
        new STC_CMD();
        for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
            String id = Files.getConfig().getString("toggle." + item_list + ".register_command");
            new SmallToggle(id, item_list).addCommand();
            Items.toggle_craft.put(item_list, item_list);
            Items.full_toggle_craft.put(id, item_list);
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
