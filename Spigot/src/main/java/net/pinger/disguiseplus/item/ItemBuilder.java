package net.pinger.disguiseplus.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    /**
     * The current instance of {@link ItemStack} which we're performing the changes on
     */

    private final ItemStack stack;

    /**
     * The display name of the {@link ItemMeta} and the {@link ItemStack}
     */

    private String displayName;

    /**
     * The lore of this {@link ItemStack}
     */

    private List<String> lore;

    /**
     * The list of enchantments that this {@link ItemStack} should have
     */

    private Map<Enchantment, Integer> enchantments = Maps.newHashMap();


    /**
     * Creates a new instance of the {@link ItemBuilder} with the given {@link ItemStack} as the base.
     * This {@link ItemStack} is used to add elements to its {@link ItemMeta} without having to call for {@link ItemStack#getItemMeta()} every time
     *
     * @param stack the stack that is being used to perform the changes on
     */

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder(Material material, int amount) {
        this.stack = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount, short data) {
        this.stack = new ItemStack(material, amount, data);
    }

    public ItemBuilder(Material material, short data) {
        this(material, 1, data);
    }

    /**
     * Checks if two references of {@link ItemStack} are similar.
     * This method is used instead of {@link ItemStack#isSimilar(ItemStack)} since we don't need to check for the amount.
     *
     * @param a the first stack
     * @param b the second stack
     * @return if they are similar
     */

    public static boolean areSimilar(ItemStack a, ItemStack b) {
        if (a.getType() != b.getType()) {
            return false;
        }

        if (!a.hasItemMeta() || !b.hasItemMeta()) {
            return false;
        }

        ItemMeta firstMeta = a.getItemMeta();
        ItemMeta secondMeta = b.getItemMeta();

        return firstMeta.getDisplayName().equalsIgnoreCase(secondMeta.getDisplayName());
    }

    /**
     * Sets the display name of the {@link ItemMeta} that is returned from the {@link ItemStack}.
     *
     * @param name the name that we wanted
     * @return this instance of the object
     */

    public ItemBuilder setName(String name) {
        this.displayName = name;
        return this;
    }

    /**
     * Sets the appropriate lore to the {@link ItemMeta}.
     *
     * @param lore the lore that is being set
     * @return this instance of the object
     */

    public ItemBuilder setLore(String... lore) {
        return this.setLore(Lists.newArrayList(lore));
    }

    /**
     * Sets the appropriate lore to the {@link ItemMeta}.
     *
     * @param lore the lore that is being set
     * @return this instance of the object
     */

    public ItemBuilder setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Adds a Level 1 {@link Enchantment} to the {@link ItemStack}.
     * This method can be used for both safe and unsafe enchantments.
     *
     * @param enchantment the enchantment that is being added
     * @return this instance of the object
     */

    public ItemBuilder addEnchantment(Enchantment enchantment) {
        return this.addEnchantment(enchantment, 1);
    }

    /**
     * Adds an {@link Enchantment} to the {@link ItemStack}.
     * This method can be used for both safe and unsafe enchantments.
     *
     * @param enchantment the enchantment that is being added
     * @param level the level of enchantment
     * @return this instance of the object
     */

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Uses the {@link ItemFlag#HIDE_ATTRIBUTES} to hide the attributes from an item.
     * This is often used when we want the inventory to look professional.
     *
     * @return this {@link ItemBuilder}
     */

    public ItemBuilder hideAttributes() {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }

        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder hideAllAttributes() {
        ItemMeta meta = this.stack.getItemMeta();

        // Edge case
        if (meta == null)
            return this;

        meta.addItemFlags(ItemFlag.values());
        this.stack.setItemMeta(meta);
        return this;
    }

    /**
     * Builds the ItemBuilder into the {@link ItemStack}
     * @return the appropriate stack
     */

    public ItemStack toItemStack() {
        ItemMeta meta = this.stack.getItemMeta();

        // Corner case (this is very odd)
        if (meta == null) {
            return this.stack;
        }

        // Set the display name
        if (this.displayName != null)
            meta.setDisplayName(this.displayName);

        // Add lore
        if (this.lore != null)
            meta.setLore(this.lore);

        // Add the enchantments
        for (Map.Entry<Enchantment, Integer> entry : this.enchantments.entrySet())
            this.stack.addUnsafeEnchantment(entry.getKey(), entry.getValue());

        this.stack.setItemMeta(meta);
        return this.stack;
    }

}
