package net.danh.stackcraft.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class STC_PAPI extends PlaceholderExpansion {

    @NotNull
    @Override
    public String getIdentifier() {
        return "stc";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return StackCraft.get().getDescription().getAuthors().toString();
    }

    @NotNull
    @Override
    public String getVersion() {
        return StackCraft.get().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Nullable
    @Override
    public String onPlaceholderRequest(Player p, @NotNull String args) {
        if (args.startsWith("toggle_")) {
            String type = args.substring(7);
            return Items.per_toggle_craft.getOrDefault(p.getName() + "_" + type, false) ? Chat.normalColorize(Files.getMessage().getString("user.status.status_on")) : Chat.normalColorize(Files.getMessage().getString("user.status.status_off"));
        } else if (args.equalsIgnoreCase("toggle")) {
            return Items.toggle.getOrDefault(p, false) ? Chat.normalColorize(Files.getMessage().getString("user.status.status_on")) : Chat.normalColorize(Files.getMessage().getString("user.status.status_off"));
        }
        return "";
    }
}
