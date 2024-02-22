package net.danh.stackcraft.cmd.smallCMD;

import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import net.danh.stackcraft.utils.SmallCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    public void execute(CommandSender c) {
        if (c instanceof Player) {
            Player p = (Player) c;
            if (c.hasPermission("stc.toggle." + item)) {
                Items.per_toggle_craft.replace(p.getName() + "_" + item, !Items.per_toggle_craft.get(p.getName() + "_" + item));
                p.sendMessage(Chat.colorize(Objects.requireNonNull(Files.getMessage().getString("user.per_item"))
                        .replace("#status#", Items.getPerStatus(p, item))
                        .replace("#item#", Objects.requireNonNull(Files.getConfig().getString("toggle." + item + ".display")))));
            }
        }
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        execute(commandSender);
        return true;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        execute(commandSender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
