package net.danh.stackcraft.utils;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.resources.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class ServerScheduler {

    private static final List<Object> TASKS = new CopyOnWriteArrayList<>();
    private static final boolean FOLIA = hasMethod(Bukkit.getServer().getClass(), "getGlobalRegionScheduler");

    private ServerScheduler() {
    }

    public static boolean isFolia() {
        return FOLIA;
    }

    public static void runGlobalTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (FOLIA) {
            scheduleFoliaGlobalTimer(runnable, delayTicks, periodTicks);
            return;
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(StackCraft.get(), runnable, delayTicks, periodTicks);
        TASKS.add(task);
    }

    public static void runForPlayer(Player player, Runnable runnable) {
        if (player == null || !player.isOnline()) {
            return;
        }

        if (FOLIA) {
            scheduleFoliaEntityTask(player, runnable);
            return;
        }

        Bukkit.getScheduler().runTask(StackCraft.get(), runnable);
    }

    public static void cancelTasks() {
        for (Object task : TASKS) {
            try {
                Method cancel = task.getClass().getMethod("cancel");
                cancel.invoke(task);
            } catch (ReflectiveOperationException ex) {
                Chat.debug("Unable to cancel scheduled task: " + ex.getMessage());
            }
        }
        TASKS.clear();

        if (!FOLIA) {
            Bukkit.getScheduler().cancelTasks(StackCraft.get());
        }
    }

    private static void scheduleFoliaGlobalTimer(Runnable runnable, long delayTicks, long periodTicks) {
        try {
            Object scheduler = Bukkit.getServer().getClass().getMethod("getGlobalRegionScheduler").invoke(Bukkit.getServer());
            Method runAtFixedRate = scheduler.getClass().getMethod("runAtFixedRate", org.bukkit.plugin.Plugin.class, Consumer.class, long.class, long.class);
            Object task = runAtFixedRate.invoke(scheduler, StackCraft.get(), (Consumer<Object>) scheduledTask -> runnable.run(), delayTicks, periodTicks);
            TASKS.add(task);
        } catch (ReflectiveOperationException ex) {
            Chat.debug("Folia global scheduler failed, falling back to Bukkit scheduler: " + ex.getMessage());
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(StackCraft.get(), runnable, delayTicks, periodTicks);
            TASKS.add(task);
        }
    }

    private static void scheduleFoliaEntityTask(Player player, Runnable runnable) {
        try {
            Object scheduler = player.getClass().getMethod("getScheduler").invoke(player);
            Method execute = scheduler.getClass().getMethod("execute", org.bukkit.plugin.Plugin.class, Runnable.class, Runnable.class, long.class);
            execute.invoke(scheduler, StackCraft.get(), runnable, null, 1L);
        } catch (ReflectiveOperationException ex) {
            Chat.debug("Folia entity scheduler failed for " + player.getName() + ": " + ex.getMessage());
        }
    }

    private static boolean hasMethod(Class<?> type, String methodName) {
        try {
            type.getMethod(methodName);
            return true;
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }
}
