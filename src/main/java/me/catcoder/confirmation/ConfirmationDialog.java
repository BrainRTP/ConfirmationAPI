package me.catcoder.confirmation;

import org.bukkit.inventory.Inventory;

/**
 * @author CatCoder
 */
public interface ConfirmationDialog {

    ConfirmCallback getCallback();

    Inventory getInventory();

    int getYesSlot();

    int getNoSlot();
}
