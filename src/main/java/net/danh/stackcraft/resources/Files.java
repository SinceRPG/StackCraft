package net.danh.stackcraft.resources;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.utils.ConfigUtils;
import net.danh.stackcraft.utils.Items;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Files {

    private static ConfigUtils config;
    private static ConfigUtils message;

    public static void loadFiles() {
        config = new ConfigUtils(StackCraft.get(), "config.yml");
        message = new ConfigUtils(StackCraft.get(), "message.yml");

        // Tạo file example_data nếu chưa tồn tại
        new ConfigUtils(StackCraft.get(), "playerData/example_data.yml");
    }

    public static void saveFiles() {
        config.save();
        message.save();
    }

    public static void reloadFiles() {
        config.reload();
        message.reload();

        if (getConfig().contains("toggle")) {
            for (String item_list : Objects.requireNonNull(getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                String id = getConfig().getString("toggle." + item_list + ".command.alias");
                boolean register = getConfig().getBoolean("toggle." + item_list + ".command.register");

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

    // Trả về FileConfiguration để các class khác không bị lỗi
    public static FileConfiguration getConfig() {
        return config.getConfig();
    }

    public static FileConfiguration getMessage() {
        return message.getConfig();
    }
}