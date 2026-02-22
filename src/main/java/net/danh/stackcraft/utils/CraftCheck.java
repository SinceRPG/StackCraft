package net.danh.stackcraft.utils;

import net.danh.stackcraft.api.SCAPI;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CraftCheck {

    private static final List<Recipe> cachedRecipes = new ArrayList<>();
    private static final Set<UUID> playerQueue = ConcurrentHashMap.newKeySet();

    public static void loadCrafting() {
        cachedRecipes.clear();
        try {
            if (!Files.getConfig().contains("toggle")) return;

            for (String toggleId : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
                List<String> containList = Files.getConfig().getStringList("toggle." + toggleId + ".contain");

                for (String itemCraftKey : containList) {
                    if (!Files.getConfig().contains("craft." + itemCraftKey)) continue;

                    boolean requiredAll = Files.getConfig().contains("craft." + itemCraftKey + ".required_all") && Files.getConfig().getBoolean("craft." + itemCraftKey + ".required_all", false);
                    List<String> ingredientStrings = Files.getConfig().getStringList("craft." + itemCraftKey + ".ingredient");

                    ItemStack resultItem = Items.parseItem(itemCraftKey);
                    if (resultItem == null) {
                        Chat.debug("Invalid result item in config: " + itemCraftKey);
                        continue;
                    }

                    List<Ingredient> ingredients = new ArrayList<>();
                    for (String ingStr : ingredientStrings) {
                        ItemStack ingItem = Items.parseItem(ingStr);
                        if (ingItem != null) {
                            int amount = Items.parseAmount(ingStr);
                            ingredients.add(new Ingredient(ingItem, amount));
                        }
                    }

                    if (!ingredients.isEmpty()) {
                        cachedRecipes.add(new Recipe(toggleId, resultItem, ingredients, requiredAll));
                    }
                }
            }
            Chat.debug("Loaded " + cachedRecipes.size() + " auto-craft recipes.");
        } catch (Exception e) {
            Chat.debug("Error loading crafting recipes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addToQueue(Player p) {
        if (p != null && p.isOnline()) {
            playerQueue.add(p.getUniqueId());
        }
    }

    public static void processQueue() {
        if (playerQueue.isEmpty()) return;

        Iterator<UUID> iterator = playerQueue.iterator();
        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                checkPlayerCrafts(p);
            }
            iterator.remove();
        }
    }

    private static void checkPlayerCrafts(Player p) {
        boolean globalToggle = Items.toggle.getOrDefault(p, false);

        for (Recipe recipe : cachedRecipes) {
            String toggleId = recipe.toggleId();
            boolean perToggle = Items.per_toggle_craft.getOrDefault(p.getName() + "_" + toggleId, false);

            if (!globalToggle && !(SCAPI.isPremium() && perToggle)) continue;

            if (!p.hasPermission("stc.toggle") && !p.hasPermission("stc.toggle." + toggleId)) {
                if (perToggle) Items.per_toggle_craft.put(p.getName() + "_" + toggleId, false);
                continue;
            }

            processCraftingRecipe(p, recipe);
        }
    }

    private static void processCraftingRecipe(Player p, Recipe recipe) {
        if (recipe.requiredAll()) {
            int maxCrafts = Integer.MAX_VALUE;
            for (Ingredient ing : recipe.ingredients()) {
                int playerHas = Items.getPlayerAmount(p, ing.item);
                if (playerHas < ing.amount) {
                    maxCrafts = 0;
                    break;
                }
                maxCrafts = Math.min(maxCrafts, playerHas / ing.amount);
            }

            if (maxCrafts > 0) {
                performCraft(p, recipe.result(), recipe.ingredients(), maxCrafts);
            }

        } else {
            for (Ingredient ing : recipe.ingredients()) {
                int playerHas = Items.getPlayerAmount(p, ing.item);
                int crafts = playerHas / ing.amount;

                if (crafts > 0) {
                    performCraft(p, recipe.result(), Collections.singletonList(ing), crafts);
                }
            }
        }
    }

    private static void performCraft(Player p, ItemStack result, List<Ingredient> ingredientsToRemove, int times) {

        for (Ingredient ing : ingredientsToRemove) {
            Items.removeItems(p, ing.item, ing.amount * times);
        }
        ItemStack reward = result.clone();
        reward.setAmount(result.getAmount() * times);

        HashMap<Integer, ItemStack> leftovers = p.getInventory().addItem(reward);

        if (!leftovers.isEmpty()) {
            leftovers.values().forEach(item ->
                    p.getWorld().dropItemNaturally(p.getLocation(), item)
            );
        }

        p.updateInventory();
    }

    private record Recipe(String toggleId, ItemStack result, List<Ingredient> ingredients, boolean requiredAll) {
    }

    private record Ingredient(ItemStack item, int amount) {
    }
}