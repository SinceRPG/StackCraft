package net.danh.stackcraft.utils;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import dev.lone.itemsadder.api.CustomStack;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import net.Indyuce.mmoitems.MMOItems;
import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.resources.Number;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Items {

    public static HashMap<Player, Boolean> toggle = new HashMap<>();
    public static HashMap<String, String> toggle_craft = new HashMap<>();
    public static HashMap<String, String> full_toggle_craft = new HashMap<>();
    public static HashMap<String, Boolean> per_toggle_craft = new HashMap<>();

    public static String getStatus(Player p, String item) {
        if (item == null) {
            if (toggle.get(p)) {
                return Files.getMessage().getString("user.status.status_on");
            } else return Files.getMessage().getString("user.status.status_off");
        } else {
            if (Items.per_toggle_craft.get(p.getName() + "_" + item)) {
                return Files.getMessage().getString("user.status.status_on");
            } else return Files.getMessage().getString("user.status.status_off");
        }
    }

    public static boolean checkToggleItem(@NotNull Player p, String itemCraft) {
        AtomicBoolean checkToggle = new AtomicBoolean(false);
        for (String toggle_item : Objects.requireNonNull(Files.getConfig().getConfigurationSection("toggle")).getKeys(false)) {
            List<String> itemCraftList = Files.getConfig().getStringList("toggle." + toggle_item + ".contain");
            String[] toggleItemSplit = Objects.requireNonNull(Files.getConfig().getString("toggle." + toggle_item + ".item")).split(";");
            if (StackCraft.isIsMMOItemsInstalled() && toggleItemSplit[0].equalsIgnoreCase("MMOITEMS")) {
                ItemStack itemStack = MMOItems.plugin.getItem(toggleItemSplit[1], toggleItemSplit[2]);
                if (itemStack != null) {
                    if (getPlayerAmount(p, itemStack) >= 1) {
                        checkToggle.set(itemCraftList.contains(itemCraft));
                    }
                }
            } else if (StackCraft.isIsItemsAdderInstalled() && toggleItemSplit[0].equalsIgnoreCase("ITEMSADDER")) {
                if (CustomStack.isInRegistry(toggleItemSplit[1])) {
                    CustomStack stack = CustomStack.getInstance(toggleItemSplit[1]);
                    if (stack != null) {
                        ItemStack itemStack = stack.getItemStack();
                        if (getPlayerAmount(p, itemStack) >= 1) {
                            checkToggle.set(itemCraftList.contains(itemCraft));
                        }
                    }
                }
            } else if (StackCraft.isIsOraxenInstalled() && toggleItemSplit[0].equalsIgnoreCase("ORAXEN")) {
                if (OraxenItems.exists(toggleItemSplit[1])) {
                    ItemBuilder stack = OraxenItems.getItemById(toggleItemSplit[1]);
                    if (stack != null) {
                        ItemStack itemStack = stack.build();
                        if (getPlayerAmount(p, itemStack) >= 1) {
                            checkToggle.set(itemCraftList.contains(itemCraft));
                        }
                    }
                }
            } else if (StackCraft.isExecutableItemsInstalled() && toggleItemSplit[0].equalsIgnoreCase("EXECUTABLEITEMS")) {
                if (ExecutableItemsAPI.getExecutableItemsManager().isValidID(toggleItemSplit[1])) {
                    Optional<ExecutableItemInterface> stack = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(toggleItemSplit[1]);
                    if (stack.isPresent()) {
                        ItemStack itemStack = stack.get().buildItem(1, Optional.of(p));
                        if (getPlayerAmount(p, itemStack) >= 1) {
                            checkToggle.set(itemCraftList.contains(itemCraft));
                        }
                    }
                }
            } else if (StackCraft.isIsMythicInstalled() && toggleItemSplit[0].equalsIgnoreCase("MYTHICMOBS")) {
                Optional<MythicItem> stack = MythicBukkit.inst().getItemManager().getItem(toggleItemSplit[1]);
                stack.ifPresent(mythicItem -> {
                    ItemStack itemStack = mythicItem.getCachedBaseItem();
                    if (getPlayerAmount(p, itemStack) >= 1) {
                        checkToggle.set(itemCraftList.contains(itemCraft));
                    }
                });
            }
        }
        return checkToggle.get();
    }

    public static ItemStack generateItem(Player p, @NotNull String itemCraft) {
        ItemStack itemStack = null;
        if (itemCraft.split(";")[0].equalsIgnoreCase("VANILLA")) {
            Material material = Material.getMaterial(itemCraft.split(";")[1]);
            if (material != Material.AIR && material != null) {
                int amount = Number.getInteger(itemCraft.split(";")[2]);
                itemStack = new ItemStack(material, amount);
            }
        } else if (StackCraft.isIsMMOItemsInstalled() && itemCraft.split(";")[0].equalsIgnoreCase("MMOITEMS")) {
            String type = itemCraft.split(";")[1];
            String id = itemCraft.split(";")[2];
            String amount = itemCraft.split(";")[3];
            ItemStack item = MMOItems.plugin.getItem(type, id);
            if (item != null) {
                item.setAmount(Number.getInteger(amount));
                itemStack = item;
            }
        } else if (StackCraft.isIsItemsAdderInstalled() && itemCraft.split(";")[0].equalsIgnoreCase("ITEMSADDER")) {
            String id = itemCraft.split(";")[1];
            String amount = itemCraft.split(";")[2];
            if (CustomStack.isInRegistry(id)) {
                CustomStack stack = CustomStack.getInstance(id);
                if (stack != null) {
                    itemStack = stack.getItemStack();
                    itemStack.setAmount(Number.getInteger(amount));
                }
            }
        } else if (StackCraft.isIsOraxenInstalled() && itemCraft.split(";")[0].equalsIgnoreCase("ORAXEN")) {
            String id = itemCraft.split(";")[1];
            String amount = itemCraft.split(";")[2];
            if (OraxenItems.exists(id)) {
                ItemBuilder stack = OraxenItems.getItemById(id);
                if (stack != null) {
                    itemStack = stack.build();
                    itemStack.setAmount(Number.getInteger(amount));
                }
            }
        } else if (StackCraft.isExecutableItemsInstalled() && itemCraft.split(";")[0].equalsIgnoreCase("EXECUTABLEITEMS")) {
            String id = itemCraft.split(";")[1];
            String amount = itemCraft.split(";")[2];
            if (ExecutableItemsAPI.getExecutableItemsManager().isValidID(id)) {
                Optional<ExecutableItemInterface> stack = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id);
                if (stack.isPresent()) {
                    itemStack = stack.get().buildItem(Number.getInteger(amount), Optional.of(p));
                }
            }
        } else if (StackCraft.isIsMythicInstalled() && itemCraft.split(";")[0].equalsIgnoreCase("MYTHICMOBS")) {
            String id = itemCraft.split(";")[1];
            String amount = itemCraft.split(";")[2];
            Optional<MythicItem> stack = MythicBukkit.inst().getItemManager().getItem(id);
            if (stack.isPresent()) {
                itemStack = stack.get().getCachedBaseItem().add(Number.getInteger(amount));
            }
        }
        return itemStack;
    }


    public static boolean checkCraftIngredient(Player p, List<String> ingredientsString) {
        HashMap<ItemStack, Integer> ingredients = Items.getIngredients(p, ingredientsString);
        HashMap<ItemStack, Boolean> ingredientsCheck = new HashMap<>();
        AtomicBoolean checkIngredients = new AtomicBoolean(false);
        for (ItemStack itemStack : ingredients.keySet()) {
            int craftAmount = ingredients.get(itemStack);
            int playerAmount = getPlayerAmount(p, itemStack);
            if (playerAmount >= craftAmount) {
                ingredientsCheck.put(itemStack, true);
            }
        }
        if (!ingredientsCheck.containsValue(false)) {
            checkIngredients.set(true);
            ingredientsCheck.keySet().forEach(itemStack -> removeItems(p, itemStack, ingredients.get(itemStack)));
        }
        return checkIngredients.get();
    }

    public static @NotNull HashMap<ItemStack, Integer> getIngredients(Player p, @NotNull List<String> ingredientsString) {
        HashMap<ItemStack, Integer> itemStacks = new HashMap<>();
        ingredientsString.forEach(ingredientString -> {
            String[] strings = ingredientString.split(";");
            if (strings.length >= 3) {
                if (strings[0].equalsIgnoreCase("VANILLA")) {
                    Material material = Material.getMaterial(strings[1]);
                    if (material != Material.AIR && material != null) {
                        int amount = Number.getInteger(strings[2]);
                        ItemStack itemStack = new ItemStack(material);
                        itemStacks.put(itemStack, amount);
                    }
                } else if (StackCraft.isIsMMOItemsInstalled() && strings[0].equalsIgnoreCase("MMOITEMS")) {
                    String type = strings[1];
                    String id = strings[2];
                    String amount = strings[3];
                    if (MMOItems.plugin.getItem(type, id) != null) {
                        itemStacks.put(MMOItems.plugin.getItem(type, id), Number.getInteger(amount));
                    }
                } else if (StackCraft.isIsItemsAdderInstalled() && strings[0].equalsIgnoreCase("ITEMSADDER")) {
                    String id = strings[1];
                    String amount = strings[2];
                    if (CustomStack.isInRegistry(id)) {
                        CustomStack stack = CustomStack.getInstance(id);
                        if (stack != null) {
                            itemStacks.put(stack.getItemStack(), Number.getInteger(amount));
                        }
                    }
                } else if (StackCraft.isIsOraxenInstalled() && strings[0].equalsIgnoreCase("ORAXEN")) {
                    String id = strings[1];
                    String amount = strings[2];
                    if (OraxenItems.exists(id)) {
                        ItemBuilder stack = OraxenItems.getItemById(id);
                        if (stack != null) {
                            itemStacks.put(stack.build(), Number.getInteger(amount));
                        }
                    }
                } else if (StackCraft.isExecutableItemsInstalled() && strings[0].equalsIgnoreCase("EXECUTABLEITEMS")) {
                    String id = strings[1];
                    String amount = strings[2];
                    if (ExecutableItemsAPI.getExecutableItemsManager().isValidID(id)) {
                        Optional<ExecutableItemInterface> stack = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id);
                        stack.ifPresent(executableItemInterface -> itemStacks.put(executableItemInterface.buildItem(1, Optional.of(p)), Number.getInteger(amount)));
                    }
                } else if (StackCraft.isIsMythicInstalled() && strings[0].equalsIgnoreCase("MYTHICMOBS")) {
                    String id = strings[1];
                    String amount = strings[2];
                    Optional<MythicItem> stack = MythicBukkit.inst().getItemManager().getItem(id);
                    stack.ifPresent(mythicItem -> itemStacks.put(mythicItem.getCachedBaseItem().add(1), Number.getInteger(amount)));
                }
            }
        });
        return itemStacks;
    }

    public static int getPlayerAmount(@NotNull HumanEntity player, ItemStack item) {
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (final ItemStack is : items) {
            if (is != null) {
                if (is.isSimilar(item)) {
                    c += is.getAmount();
                }
            }
        }
        return c;
    }

    public static void removeItems(@NotNull Player player, ItemStack item, long amount) {
        item = item.clone();
        final PlayerInventory inv = player.getInventory();
        final ItemStack[] items = inv.getContents();
        int c = 0;
        for (int i = 0; i < items.length; ++i) {
            final ItemStack is = items[i];
            if (is != null) {
                if (is.isSimilar(item)) {
                    if (c + is.getAmount() > amount) {
                        final long canDelete = amount - c;
                        is.setAmount((int) (is.getAmount() - canDelete));
                        items[i] = is;
                        break;
                    }
                    c += is.getAmount();
                    items[i] = null;
                }
            }
        }
        inv.setContents(items);
        player.updateInventory();
    }

}
