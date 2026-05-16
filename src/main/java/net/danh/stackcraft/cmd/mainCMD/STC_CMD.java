package net.danh.stackcraft.cmd.mainCMD;

import net.danh.stackcraft.cmd.smallCMD.SmallToggle;
import net.danh.stackcraft.playerdata.PlayerData;
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
                Items.full_toggle_craft.forEach((alias, id) -> new SmallToggle(alias, id).removeCommand());
                Items.full_toggle_craft.clear();
                Items.toggle_craft.clear();
                Items.per_toggle_craft.clear();

                Files.reloadFiles();

                Bukkit.getOnlinePlayers().forEach(p -> new PlayerData(p).loadData());

                CraftCheck.loadCrafting();

                Chat.sendMessage(c, Files.getMessage().getString("admin.reload_files"));
            }

            if (c.hasPermission("stc.toggle") && c instanceof Player) {
                Player p = (Player) c;
                if (args[0].equalsIgnoreCase("toggle")) {
                    boolean newState = !Items.getGlobalToggle(p);
                    Items.setGlobalToggle(p, newState);

                    for (String item : Items.toggle_craft.keySet()) {
                        Items.setPerToggle(p, item, newState);
                    }

                    p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.toggle"))
                            .replace("#status#", Items.getStatus(p, null))));
                }

                if (args[0].equalsIgnoreCase("craft")) {
                    CraftCheck.addToQueue(p);
                    Chat.sendMessage(p, Files.getMessage().getString("user.manual_craft"));
                }

                if (args[0].equalsIgnoreCase("status")) {
                    Chat.sendMessage(p, Files.getMessage().getString("user.status_header")
                            .replace("#status#", Items.getStatus(p, null)));
                    Items.toggle_craft.keySet().stream().sorted().forEach(item -> Chat.sendMessage(p,
                            Files.getMessage().getString("user.status_item")
                                    .replace("#item#", Files.getConfig().getString("toggle." + item + ".display", item))
                                    .replace("#status#", Items.getStatus(p, item))));
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
                commands.add("craft");
                commands.add("status");
                commands.add("toggle");
            }
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
