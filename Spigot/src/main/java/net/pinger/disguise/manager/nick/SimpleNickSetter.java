package net.pinger.disguise.manager.nick;

import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SimpleNickSetter {

    private static final Logger logger = LoggerFactory.getLogger("DisguisePlusManager");

    private static Method PLAYER_HANDLE_METHOD;
    private static Method PROFILE_METHOD;

    /**
     * This method applies a nick to a specific player.
     * It shouldn't be used anywhere outside of the {@link net.pinger.disguise.manager.SimpleDisguiseManager} class.
     *
     * @param player the player
     * @param name the name
     */

    public static void applyNick(Player player, String name) {
        try {
            // Set the player handle method
            if (PLAYER_HANDLE_METHOD == null)
                PLAYER_HANDLE_METHOD = player.getClass().getMethod("getHandle");

            Object entity = PLAYER_HANDLE_METHOD.invoke(player);

            // Set the profile method
            if (PROFILE_METHOD == null) {
                PROFILE_METHOD = entity.getClass().getMethod("getProfile");
            }

            Object ep = PROFILE_METHOD.invoke(entity);
            Field nameField = ep.getClass().getDeclaredField("name");

            // Set the field
            nameField.setAccessible(true);
            nameField.set(ep, name);

            // Change the player fields
            player.setDisplayName(name);
            player.setPlayerListName(name);
        } catch (Exception e) {
            logger.error("Failed to load set a nick for player -> " + player.getName());
            logger.error(e.getMessage());
        }
    }

}
