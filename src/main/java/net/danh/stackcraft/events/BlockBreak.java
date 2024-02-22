package net.danh.stackcraft.events;

import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(@NotNull BlockBreakEvent e) {
        Player p = e.getPlayer();
        for (String item_Craft : Objects.requireNonNull(Files.getConfig().getConfigurationSection("craft")).getKeys(false)) {
            if (Items.toggle.getOrDefault(p, false) || Items.checkToggleItem(p, item_Craft)) {
                List<String> ingredient = Files.getConfig().getStringList("craft." + item_Craft + ".ingredient");
                HashMap<ItemStack, Integer> ingredients = Items.getIngredients(ingredient);
                ingredients.forEach((itemStack, integer) -> {
                    int craftAmount = ingredients.get(itemStack);
                    int playerAmount = Items.getPlayerAmount(p, itemStack);
                    if (playerAmount >= craftAmount) {
                        for (int i = 1; i <= playerAmount / craftAmount; i++) {
                            if (Items.checkCraftIngredient(p, ingredient)) {
                                p.getInventory().addItem(Items.generateItem(item_Craft));
                            }
                        }
                    }
                });
            }
        }
    }
}
