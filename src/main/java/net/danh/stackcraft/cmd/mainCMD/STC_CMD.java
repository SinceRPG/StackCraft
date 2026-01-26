package net.danh.stackcraft.cmd.mainCMD;

import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.CMDBase;
import net.danh.stackcraft.utils.CraftCheck;
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
            if (args[0].equalsIgnoreCase("help")) {
                if (c.hasPermission("stc.admin")) {
                    Chat.sendMessage(c, Files.getMessage().getStringList("admin.help"));
                }
                Chat.sendMessage(c, Files.getMessage().getStringList("user.help"));
            }

            if (c.hasPermission("stc.admin") && args[0].equalsIgnoreCase("reload")) {
                Files.reloadFiles();

                Items.full_toggle_craft.forEach((alias, id) -> new SmallToggle(alias, id).removeCommand());
                Items.full_toggle_craft.clear();
                Items.toggle_craft.clear();

                if (Files.getConfig().contains("toggle")) {
                    for (String item_list : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                        String id = Files.getConfig().getString("toggle." + item_list + ".command.alias");
                        boolean register = Files.getConfig().getBoolean("toggle." + item_list + ".command.register");

                        if (register && id != null) {
                            new SmallToggle(id, item_list).addCommand();
                        }

                        Items.toggle_craft.put(item_list, item_list);
                        if (id != null) Items.full_toggle_craft.put(id, item_list);
                    }
                }

                boolean defaultPer = Files.getConfig().getBoolean("default_toggle_item.per", false);
                Items.toggle_craft.forEach((key, val) -> Bukkit.getOnlinePlayers().forEach(p -> {
                    Items.per_toggle_craft.putIfAbsent(p.getName() + "_" + key, defaultPer);
                }));

                CraftCheck.loadCrafting();

                Chat.sendMessage(c, Files.getMessage().getString("admin.reload_files"));
            }

            if (c.hasPermission("stc.toggle") && c instanceof Player) {
                if (args[0].equalsIgnoreCase("toggle")) {
                    Player p = (Player) c;
                    boolean newState = !Items.toggle.getOrDefault(p, false);
                    Items.toggle.put(p, newState);

                    for (String item : Items.toggle_craft.keySet()) {
                        Items.per_toggle_craft.put(p.getName() + "_" + item, newState);
                    }

                    p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.toggle"))
                            .replace("#status#", Items.getStatus(p, null))));
                }
            } else if (!c.hasPermission("stc.toggle") && !c.hasPermission("stc.admin")) {
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