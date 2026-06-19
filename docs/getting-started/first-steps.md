# First Steps

After installing StackCraft, you'll want to configure your very first auto-crafting rule.

## 1. Locate the config.yml
Open the `plugins/StackCraft/config.yml` file using a text editor.

## 2. Define a Simple Craft
Scroll down to the `craft:` section. By default, you will see some example conversions like coal and MMOItems configurations. Let's add a rule to automatically convert Iron Ore into Iron Ingots (assuming an instant smelt auto-craft server setup), or 9 Iron Ingots into an Iron Block!

```yaml
craft:
  VANILLA;IRON_BLOCK;1:
    required_all: true
    ingredient:
      - VANILLA;IRON_INGOT;9
```

This simple configuration means that whenever a player has 9 Vanilla Iron Ingots in their inventory, the plugin will remove them and give the player 1 Vanilla Iron Block.

## 3. Register it in a Toggle Group
**IMPORTANT:** Your new crafting rule will NOT work until you add it to a toggle group! StackCraft requires all crafted items to belong to a toggle category so players can enable/disable them.

Scroll up to the `toggle:` section and add your new item to an existing group, or create a new one:

```yaml
toggle:
  iron:
    display: "<gray>AutoCraft Iron"
    command:
      register: true
      alias: ironcrafttoggle
    contain:
      - VANILLA;IRON_BLOCK;1 # <- Add your craft result here!
```

## 4. Reload Settings
Use the `/stc reload` command in-game or from the console to apply your new configuration instantly.

## Next Up
Learn more about [General Settings](../configuration/general.md), [Auto Crafting](../configuration/auto-crafting.md), and [Item Formats](../integrations/item-formats.md).
