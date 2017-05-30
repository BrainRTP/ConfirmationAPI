# ConfirmationAPI

Simple Bukkit plugin API for listetning player confirmation responses ("yes", "no").

####Usage

If yot use API as external plugin, so you do not need to initialize API.
Otherwise, you need to initialize it with your plugin:

```java
ConfirmationAPI.initialize(yorplugin);
```

Any example:
```java
//Creating inventory.
Inventory inv = Bukkit.createInventory(null, 27, "Test confirm");

//Registering dialog
ConfirmationDialog dialog = ConfirmationAPI.getInventoryManager().registerDialog("Test", inv, 12, 14, (condition, player) -> {
    //Handle player response.
    player.closeInventory();
    player.sendMessage("You clicked: " + (condition ? "Yes" : "No"));
});

//Opening dialog for any player.
player.openInventory(dialog.getInventory());
```
