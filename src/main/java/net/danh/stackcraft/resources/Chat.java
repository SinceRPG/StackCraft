package net.danh.stackcraft.resources;

import net.danh.stackcraft.StackCraft;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Chat {

    public static void debug(String message) {
        if (Files.getConfig().getBoolean("settings.debug")) StackCraft.getStackCraft().getLogger().warning(message);
    }

    public static @NotNull Component colorize(String message) {
        return MiniMessage.miniMessage().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacySection().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacyAmpersand().deserialize(replaceColorCode(message))))));
    }

    public static List<Component> colorize(String... message) {
        return Arrays.stream(message).map(Chat::colorize).collect(Collectors.toList());
    }

    public static List<Component> colorize(@NotNull List<String> message) {
        return message.stream().map(Chat::colorize).collect(Collectors.toList());
    }

    public static @NotNull Component colorizewp(String message) {
        return MiniMessage.miniMessage().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacySection().deserialize(LegacyComponentSerializer.legacyAmpersand().serialize(LegacyComponentSerializer.legacyAmpersand().deserialize(replaceColorCode(Files.getConfig().getString("settings.prefix") + " " + message))))));
    }

    public static List<Component> colorizewp(String... message) {
        return Arrays.stream(message).map(Chat::colorizewp).collect(Collectors.toList());
    }

    public static String replaceColorCode(String message) {
        message = message.replace("§", "&");
        if (message.contains("&0")) message = message.replace("&0", "<black>");
        if (message.contains("&1")) message = message.replace("&1", "<dark_blue>");
        if (message.contains("&2")) message = message.replace("&2", "<dark_green>");
        if (message.contains("&3")) message = message.replace("&3", "<dark_aqua>");
        if (message.contains("&4")) message = message.replace("&4", "<dark_red>");
        if (message.contains("&5")) message = message.replace("&5", "<dark_purple>");
        if (message.contains("&6")) message = message.replace("&6", "<gold>");
        if (message.contains("&7")) message = message.replace("&7", "<gray>");
        if (message.contains("&8")) message = message.replace("&8", "<dark_gray>");
        if (message.contains("&9")) message = message.replace("&9", "<blue>");
        if (message.contains("&o")) message = message.replace("&o", "<italic>");
        if (message.contains("&a")) message = message.replace("&a", "<green>");
        if (message.contains("&b")) message = message.replace("&b", "<aqua>");
        if (message.contains("&c")) message = message.replace("&c", "<red>");
        if (message.contains("&d")) message = message.replace("&d", "<light_purple>");
        if (message.contains("&e")) message = message.replace("&e", "<yellow>");
        if (message.contains("&f")) message = message.replace("&f", "<white>");
        if (message.contains("&k")) message = message.replace("&k", "<obfuscated>");
        if (message.contains("&l")) message = message.replace("&l", "<bold>");
        if (message.contains("&m")) message = message.replace("&m", "<strikethrough>");
        if (message.contains("&n")) message = message.replace("&n", "<underlined>");
        if (message.contains("&r")) message = message.replace("&r", "<reset>");
        if (message.contains("&O")) message = message.replace("&O", "<italic>");
        if (message.contains("&A")) message = message.replace("&A", "<green>");
        if (message.contains("&B")) message = message.replace("&B", "<aqua>");
        if (message.contains("&C")) message = message.replace("&C", "<red>");
        if (message.contains("&D")) message = message.replace("&D", "<light_purple>");
        if (message.contains("&E")) message = message.replace("&E", "<yellow>");
        if (message.contains("&F")) message = message.replace("&F", "<white>");
        if (message.contains("&K")) message = message.replace("&K", "<obfuscated>");
        if (message.contains("&L")) message = message.replace("&L", "<bold>");
        if (message.contains("&M")) message = message.replace("&M", "<strikethrough>");
        if (message.contains("&N")) message = message.replace("&N", "<underlined>");
        if (message.contains("&R")) message = message.replace("&R", "<reset>");
        return message;
    }

    public static List<Component> colorizewp(@NotNull List<String> message) {
        return message.stream().map(Chat::colorizewp).collect(Collectors.toList());
    }

    public static void sendMessage(@NotNull Audience sender, String message) {
        sender.sendMessage(colorizewp(message));
    }

    public static void sendMessage(Audience sender, @NotNull List<String> messages) {
        messages.forEach(message -> sendMessage(sender, message));
    }
}

