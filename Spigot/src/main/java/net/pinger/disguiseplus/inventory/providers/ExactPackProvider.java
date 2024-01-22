package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.prompts.ConfirmDeletePackPrompt;
import net.pinger.disguiseplus.statistic.SkinStatistic;
import net.pinger.disguiseplus.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class ExactPackProvider implements GuiProvider {

    private final SkinPack pack;
    private final DisguisePlus dp;

    public ExactPackProvider(SkinPack pack, DisguisePlus dp) {
        this.pack = pack;
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        GuiPagination page = contents.getPagination();
        User user = this.dp.getUserManager().getUser(player);

        // Get the skins
        List<Skin> skins = this.pack.getSkins();
        GuiItem[] items = new GuiItem[skins.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            Skin skin = skins.get(i);

            items[i] = GuiItem.of(this.getSkinPack(skin, (i + 1)), e -> {
                // Here perform action
                // For every skin
                if (user.isDisguised()) {
                    user.sendMessage("player.currently-disguised");
                    return;
                }

                // Send the confirmation message
                this.dp.getProvider().updatePlayer(player, skin);
                user.addStatistic(new SkinStatistic(skin));
                user.sendMessage("player.skin-set", this.pack.getName());
            });
        }

        page.setItems(36, items);
        page.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, GuiContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh
        // Every 2 ticks
        if (refresh % 2 != 0)
            return;

        // Add a skin to this inventory
        ItemStack cre = new ItemBuilder(XMaterial.COMPASS)
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Add Skin")
                .lore(ChatColor.GRAY + "Click to add a new skin")
                .build();

        contents.setItem(5, 1, GuiItem.of(cre, e -> {
            this.dp.getInventoryManager().getAddSkinProvider(this.pack).open((Player) e.getWhoClicked());
        }));

        // Delete this skin pack
        ItemStack dl = new ItemBuilder(XMaterial.TRIPWIRE_HOOK)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Delete Skin Pack")
                .lore(ChatColor.GRAY + "Click to delete this pack")
                .build();

        contents.setItem(5, 7, GuiItem.of(dl, e -> {
            this.dp.getConversation().createConversation((Player) e.getWhoClicked(), new ConfirmDeletePackPrompt(this.dp, this.pack));
        }));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getSkinPack(Skin skin, int index) {
        return new ItemBuilder(skin.toSkull())
                .name(ChatColor.GOLD + ChatColor.BOLD.toString() + this.pack.getName() + " > " + ChatColor.RESET + ChatColor.YELLOW + index)
                .lore(ChatColor.GRAY + "Click here to " + ChatColor.AQUA + "apply" + ChatColor.GRAY + " this skin", "",
                        ChatColor.GRAY + "Times Worn: ", ChatColor.AQUA + "Unknown", "",
                        ChatColor.GRAY + "Average Time Worn: ", ChatColor.AQUA + "Unknown")
                .build();
    }
}
