package net.danh.stackcraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class SmallCommands extends Command implements CommandExecutor, TabCompleter {

    public SmallCommands(String id) {
        super(id);
    }

    public void addCommand() {
        if (getName() != null) {
            CommandMap commandMap;
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                commandMap.register(getName(), this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeCommand() {
        if (getName() != null) {
            Field cMap;
            Field knownCommands;
            try {
                cMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                cMap.setAccessible(true);
                knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommands.setAccessible(true);
                ((Map<String, Command>) knownCommands.get(cMap.get(Bukkit.getServer()))).remove(getName());
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
                this.unregister((CommandMap) cMap.get(Bukkit.getServer()));
                this.unregister(commandMap);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
