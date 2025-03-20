package net.danh.stackcraft.utils;

import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class CraftCheck {

    private static HashMap<String, List<String>> listCrafting = new HashMap<>();
    private static List<String> itemCraft = new ArrayList<>();
    private static HashMap<String, List<String>> ingredient = new HashMap<>();
    private static HashMap<String, Boolean> requiredAll = new HashMap<>();

    public static boolean perToggleCraft(boolean perToggleCraft) {
        return perToggleCraft && SCAPI.isPremium();
    }

    public static void loadCrafting() {
        if (!listCrafting.isEmpty())
            listCrafting.clear();
        Items.toggle_craft.forEach((s, s2) -> {
            listCrafting.put(s, Files.getConfig().getStringList("toggle." + s + ".contain"));
        });
        loadItemCraft();
    }

    private static void loadItemCraft() {
        if (!itemCraft.isEmpty())
            itemCraft.clear();
        itemCraft.addAll(Objects.requireNonNull(Files.getConfig().getConfigurationSection("craft")).getKeys(false));
        loadIngredient();
        loadRequiredAll();
    }

    private static void loadIngredient() {
        if (!ingredient.isEmpty())
            ingredient.clear();
        for (String iCraft : itemCraft) {
            ingredient.put(iCraft, Files.getConfig().getStringList("craft." + iCraft + ".ingredient"));
        }
    }

    private static void loadRequiredAll() {
        if (!requiredAll.isEmpty())
            requiredAll.clear();
        for (String iCraft : itemCraft) {
            requiredAll.put(iCraft, Files.getConfig().getBoolean("craft." + iCraft + ".required_all", false));
        }
    }

    public static List<String> getItemCraft() {
        return itemCraft;
    }

    public static HashMap<String, List<String>> getListCrafting() {
        return listCrafting;
    }

    public static HashMap<String, List<String>> getIngredient() {
        return ingredient;
    }

    public static HashMap<String, Boolean> getRequiredAll() {
        return requiredAll;
    }

    public static void craftingCheck(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                AtomicBoolean perToggleCraft = new AtomicBoolean(false);
                for (String iCraft : itemCraft) {
                    Items.toggle_craft.forEach((s, s2) -> {
                        if (Items.per_toggle_craft.getOrDefault(p.getName() + "_" + s, false)) {
                            perToggleCraft.set(listCrafting.get(s).contains(iCraft));
                        }
                    });
                    if (Items.toggle.getOrDefault(p, false) || CraftCheck.perToggleCraft(perToggleCraft.get())) {
                        Chat.debug(Items.toggle.getOrDefault(p, false) + "_" + perToggleCraft.get());
                        List<String> ingredient = getIngredient().get(iCraft);
                        HashMap<ItemStack, Integer> ingredients = Items.getIngredients(p, ingredient);
                        ingredients.forEach((itemStack, integer) -> {
                            int craftAmount = ingredients.get(itemStack);
                            Chat.debug("craftAmount: " + craftAmount);
                            int playerAmount = Items.getPlayerAmount(p, itemStack);
                            Chat.debug("playerAmount: " + playerAmount);
                            Chat.debug("craft: " + playerAmount / craftAmount);
                            if (playerAmount >= craftAmount) {
                                for (int i = 1; i <= playerAmount / craftAmount; i++) {
                                    Chat.debug("craftTimes: " + i);
                                    if (Items.checkCraftIngredient(p, ingredient, getRequiredAll().get(iCraft))) {
                                        p.getInventory().addItem(Items.generateItem(p, iCraft));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }.runTaskAsynchronously(StackCraft.getStackCraft());
    }
}
