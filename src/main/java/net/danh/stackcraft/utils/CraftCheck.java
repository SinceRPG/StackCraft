package net.danh.stackcraft.utils;

import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class CraftCheck {

    public static boolean perToggleCraft(boolean perToggleCraft) {
        return perToggleCraft && SCAPI.isPremium();
    }

    public static void craftingCheck(Player p) {
        AtomicBoolean perToggleCraft = new AtomicBoolean(false);
        for (String itemCraft : Objects.requireNonNull(Files.getConfig().getConfigurationSection("craft")).getKeys(false)) {
            Items.toggle_craft.forEach((s, s2) -> {
                if (Items.per_toggle_craft.getOrDefault(p.getName() + "_" + s, false)) {
                    List<String> listCrafting = Files.getConfig().getStringList("toggle." + s + ".contain");
                    perToggleCraft.set(listCrafting.contains(itemCraft));
                }
            });
            if (Items.toggle.getOrDefault(p, false) ||  CraftCheck.perToggleCraft(perToggleCraft.get())) {
                Chat.debug(Items.toggle.getOrDefault(p, false) + "_"  + perToggleCraft.get());
                List<String> ingredient = Files.getConfig().getStringList("craft." + itemCraft + ".ingredient");
                HashMap<ItemStack, Integer> ingredients = Items.getIngredients(p, ingredient);
                ingredients.forEach((itemStack, integer) -> {
                    int craftAmount = ingredients.get(itemStack);
                    int playerAmount = Items.getPlayerAmount(p, itemStack);
                    if (playerAmount >= craftAmount) {
                        for (int i = 1; i <= playerAmount / craftAmount; i++) {
                            if (Items.checkCraftIngredient(p, ingredient, Files.getConfig().getBoolean("craft." + itemCraft + ".required_all", false))) {
                                p.getInventory().addItem(Items.generateItem(p, itemCraft));
                            }
                        }
                    }
                });
            }
        }
    }
}
