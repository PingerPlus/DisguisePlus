package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.item.FreshMaterial;
import net.pinger.disguiseplus.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

public class DisguisePlusProvider implements IntelligentProvider {

    private final DisguisePlus dp;

    public DisguisePlusProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {
        // Get skull for this player
        ItemStack skull = new ItemBuilder(this.dp.getSkullManager().getSkullFrom(player.getUniqueId()))
                .setName(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Users")
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view users.")
                .toItemStack();

        contents.setItem(1, 2, IntelligentItem.createNew(skull, e -> {
            this.dp.getInventoryManager().getUserListProvider().open((Player) e.getWhoClicked());
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh every
        // 2 ticks
        if (refresh % 2 != 0)
            return;

        // Item for viewing
        // All skin packs
        ItemStack skinPack = new ItemBuilder(FreshMaterial.GOLD_NUGGET.toMaterial())
                .setName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Skin Packs")
                .setLore(ChatColor.GRAY + "Click to open skin packs.")
                .toItemStack();

        contents.setItem(1, 6, IntelligentItem.createNew(skinPack, e -> {
            this.dp.getInventoryManager().getSkinPacksProvider().open((Player) e.getWhoClicked());
        }));
    }
}
