package net.danh.stackcraft.resources;

import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.utils.Items;
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
        if (Files.getConfig().contains("toggle")) {
            for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                String id = Files.getConfig().getString("toggle." + item_list + ".command.alias");
                boolean register = Files.getConfig().getBoolean("toggle." + item_list + ".command.register");

                if (register && id != null) {
                    new SmallToggle(id, item_list).addCommand();
                }

                Items.toggle_craft.put(item_list, item_list);
                if (id != null) {
                    Items.full_toggle_craft.put(id, item_list);
                }
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
