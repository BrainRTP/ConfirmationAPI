package me.catcoder.confirmation;

import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Main API class.
 *
 * @author CatCoder
 */
public class ConfirmationAPI {


    private static boolean initialized;
    private static InventoryManager inventoryManager;


    /**
     * Initializing API.
     *
     * @param plugin - plugin to hook with.
     */
    public static void initialize(Plugin plugin) {
        checkState(!initialized, "ConfirmationAPI is already initialized.");
        checkArgument(plugin != null, "Plugin cannot be NULL.");
        checkState(plugin.isEnabled(), "Plugin cannot be disabled.");
        inventoryManager = new InventoryManager(plugin);
        initialized = true;
    }

    /**
     * @see ConfirmationDialog::registerDialog
     */
    public static ConfirmationDialog registerDialog(String id, Inventory inventory, int yesSlot, int noSlot, ConfirmCallback callback) {
        return getInventoryManager().registerDialog(id, inventory, yesSlot, noSlot, callback);
    }

    /**
     * Gets manager.
     *
     * @return InventoryManager if API initialized.
     */
    public static InventoryManager getInventoryManager() {
        checkArgument(isInitialized(), "ConfirmationAPI is not initialized. Please initialize it.");
        return inventoryManager;
    }

    /**
     * Deinitializing API. Only package access.
     */
    protected static void deInitialize() {
        inventoryManager = null;
        initialized = false;
    }

    /**
     * Is API initialized?
     *
     * @return true if API initialized.
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
