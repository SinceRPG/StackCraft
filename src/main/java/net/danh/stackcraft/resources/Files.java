package net.danh.stackcraft.resources;

import net.danh.stackcraft.StackCraft;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;

public class Files {

    public static void loadFiles() {
        SimpleConfigurationManager.get().build("", false, "config.yml", "message.yml", "playerData/example_data.yml");
    }

    public static void saveFiles() {
        SimpleConfigurationManager.get().save("config.yml", "message.yml");
    }

    public static void reloadFiles() {
        SimpleConfigurationManager.get().reload("config.yml", "message.yml");
        StackCraft.setIsAutoCraftSchedule(Files.getConfig().getBoolean("settings.auto_craft_schedule"));
    }

    public static FileConfiguration getConfig() {
        return SimpleConfigurationManager.get().get("config.yml");
    }

    public static FileConfiguration getMessage() {
        return SimpleConfigurationManager.get().get("message.yml");
    }
}
