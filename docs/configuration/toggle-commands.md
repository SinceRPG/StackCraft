# Toggle Commands

StackCraft allows you to group certain crafting recipes under a "toggleable" node. Players with the right permissions can enable or disable these groups so they don't accidentally craft things they want to keep as raw materials!

> [!IMPORTANT]
> **Mandatory Step:** Every item you define in the `craft:` section **must** be added to the `contain` list of at least one `toggle` group. If an item is not in a toggle group, StackCraft will not auto-craft it!

## Configuration

In your `config.yml`, find the `toggle:` section:

```yaml
toggle:
  coal: 
    display: "<gray>AutoCraft Coal"
    command: 
      register: true 
      alias: coalcrafttoggle
    contain:
      - VANILLA;COAL;1
      - MMOITEMS;MATERIAL;COAL;1
      - MMOITEMS;MATERIAL;COAL_ORE;1
      - MMOITEMS;MATERIAL;COAL_BLOCK;1
```

## Options

- **`coal`**: The internal name of the toggle category. The permission to use this toggle will automatically become `stc.toggle.coal`.
- **`display`**: The MiniMessage-formatted text used in GUIs or messages.
- **`command.register`**: If `true`, StackCraft will register a dedicated command for this toggle.
- **`command.alias`**: The command players can type (e.g., `/coalcrafttoggle`).
- **`contain`**: A list of `RESULT_ITEM_FORMAT` items from the `craft` section. If a player toggles "coal" off, the plugin will pause auto-crafting for all items listed here for that specific player.

## Default States

You can also define the default states for players in `default_toggle_item`:

```yaml
default_toggle_item:
  all: false # If true, auto-crafting is enabled for new players. If false, they must manually enable it!
  per: false # Advanced per-item toggles (for future expansions)
```

> [!TIP]
> If `all: false`, players **will not** have auto-crafting enabled when they join. They must manually type the toggle command (e.g., `/coalcrafttoggle`) to turn it ON. Change this to `true` if you want auto-crafting enabled by default!
