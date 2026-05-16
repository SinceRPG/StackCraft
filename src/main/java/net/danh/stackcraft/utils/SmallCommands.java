package net.danh.stackcraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.*;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class SmallCommands extends Command implements CommandExecutor, TabCompleter {

    private static final String FALLBACK_PREFIX = "stackcraft";

    public SmallCommands(String id) {
        super(id);
    }

    public void addCommand() {
        if (getName() != null) {
            CommandMap commandMap;
            try {
                commandMap = getCommandMap();
                removeKnownCommand(commandMap, getName());
                commandMap.register(FALLBACK_PREFIX, this);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeCommand() {
        if (getName() != null) {
            try {
                CommandMap commandMap = getCommandMap();
                removeKnownCommand(commandMap, getName());
                removeKnownCommand(commandMap, FALLBACK_PREFIX + ":" + getName());
                this.unregister(commandMap);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
    }

    private CommandMap getCommandMap() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
    }

    @SuppressWarnings("unchecked")
    private void removeKnownCommand(CommandMap commandMap, String commandName) throws NoSuchFieldException, IllegalAccessException {
        Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
        knownCommands.setAccessible(true);
        Map<String, Command> commands = (Map<String, Command>) knownCommands.get(commandMap);
        Command command = commands.remove(commandName);
        if (command != null) {
            command.unregister(commandMap);
        }
    }
}
