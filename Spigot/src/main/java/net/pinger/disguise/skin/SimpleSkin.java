package net.pinger.disguise.skin;

import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.disguise.skull.SkullManager;
import net.pinger.disguise.utils.PropertyUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SimpleSkin implements Skin {

    private final String signature, value;
    private final ItemStack skull = new ItemBuilder(FreshMaterial.PLAYER_HEAD.toMaterial(), (short) 3).toItemStack();

    private SimpleSkin(String value, String signature) {
        this.signature = signature;
        this.value = value;

        // Update the meta
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        SkullManager.mutateItemMeta(meta, this);
        this.skull.setItemMeta(meta);
    }

    public static Skin newBuilder(String value, String signature) {
        return new SimpleSkin(value, signature);
    }

    /**
     * Returns the value of this skin.
     *
     * @return the value
     */
    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * Returns the signature of this skin.
     *
     * @return the signature
     */
    @Override
    public String getSignature() {
        return this.signature;
    }

    /**
     * Transform this skin into a property object based on the current version of the server.
     *
     * @return the property
     */

    @Override
    public Object toProperty() {
        return PropertyUtil.getProperty(this);
    }

    /**
     * Transforms this skin to a skull.
     * <p>
     * This instance is created once the skin has been initialized,
     * and it will never change unless the server has been restarted.
     *
     * @return the skull from this item
     */

    @Override
    public ItemStack toSkull() {
        return this.skull;
    }
}
