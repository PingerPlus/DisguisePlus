package net.pinger.disguiseplus.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDisguiseEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    private final Player player;

    /**
     * Constructs a new disguise event for the specified player.
     *
     * @param player the player that is being disguised
     */

    public PlayerDisguiseEvent(Player player) {
        this.player = player;
    }

    /**
     * Returns the player that is involved in this event.
     *
     * @return the player involved
     */

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
