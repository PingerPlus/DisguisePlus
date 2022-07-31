package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguise.Skin;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.DisguisePlusAPI;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.prompts.ConfirmDeletePackPrompt;
import net.pinger.disguiseplus.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.contents.InventoryPagination;
import org.intelligent.inventories.contents.IteratorType;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

import java.util.List;

public class ExactPackProvider implements IntelligentProvider {

    private final SkinPack pack;
    private final DisguisePlus dp;

    public ExactPackProvider(SkinPack pack, DisguisePlus dp) {
        this.pack = pack;
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {
        // Pagination
        InventoryPagination page = contents.getPagination();
        User user = this.dp.getUserManager().getUser(player);

        // Get the skins
        List<Skin> skins = this.pack.getSkins();
        IntelligentItem[] items = new IntelligentItem[skins.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            Skin skin = skins.get(i);

            items[i] = IntelligentItem.createNew(this.getSkinPack(skin, (i + 1)), e -> {
                // Here perform action
                // For every skin
                DisguisePlusAPI.getDisguiseManager().applySkin(player, skin);

                // Send the confirmation message
                user.sendMessage("player.skin-set", this.pack.getName());
            });
        }

        page.setItemsPerPage(36);
        page.setItems(items);
        page.addToIterator(contents.newIterator(IteratorType.HORIZONTAL, 0, 0));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
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

        contents.setItem(5, 1, IntelligentItem.createNew(cre, e -> {
            this.dp.getInventoryManager().getAddSkinProvider(this.pack).open((Player) e.getWhoClicked());
        }));

        // Delete this skin pack
        ItemStack dl = new ItemBuilder(XMaterial.TRIPWIRE_HOOK)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Delete Skin Pack")
                .lore(ChatColor.GRAY + "Click to delete this pack")
                .build();

        contents.setItem(5, 7, IntelligentItem.createNew(dl, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new ConfirmDeletePackPrompt(this.dp, this.pack));
        }));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
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
