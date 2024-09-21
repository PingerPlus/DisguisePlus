package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DisguisePlusProvider implements GuiProvider {

    private final DisguisePlus dp;

    public DisguisePlusProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        // Get skull for this player
        final ItemStack skull = new ItemBuilder(this.dp.getSkullManager().getSkullFrom(player.getUniqueId()))
                .name(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Users")
                .lore(ChatColor.GRAY + "Coming soon...")
                .build();

        contents.setItem(1, 2, GuiItem.of(skull));

        // Settings
        final ItemStack settings = new ItemBuilder(XMaterial.COMMAND_BLOCK)
                .name(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + "Settings")
                .lore(ChatColor.GRAY + "Coming soon...")
                .build();

        contents.setItem(3, 4, GuiItem.of(settings));
    }

    @Override
    public void update(Player player, GuiContents contents) {
        final int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh every
        // 2 ticks
        if (refresh % 2 != 0)
            return;

        // Item for viewing
        // All skin packs
        final ItemStack skinPack = new ItemBuilder(XMaterial.GOLD_NUGGET)
                .name(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Skin Packs")
                .lore(ChatColor.GRAY + "Click to open skin packs.")
                .build();

        contents.setItem(1, 6, GuiItem.of(skinPack, e -> {
            this.dp.getInventoryManager().getSkinPacksProvider().open((Player) e.getWhoClicked());
        }));
    }
}
