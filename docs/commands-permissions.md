# Commands & Permissions

StackCraft provides a simple set of commands and permissions.

## Commands

### `/stc`
The main command for StackCraft. It will open up the main GUI menu or display a help message depending on your setup.

### `/stc reload`
Reloads the `config.yml` configuration without requiring a server restart. Great for testing new recipes!

### Custom Toggle Commands
If you configured a custom toggle command in the `toggle` section of your config (e.g., `alias: coalcrafttoggle`), players can use that command directly (e.g., `/coalcrafttoggle`) to enable or disable that specific crafting group.

## Permissions

| Permission | Description | Default |
|---|---|---|
| `stc.admin` | Grants access to administrative commands like `/stc reload` and the main admin GUI. | OP |
| `stc.toggle.<name>` | Grants permission to toggle a specific auto-crafting group. `<name>` matches the key defined in the `toggle` config section (e.g., `stc.toggle.coal`). | False |

> [!NOTE]
> If a player does not have the `stc.toggle.<name>` permission, they will not be able to use the custom toggle command, and the items in that group will continue to auto-craft based on server rules.
