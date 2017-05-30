package me.catcoder.confirmation;

import org.bukkit.entity.Player;

/**
 * Confirmation response handler.
 *
 * @author CatCoder
 */
public interface ConfirmCallback {

    /**
     * Invoked when player clicked on item.
     *
     * @param response - 'yes' -> true or 'no' -> false.
     * @param player   - who clicked.
     */
    void onResponse(boolean response, Player player);
}
