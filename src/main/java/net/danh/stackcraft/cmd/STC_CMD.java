package net.danh.stackcraft.cmd;

import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
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
                    Chat.sendMessage(c, Files.getMessage().getString("admin.reload_files"));
                }
            }
            if (c.hasPermission("stc.toggle")) {
                if (c instanceof Player) {
                    if (args[0].equalsIgnoreCase("toggle")) {
                        Player p = (Player) c;
                        Items.toggle.replace(p, !Items.toggle.get(p));
                        p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.toggle"))
                                .replace("#status#", Items.getStatus(p))));
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
