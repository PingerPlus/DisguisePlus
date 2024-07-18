package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import io.pnger.gui.slot.GuiSlotIterator;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinFactory;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.prompts.CreateCategoryPrompt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SkinPacksProvider implements GuiProvider {

    private final DisguisePlus dp;

    public SkinPacksProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void update(Player player, GuiContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh
        // Every 2 ticks
        if (refresh % 2 != 0)
            return;

        // Pagination
        GuiPagination page = contents.getPagination();

        SkinFactory factory = this.dp.getSkinFactory();
        GuiItem[] items = new GuiItem[factory.getCategories().size()];

        // Get the default skull
        ItemStack defaultSkull = this.dp.getSkullManager().getSkullFrom(player.getUniqueId());

        // Loop through every category
        // And add it to the inventory
        int i = 0;
        for (String category : factory.getCategories()) {
            // Get the necessary itemStack
            ItemStack itemStack;

            // Update depending on the condition
            if (!factory.getSkinPacks(category).isEmpty()) {
                // Get the first skin pack
                SkinPack skinPack = factory.getSkinPacks(category).get(0);

                // Check if it contains any skins and if not
                // Get the default skull
                if (skinPack.getSkins().isEmpty()) {
                    itemStack = this.getItemFromSkin(defaultSkull, category);
                } else {
                    itemStack = this.getItemFromPack(skinPack);
                }
            } else {
                itemStack = this.getItemFromSkin(defaultSkull, category);
            }

            // Add the item to the inventory
            items[i++] = GuiItem.of(itemStack, e -> {
                this.dp.getInventoryManager().getCategoryProvider(category).open(e.getWhoClicked());
            });
        }

        // Add all items to an iterator
        page.setItems(21, items);

        // Blacklist certain slots
        GuiSlotIterator iterator = contents.newIterator(GuiIteratorType.HORIZONTAL, 1, 1);
        iterator.blacklistColumn(0);
        iterator.blacklistColumn(8);
        page.addToIterator(iterator);

        // Item for creating categories
        // Through prompts
        ItemStack cat = new ItemBuilder(XMaterial.COMPASS)
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Create Category")
                .lore(ChatColor.GRAY + "Click to create a new category")
                .build();

        contents.setItem(5, 1, GuiItem.of(cat, e -> {
            this.dp.getConversation().createConversation((Player) e.getWhoClicked(), new CreateCategoryPrompt(this.dp), 25);
        }));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemFromPack(SkinPack pack) {
        return new ItemBuilder(pack.getSkins().get(0).toSkull().clone())
                .name(ChatColor.GOLD + ChatColor.BOLD.toString() + pack.getCategory())
                .lore(String.format(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view %s skin packs", this.dp.getSkinFactory().getSkinPacks(pack.getCategory()).size()))
                .build();
    }

    private ItemStack getItemFromSkin(ItemStack s, String category) {
        return new ItemBuilder(s.clone())
                .name(ChatColor.GOLD + ChatColor.BOLD.toString() + category)
                .lore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view the skin packs.")
                .build();
    }
}
