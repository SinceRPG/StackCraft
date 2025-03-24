package net.danh.stackcraft.resources;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

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
        for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
            String id = Files.getConfig().getString("toggle." + item_list + ".command.alias");
            boolean register = Files.getConfig().getBoolean("toggle." + item_list + ".command.register");
            if (register) {
                new SmallToggle(id, item_list).addCommand();
            }
        }
    }

    public static FileConfiguration getConfig() {
        return SimpleConfigurationManager.get().get("config.yml");
    }

    public static FileConfiguration getMessage() {
        return SimpleConfigurationManager.get().get("message.yml");
    }
}
