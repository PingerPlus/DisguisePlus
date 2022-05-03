package net.pinger.disguise;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

/**
 * This class represents a type which can be used for changing
 * the player's properties through the GameObject type.
 * <p>
 * Each skin has a Base64 encoded value and signature, which is
 * used for the transformation.
 *
 * @since 2.0
 */

public interface Skin {

    /**
     * This method returns the encoded
     * value of this skin.
     *
     * @return the value
     */

    String getValue();

    /**
     * This method returns the encoded
     * signature of this skin.
     *
     * @return the signature
     */

    String getSignature();

    /**
     * Transforms this skin to a skull.
     * <p>
     * This instance is created once the skin has been initialized,
     * and it will never change unless the server has been restarted.
     *
     * @return the skull from this item
     */

    ItemStack toSkull();

    /**
     * This method returns the property handle
     * for this skin.
     *
     * @return the handle
     */

    @Nonnull
    Object getHandle();

}
