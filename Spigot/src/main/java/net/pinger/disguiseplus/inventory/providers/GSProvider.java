package net.pinger.disguiseplus.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GSProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public GSProvider(DisguisePlus dp) {
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

        contents.set(0, 4, ClickableItem.empty(dw));

        // Coming soon
        ItemStack cs = new ItemBuilder(FreshMaterial.CRAFTING_TABLE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("Disguise Settings", state))
                .toItemStack();

        contents.set(0, 3, ClickableItem.of(cs, e -> {
            this.dp.getInventoryManager().getDisguiseSettings().open((Player) e.getWhoClicked());
        }));

        ItemStack wd = new ItemBuilder(FreshMaterial.ENDER_EYE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Worlds", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view " + ChatColor.GOLD + "disabled" + ChatColor.GRAY + " worlds.")
                .toItemStack();

        contents.set(2, 1, ClickableItem.of(wd, e -> {
            this.dp.getInventoryManager().getWorldListProvider().open((Player) e.getWhoClicked());
        }));

        String enable = this.dp.getSettings().isOnlineMode() ? "disable" : "enable";

        ItemStack np = new ItemBuilder(FreshMaterial.BLAZE_ROD.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Online Mode", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + enable + ".", Strings.EMPTY,
                        ChatColor.GRAY + "If this option is disabled, we assume that the server", ChatColor.GRAY + "is also running in offline mode.")
                .toItemStack();

        contents.set(2, 7, ClickableItem.of(np, e -> {
            this.dp.getSettings().reverseOnline();
        }));

        String update = this.dp.getSettings().isUpdate() ? "disable" : "enable";

        ItemStack uc = new ItemBuilder(FreshMaterial.GOLD_INGOT.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Update Checker", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + update + ".", Strings.EMPTY,
                        ChatColor.GRAY + "This option makes sure that the server will", ChatColor.GRAY + "stay " + ChatColor.YELLOW + "up-to-date" + ChatColor.GRAY + " with the plugin version.")
                .toItemStack();

        contents.set(3, 3, ClickableItem.of(uc, e -> {
            this.dp.getSettings().reverseUpdate();
        }));

        // Placeholders
        ItemStack ph = new ItemBuilder(FreshMaterial.DIAMOND_BLOCK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Display Settings", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + "change" + ChatColor.GRAY + " the placeholders.")
                .toItemStack();

        contents.set(2, 4, ClickableItem.of(ph, e -> {
            this.dp.getInventoryManager().getDisplayProvider().open((Player) e.getWhoClicked());
        }));

        String bstats = this.dp.getSettings().isMetrics() ? "disable" : "enable";

        ItemStack bs = new ItemBuilder(FreshMaterial.BLAZE_POWDER.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("bStats", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + bstats + ".", Strings.EMPTY,
                        ChatColor.GRAY + "Will take effect after the reload of the server.")
                .toItemStack();

        contents.set(3, 5, ClickableItem.of(bs, e -> {
            this.dp.getSettings().reverseMetrics();
        }));

        // Return
        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }
}
