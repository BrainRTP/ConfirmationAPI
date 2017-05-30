package me.catcoder.confirmation;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Dialog ID inventory holder.
 *
 * @author CatCoder
 */
public class DialogInventoryHolder implements InventoryHolder {

    private final String id;

    public DialogInventoryHolder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
