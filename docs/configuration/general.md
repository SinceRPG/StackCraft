# General Settings

The `settings` section in `config.yml` controls the overall behavior and performance of StackCraft.

```yaml
settings:
  debug: false
  prefix: "<white>[ <yellow>STC</yellow> ]"
  queue_interval_ticks: 5
  auto_craft_schedule: false
  auto_craft_schedule_interval_ticks: 20
  drop_overflow_items: true
```

## Options Explained

- `debug`: Enable this to print detailed logs to the console. Very helpful when setting up new custom items or troubleshooting.
- `prefix`: The MiniMessage-formatted prefix used in plugin messages.
- `queue_interval_ticks`: The delay (in ticks) between processing players in the queue. 20 ticks = 1 second. Lowering this makes processing faster but may slightly increase server load.
- `auto_craft_schedule`: If enabled, the plugin will continuously check all online players and auto-craft their items on a schedule. *Keep this disabled if players often need to uncraft items manually.*
- `auto_craft_schedule_interval_ticks`: How often (in ticks) the scheduled auto-craft scans all online players (only applies if `auto_craft_schedule` is `true`).
- `drop_overflow_items`: When a player's inventory is full after an item is crafted, setting this to `true` will drop the excess items on the ground near them. If `false`, the overflow items are deleted.
