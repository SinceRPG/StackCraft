# Item Formats

When defining recipes in your `craft` section or grouping items in your `toggle` section, you must use StackCraft's internal formatting syntax.

## General Syntax

`<PLUGIN_PREFIX>;<IDENTIFIER>;<AMOUNT>`

### Vanilla Items
- **Prefix**: `VANILLA`
- **Format**: `VANILLA;MATERIAL;AMOUNT`
- **Example**: `VANILLA;DIAMOND;64`

### ItemsAdder
- **Prefix**: `ITEMSADDER`
- **Format**: `ITEMSADDER;CUSTOM_ID;AMOUNT`
- **Example**: `ITEMSADDER;ruby;1`

### Oraxen
- **Prefix**: `ORAXEN`
- **Format**: `ORAXEN;CUSTOM_ID;AMOUNT`
- **Example**: `ORAXEN;amethyst_sword;1`

### MMOItems
- **Prefix**: `MMOITEMS`
- **Format**: `MMOITEMS;TYPE;ID;AMOUNT`
- **Example**: `MMOITEMS;MATERIAL;STEEL_INGOT;1`

### ExecutableItems (SCore)
- **Prefix**: `EXECUTABLEITEMS`
- **Format**: `EXECUTABLEITEMS;ID;AMOUNT`
- **Example**: `EXECUTABLEITEMS;magic_wand;1`

### Nexo
- **Prefix**: `NEXO`
- **Format**: `NEXO;ID;AMOUNT`
- **Example**: `NEXO;copper_coin;10`

### ItemEdit
- **Prefix**: `ITEMEDIT`
- **Format**: `ITEMEDIT;ID;AMOUNT`
- **Example**: `ITEMEDIT;custom_potion;1`

### MythicMobs
- **Prefix**: `MYTHICMOBS`
- **Format**: `MYTHICMOBS;ID;AMOUNT`
- **Example**: `MYTHICMOBS;SkeletonKingSword;1`
