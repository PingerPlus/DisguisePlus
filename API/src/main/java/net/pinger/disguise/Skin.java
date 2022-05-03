package net.pinger.disguise;

import org.bukkit.inventory.ItemStack;

public interface Skin {

    /**
     * Returns the value of this skin.
     *
     * @return the value
     */

    String getValue();

    /**
     * Returns the signature of this skin.
     *
     * @return the signature
     */

    String getSignature();

    /**
     * Transform this skin into a property object based on the current version of the server.
     *
     * @return the property
     */

    Object toProperty();

    /**
     * Transforms this skin to a skull.
     * <p>
     * This instance is created once the skin has been initialized,
     * and it will never change unless the server has been restarted.
     *
     * @return the skull from this item
     */

    ItemStack toSkull();


}
