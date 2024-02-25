package net.danh.stackcraft.cmd.mainCMD;

import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.CMDBase;
import net.danh.stackcraft.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class STC_CMD extends CMDBase {
    public STC_CMD() {
        super("stc");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (args.length == 1) {
            if (c.hasPermission("stc.admin")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Files.reloadFiles();
                    for (String id : Items.full_toggle_craft.keySet()) {
                        new SmallToggle(id, Items.full_toggle_craft.get(id)).removeCommand();
                    }
                    Items.full_toggle_craft.clear();
                    Items.toggle_craft.clear();
                    for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                        String id = Files.getConfig().getString("toggle." + item_list + ".register_command");
                        new SmallToggle(id, item_list).addCommand();
                        Items.toggle_craft.put(item_list, item_list);
                        Items.full_toggle_craft.put(id, item_list);
                        Items.toggle_craft.forEach((s, s2) -> Bukkit.getOnlinePlayers().forEach(p -> {
                            if (!Items.per_toggle_craft.containsKey(p.getName() + "_" + s)) {
                                Items.per_toggle_craft.put(p.getName() + "_" + s, Files.getConfig().getBoolean("default_toggle_item.per", false));
                            } else {
                                Items.per_toggle_craft.replace(p.getName() + "_" + s, Files.getConfig().getBoolean("default_toggle_item.per", false));
                            }
                        }));
                    }
                    Chat.sendMessage(c, Files.getMessage().getString("admin.reload_files"));
                }
            }
            if (c.hasPermission("stc.toggle")) {
                if (c instanceof Player) {
                    if (args[0].equalsIgnoreCase("toggle")) {
                        Player p = (Player) c;
                        Items.toggle.replace(p, !Items.toggle.get(p));
                        for (String item : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                            Items.per_toggle_craft.replace(p.getName() + "_" + item, Items.toggle.get(p));
                        }
                        p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.toggle")).replace("#status#", Items.getStatus(p, null))));
                    }
                }
            } else {
                Chat.sendMessage(c, Files.getMessage().getString("user.permission"));
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("stc.admin")) {
                commands.add("reload");
            }
            if (sender.hasPermission("stc.toggle") && sender instanceof Player) {
                commands.add("toggle");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
