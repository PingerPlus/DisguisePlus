package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguise.Skin;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.item.FreshMaterial;
import net.pinger.disguiseplus.item.ItemBuilder;
import net.pinger.disguiseplus.prompts.ConfirmDeletePackPrompt;
import net.pinger.disguiseplus.SkinPack;
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

        // Get the skins
        List<Skin> skins = this.pack.getSkins();
        IntelligentItem[] items = new IntelligentItem[skins.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            Skin skin = skins.get(i);

            items[i] = IntelligentItem.createNew(this.getSkinPack(skin), e -> {
                // Here perform action
                // For every skin
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
        ItemStack cre = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                .setName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Add Skin")
                .setLore(ChatColor.GRAY + "Click to add a new skin")
                .toItemStack();

        contents.setItem(5, 1, IntelligentItem.createNew(cre, e -> {
            this.dp.getInventoryManager().getAddSkinProvider(this.pack).open((Player) e.getWhoClicked());
        }));

        // Delete this skin pack
        ItemStack dl = new ItemBuilder(FreshMaterial.TRIPWIRE_HOOK.toMaterial())
                .setName(ChatColor.AQUA + ChatColor.BOLD.toString() +"Delete Skin Pack")
                .setLore(ChatColor.GRAY + "Click to delete this pack")
                .toItemStack();

        contents.setItem(5, 7, IntelligentItem.createNew(dl, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new ConfirmDeletePackPrompt(this.dp, this.pack));
        }));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getSkinPack(Skin skin) {
        ItemBuilder stack = new ItemBuilder(skin.toSkull());

        // The name
        stack.setName(" ");
        stack.setLore("");

        return stack.toItemStack();
    }
}
