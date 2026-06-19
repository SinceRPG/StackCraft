<div align="center">
  <img src="pickaxe.png" alt="StackCraft Logo" width="200" />
  <h1>StackCraft</h1>
  <p>An advanced, high-performance auto-crafting and item conversion plugin for PaperMC.</p>

  [![Version](https://img.shields.io/badge/Version-1.8.2-blue.svg)](https://github.com/SinceRPG/StackCraft)
  [![API](https://img.shields.io/badge/API-1.21-green.svg)](https://papermc.io/)
  [![Folia](https://img.shields.io/badge/Folia-Supported-success.svg)](https://papermc.io/software/folia)

</div>

## 🌟 Overview

**StackCraft** is a powerful plugin designed to seamlessly convert and auto-craft items in a player's inventory. Whether you are dealing with Vanilla items or custom items from popular plugins, StackCraft handles it all with minimal performance impact.

It automatically processes items after pickups, block breaks, mob kills, or via a scheduled task, keeping your players' inventories organized and saving them from tedious manual crafting.

## ✨ Features

- **Extensive Plugin Support**: Fully supports custom items from:
  - `ItemsAdder`
  - `Oraxen`
  - `MMOItems`
  - `ExecutableItems` (SCore)
  - `Nexo`
  - `ItemEdit`
  - `MythicMobs`
- **Folia Support**: Fully compatible with Folia for high-performance servers.
- **Flexible Crafting Recipes**: Define custom conversion rates and recipes. Require all ingredients or allow any of the listed ingredients to craft the result.
- **Toggle System**: Allow players to toggle specific auto-crafting rules on or off with customizable commands and permissions.
- **Queue-Based Processing**: High-performance, queue-based item processing prevents server lag.
- **Overflow Handling**: Automatically drops crafted items near the player if their inventory is full.

## 📖 Documentation & Wiki

For detailed setup instructions, configuration guides, and usage examples, please visit our **[Official Wiki](https://sincerpg.github.io/StackCraft/)** (or the documentation site linked in this repository).

## 🚀 Quick Start

1. Download the latest release of `StackCraft.jar`.
2. Place it in your server's `plugins/` folder.
3. Ensure you have the required dependencies for any custom item plugins you plan to use.
4. Restart your server.
5. Configure your crafting rules in `plugins/StackCraft/config.yml`.

## ⚙️ Commands & Permissions

| Command | Permission | Description |
|---|---|---|
| `/stc` | `stc.admin` | Main administrative command for StackCraft. |
| Custom Toggle | `stc.toggle.<name>` | Permission to use a specific toggle command (e.g., `stc.toggle.coal`). |

## 🛠️ Configuration Example

Here is a quick look at how easy it is to configure a custom crafting rule:

```yaml
craft:
  VANILLA;DIAMOND;1:
    required_all: true
    ingredient:
      - VANILLA;DIAMOND_BLOCK;1
  MMOITEMS;MATERIAL;CUSTOM_DIAMOND;1:
    ingredient:
      - VANILLA;DIAMOND;64
```

*In this example, 1 Vanilla Diamond Block converts into 1 Vanilla Diamond. Also, 64 Vanilla Diamonds convert into 1 Custom MMOItem Diamond.*

## 📄 License

This project is licensed under the MIT License.
