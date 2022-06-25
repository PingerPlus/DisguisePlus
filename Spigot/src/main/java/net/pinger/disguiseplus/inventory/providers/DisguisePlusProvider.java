package net.pinger.disguiseplus.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DisguisePlusProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public DisguisePlusProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // Get skull for this player
        ItemStack skull = new ItemBuilder(this.dp.getSkullManager().getSkullFrom(player.getUniqueId()))
                .setName(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Users")
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view users.")
                .toItemStack();

        contents.set(1, 2, ClickableItem.of(skull, e -> {
            this.dp.getInventoryManager().getUserListProvider().open((Player) e.getWhoClicked());
        }));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        ItemStack skinPack = new ItemBuilder(FreshMaterial.GOLD_NUGGET.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE).getMaskedString("Skin Packs", state))
                .setLore(ChatColor.GRAY + "Click to open skin packs.")
                .toItemStack();

        contents.set(1, 6, ClickableItem.of(skinPack, e -> {
            this.dp.getInventoryManager().getSkinPacksProvider().open((Player) e.getWhoClicked());
        }));

        ItemStack settings = new ItemBuilder(FreshMaterial.COMMAND_BLOCK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE).getMaskedString("Settings", state))
                .setLore(ChatColor.GRAY + "Click to view the settings.")
                .toItemStack();

        contents.set(3, 4, ClickableItem.of(settings, e -> {
            this.dp.getInventoryManager().getSettingsProvider().open((Player) e.getWhoClicked());
        }));
    }
}
