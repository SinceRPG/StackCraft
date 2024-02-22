package net.danh.stackcraft;

import net.danh.stackcraft.cmd.STC_CMD;
import net.danh.stackcraft.events.BlockBreak;
import net.danh.stackcraft.events.Join;
import net.danh.stackcraft.resources.Files;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

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
        registerEvents(new BlockBreak(), new Join());
        new STC_CMD();
    }

    @Override
    public void onDisable() {
        Files.saveFiles();
    }

    private void registerEvents(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> {
            Bukkit.getPluginManager().registerEvents(listener, stackCraft);
            getLogger().info("Registered Listener " + listener);
        });
    }
}
