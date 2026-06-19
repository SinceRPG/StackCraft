# Auto Crafting Settings

The heart of StackCraft is the `craft:` section. This is where you map ingredients to final products.

## The Structure

```yaml
craft:
  [RESULT_ITEM_FORMAT]:
    required_all: [true|false]
    ingredient:
      - [INGREDIENT_1_FORMAT]
      - [INGREDIENT_2_FORMAT]
```

- **`RESULT_ITEM_FORMAT`**: The item the player will receive. See [Item Formats](../integrations/item-formats.md) for syntax.
- **`required_all`**:
    - If `true`, the player must have **every** item listed in the `ingredient` list in their inventory to perform the craft.
    - If `false` or missing, the player can use **any one** of the items in the `ingredient` list to craft the result.
- **`ingredient`**: A list of required item formats and amounts.

> [!WARNING]
> **Crucial Step:** Simply adding a recipe in the `craft:` section is **not enough**. For the auto-craft to actually work, you **must** add the `[RESULT_ITEM_FORMAT]` to the `contain` list of a `toggle` group! See [Toggle Commands](toggle-commands.md) for more details.

## Example: Any Ingredient Works

Convert either 64 Vanilla Coal OR 64 Vanilla Coal Blocks into 1 Custom MMOItem Coal.

```yaml
  MMOITEMS;MATERIAL;COAL;1:
    required_all: false
    ingredient:
      - VANILLA;COAL;64
      - VANILLA;COAL_BLOCK;64
```

## Example: All Ingredients Required

Combine 1 Sword Blade, 1 Sword Hilt, and 1 Magic Gem to create an Epic Sword.

```yaml
  MMOITEMS;SWORD;EPIC_SWORD;1:
    required_all: true
    ingredient:
      - MMOITEMS;MATERIAL;SWORD_BLADE;1
      - MMOITEMS;MATERIAL;SWORD_HILT;1
      - MMOITEMS;MATERIAL;MAGIC_GEM;1
```
