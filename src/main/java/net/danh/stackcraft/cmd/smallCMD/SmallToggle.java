package net.danh.stackcraft.cmd.smallCMD;

import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import net.danh.stackcraft.utils.SmallCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class SmallToggle extends SmallCommands {

    String item;

    public SmallToggle(String id, String item) {
        super(id);
        this.item = item;
    }

    @Override
    public boolean execute(@NotNull CommandSender c, @NotNull String s, @NotNull String[] args) {
        if (c instanceof Player p) {
            if (args.length == 0) {
                if (p.hasPermission("stc.toggle." + item)) {
                    toggleItem(p);
                }
            }
        } else if (c instanceof ConsoleCommandSender) {
            if (args.length == 1) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p != null) {
                    if (p.hasPermission("stc.toggle." + item)) {
                        toggleItem(p);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender c, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        execute(c, s, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    private void toggleItem(Player player) {
        Items.setPerToggle(player, item, !Items.getPerToggle(player, item));
        player.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.per_item"))
                .replace("#status#", Items.getStatus(player, item))
                .replace("#item#", Objects.requireNonNull(Files.getConfig().getString("toggle." + item + ".display")))));
    }
}
