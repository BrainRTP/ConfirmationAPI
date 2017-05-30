package me.catcoder.confirmation.plugin;

import me.catcoder.confirmation.ConfirmationAPI;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit plugin to hook with API.
 *
 * @author CatCoder
 */
public class ConfirmationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfirmationAPI.initialize(this);
    }
}
