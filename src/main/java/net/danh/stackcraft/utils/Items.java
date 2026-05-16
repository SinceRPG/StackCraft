package net.danh.stackcraft.utils;

import com.nexomc.nexo.api.NexoItems;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import dev.lone.itemsadder.api.CustomStack;
import emanondev.itemedit.ItemEdit;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.th0rgal.oraxen.api.OraxenItems;
import net.Indyuce.mmoitems.MMOItems;
import net.danh.stackcraft.StackCraft;
import net.danh.stackcraft.resources.Chat;
import net.danh.stackcraft.resources.Files;
import net.danh.stackcraft.resources.Number;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Items {

    public static final ConcurrentMap<UUID, Boolean> toggle = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, String> toggle_craft = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, String> full_toggle_craft = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, Boolean> per_toggle_craft = new ConcurrentHashMap<>();

    public static String getStatus(Player p, String item) {
        boolean isOn = (item == null)
                ? getGlobalToggle(p)
                : getPerToggle(p, item);
        return isOn ? Files.getMessage().getString("user.status.status_on")
                : Files.getMessage().getString("user.status.status_off");
    }

    public static boolean getGlobalToggle(Player player) {
        return toggle.getOrDefault(player.getUniqueId(), false);
    }

    public static void setGlobalToggle(Player player, boolean enabled) {
        toggle.put(player.getUniqueId(), enabled);
    }

    public static boolean getPerToggle(Player player, String item) {
        return per_toggle_craft.getOrDefault(getPlayerItemKey(player, item), false);
    }

    public static void setPerToggle(Player player, String item, boolean enabled) {
        per_toggle_craft.put(getPlayerItemKey(player, item), enabled);
    }

    public static void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        toggle.remove(uuid);
        String prefix = uuid + "_";
        per_toggle_craft.keySet().removeIf(key -> key.startsWith(prefix));
    }

    public static String getPlayerItemKey(Player player, String item) {
        return player.getUniqueId() + "_" + item;
    }

    @Nullable
    public static ItemStack parseItem(String itemString) {
        try {
            String[] split = itemString.split(";");
            if (split.length < 2) return null;

            String type = split[0].toUpperCase();
            String id = split[1];
            ItemStack item = null;

            if (type.equals("VANILLA")) {
                Material mat = Material.getMaterial(id);
                if (mat != null) item = new ItemStack(mat);
            } else {
                item = getCustomItem(type, id, split);
            }

            if (item != null) item.setAmount(1);
            return item;
        } catch (Exception e) {
            Chat.debug("Error parsing item: " + itemString + " (" + e.getMessage() + ")");
            return null;
        }
    }

    private static ItemStack getCustomItem(String type, String id, String[] split) {
        try {
            switch (type) {
                case "MMOITEMS":
                    if (StackCraft.isMMOItemsInstalled() && split.length >= 3)
                        return MMOItems.plugin.getItem(split[1], split[2]);
                    break;
                case "ITEMSADDER":
                    if (StackCraft.isItemsAdderInstalled() && CustomStack.isInRegistry(id)) {
                        CustomStack cs = CustomStack.getInstance(id);
                        return (cs != null) ? cs.getItemStack() : null;
                    }
                    break;
                case "ORAXEN":
                    if (StackCraft.isOraxenInstalled() && OraxenItems.exists(id))
                        return OraxenItems.getItemById(id).build();
                    break;
                case "EXECUTABLEITEMS":
                    if (StackCraft.isExecutableItemsInstalled())
                        return ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id)
                                .map(ei -> ei.buildItem(1, Optional.empty())).orElse(null);
                    break;
                case "NEXO":
                    if (StackCraft.isNexoInstalled() && NexoItems.itemFromId(id) != null)
                        return NexoItems.itemFromId(id).build();
                    break;
                case "ITEMEDIT":
                    if (StackCraft.isItemEditInstalled())
                        return ItemEdit.get().getServerStorage().getItem(id);
                    break;
                case "MYTHICMOBS":
                    if (StackCraft.isMythicInstalled())
                        return MythicBukkit.inst().getItemManager().getItem(id)
                                .map(mi -> mi.getCachedBaseItem()).orElse(null);
                    break;
                default:
                    Chat.debug("Unsupported item provider: " + type);
                    break;
            }
        } catch (NoClassDefFoundError | Exception ex) {
            Chat.debug("Dependency Error for " + type + ": " + ex.getMessage());
        }
        return null;
    }

    public static int parseAmount(String itemString) {
        String[] split = itemString.split(";");
        if (split[0].equalsIgnoreCase("MMOITEMS")) {
            return split.length >= 4 ? Number.getInteger(split[3]) : 1;
        }
        return split.length >= 3 ? Number.getInteger(split[2]) : 1;
    }

    public static boolean isSameItem(ItemStack i1, ItemStack i2) {
        if (i1 == null || i2 == null) return false;
        return i1.isSimilar(i2);
    }

    public static int getPlayerAmount(HumanEntity player, ItemStack item) {
        if (item == null) return 0;
        PlayerInventory inv = player.getInventory();
        int count = 0;
        for (ItemStack is : inv.getContents()) {
            if (is != null && isSameItem(is, item)) {
                count += is.getAmount();
            }
        }
        return count;
    }

    public static void removeItems(Player player, ItemStack item, int amount) {
        if (item == null || amount <= 0) return;
        PlayerInventory inv = player.getInventory();
        ItemStack[] contents = inv.getContents();
        int remaining = amount;

        for (int i = 0; i < contents.length; i++) {
            ItemStack is = contents[i];
            if (is != null && isSameItem(is, item)) {
                if (is.getAmount() > remaining) {
                    is.setAmount(is.getAmount() - remaining);
                    remaining = 0;
                } else {
                    remaining -= is.getAmount();
                    contents[i] = null;
                }
            }
            if (remaining <= 0) break;
        }
        inv.setContents(contents);
    }
}
