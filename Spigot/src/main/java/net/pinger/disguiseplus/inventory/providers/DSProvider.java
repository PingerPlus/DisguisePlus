package net.pinger.disguiseplus.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DSProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public DSProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        // Disabled World
        ItemStack dw = new ItemBuilder(FreshMaterial.OBSIDIAN.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("General Settings", state))
                .setLore(ChatColor.GRAY + "Click to change the settings")
                .hideAllAttributes()
                .addEnchantment(Enchantment.LURE)
                .toItemStack();

        contents.set(0, 4, ClickableItem.of(dw, e -> {
            this.dp.getInventoryManager().getGeneralSettingsProvider().open((Player) e.getWhoClicked());
        }));

        // Coming soon
        ItemStack cs = new ItemBuilder(FreshMaterial.CRAFTING_TABLE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("Disguise Settings", state))
                .toItemStack();

        contents.set(0, 3, ClickableItem.empty(cs));

        // Nick Creator
        ItemStack nc = new ItemBuilder(Material.STICK)
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Nick Creator", state))
                .setLore(ChatColor.AQUA + "Click " + ChatColor.GRAY + "to" + ChatColor.GOLD + " choose " + ChatColor.GRAY + "a custom pattern for your skins.")
                .toItemStack();

        SimpleInventoryManager.addReturnButton(4, 4, contents);
    }
}
