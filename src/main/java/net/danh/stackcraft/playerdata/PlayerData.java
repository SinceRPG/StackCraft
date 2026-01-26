package net.danh.stackcraft.playerdata;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerData {

    private final Player p;
    private File playerdataFile;
    private FileConfiguration playerdata;

    public PlayerData(Player p) {
        this.p = p;
    }

    public void loadData() {
        create();
        if (!Items.toggle.containsKey(p)) {
            Items.toggle.put(p, get().getBoolean("toggle_item.all", Files.getConfig().getBoolean("default_toggle_item.all", false)));
        } else {
            Items.toggle.replace(p, get().getBoolean("toggle_item.all", Files.getConfig().getBoolean("default_toggle_item.all", false)));
        }
        Items.toggle_craft.forEach((s, s2) -> {
            if (!Items.per_toggle_craft.containsKey(p.getName() + "_" + s)) {
                Items.per_toggle_craft.put(p.getName() + "_" + s, get().getBoolean("toggle_item.per." + s, Files.getConfig().getBoolean("default_toggle_item.per", false)));
            } else {
                Items.per_toggle_craft.replace(p.getName() + "_" + s, get().getBoolean("toggle_item.per." + s, Files.getConfig().getBoolean("default_toggle_item.per", false)));
            }
        });
    }

    public void saveData() {
        create();
        get().set("name", p.getName());
        get().set("toggle_item.all", Items.toggle.get(p));
        Items.toggle_craft.forEach((s, s2) -> get().set("toggle_item.per." + s, Items.per_toggle_craft.get(p.getName() + "_" + s)));
        save();
        reload();
    }

    private String getFileName() {
        return "playerData/" + p.getName() + "_" + p.getUniqueId() + ".yml";
    }

    public Player getPlayer() {
        return p;
    }

    public void create() {
        playerdataFile = new File(StackCraft.get().getDataFolder(), getFileName());
        if (!playerdataFile.exists()) SimpleConfigurationManager.get().build("", true, getFileName());
        playerdata = new YamlConfiguration();

        try {
            playerdata.load(playerdataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration get() {
        return playerdata;
    }

    public void reload() {
        playerdata = YamlConfiguration.loadConfiguration(playerdataFile);
    }

    public void save() {
        try {
            playerdata.save(playerdataFile);
        } catch (IOException ignored) {
        }
    }
}
