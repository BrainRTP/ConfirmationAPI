package me.catcoder.confirmation;

import com.google.common.collect.MapMaker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Click listener and dialog registrar.
 *
 * @author CatCoder
 */
public class InventoryManager {


    private final Plugin targetPlugin;
    private final Map<String, ConfirmationDialog> dialogMap;

    public InventoryManager(Plugin targetPlugin) {
        this.targetPlugin = targetPlugin;
        this.dialogMap = new MapMaker().weakKeys().makeMap();
        this.registerBukkitEvents();
    }

    /**
     * Register new confirmation dialog.
     *
     * @param id        - unique dialog id.
     * @param inventory - bukkit inventory.
     * @param yesSlot   - slot, when player clicked 'yes'
     * @param noSlot    - slot, when player clicked 'no'
     * @param callback  - callback for action execution.
     * @return created {@link ConfirmationDialog}
     */
    public ConfirmationDialog registerDialog(String id, Inventory inventory, int yesSlot, int noSlot, ConfirmCallback callback) {
        checkState(!dialogMap.containsKey(id.toLowerCase()), "Dialog ID is already defined: %s", id);
        final Inventory copied = copy(inventory, new DialogInventoryHolder(id));
        ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            public ConfirmCallback getCallback() {
                return callback;
            }

            @Override
            public Inventory getInventory() {
                return copied;
            }

            @Override
            public int getYesSlot() {
                return yesSlot;
            }

            @Override
            public int getNoSlot() {
                return noSlot;
            }
        };
        dialogMap.put(id.toLowerCase(), dialog);
        return dialog;
    }

    /**
     * Removes dialog by given id.
     *
     * @param id - unique id of dialog
     * @return previous dialog if exists.
     */
    public ConfirmationDialog removeDialog(String id) {
        return dialogMap.remove(id.toLowerCase());
    }

    /**
     * Internal method for register needed bukkit events.
     */
    private void registerBukkitEvents() {
        Listener pluginListener = new Listener() {

            @EventHandler
            public void onDisable(PluginDisableEvent event) {
                if (event.getPlugin().equals(targetPlugin)) {
                    ConfirmationAPI.deInitialize();
                }
            }
        };
        Listener inventoryListener = new Listener() {

            @EventHandler
            public void onDrag(InventoryDragEvent event) {
                if (isConfirmationInventory(event.getInventory())) {
                    event.setCancelled(true);
                }
            }

            //Response handle
            @EventHandler
            public void onClick(InventoryClickEvent event) {
                if (!isConfirmationInventory(event.getInventory())) return;
                Player player = (Player) event.getWhoClicked();
                DialogInventoryHolder holder = (DialogInventoryHolder) event.getInventory().getHolder();
                ConfirmationDialog dialog = dialogMap.get(holder.getId());

                checkState(dialog != null, "Unregistered dialog: " + holder.getId());

                int clickedSlot = event.getRawSlot();

                if (clickedSlot == dialog.getYesSlot()) {
                    //Player clicked 'yes'.
                    dialog.getCallback().onResponse(true, player);
                } else if (clickedSlot == dialog.getNoSlot()) {
                    //Player clicked 'no'
                    dialog.getCallback().onResponse(false, player);
                }
                //Cancel event
                event.setCancelled(true);
            }

        };
        final PluginManager pluginManager = targetPlugin.getServer().getPluginManager();
        pluginManager.registerEvents(pluginListener, targetPlugin);
        pluginManager.registerEvents(inventoryListener, targetPlugin);
    }


    ///////////////////////////////////////////////////
    // STATIC METHODS
    ///////////////////////////////////////////////////

    /**
     * Utility method.
     *
     * @param inventory - bukkit inventory
     * @return true if {@link InventoryHolder} instance of {@link DialogInventoryHolder}
     */
    private static boolean isConfirmationInventory(Inventory inventory) {
        return inventory.getHolder() instanceof DialogInventoryHolder;
    }

    /**
     * Copy inventory info to new inventory.
     *
     * @param inventory - target inventory.
     * @param holder - inventory holder.
     * @return copied {@link Inventory}
     */
    private static Inventory copy(Inventory inventory, InventoryHolder holder) {
        Inventory copied = Bukkit.createInventory(holder, inventory.getSize(), inventory.getTitle());
        copied.setContents(inventory.getContents());
        return copied;
    }
}
