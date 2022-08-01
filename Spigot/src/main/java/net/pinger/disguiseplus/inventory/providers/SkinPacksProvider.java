package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.prompts.CreateCategoryPrompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.contents.InventoryPagination;
import org.intelligent.inventories.contents.InventorySlotIterator;
import org.intelligent.inventories.contents.IteratorType;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

public class SkinPacksProvider implements IntelligentProvider {

    private final DisguisePlus dp;

    public SkinPacksProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh
        // Every 2 ticks
        if (refresh % 2 != 0)
            return;

        // Pagination
        InventoryPagination page = contents.getPagination();

        SkinFactory factory = this.dp.getSkinFactory();
        IntelligentItem[] items = new IntelligentItem[factory.getCategories().size()];

        // Get the default skull
        ItemStack defaultSkull = this.dp.getSkullManager().getDefaultPlayerSkull();

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
            items[i++] = IntelligentItem.createNew(itemStack, e -> {
                this.dp.getInventoryManager().getCategoryProvider(category).open(e.getWhoClicked());
            });
        }

        // Add all items to an iterator
        page.setItemsPerPage(21);
        page.setItems(items);

        // Blacklist certain slots
        InventorySlotIterator iterator = contents.newIterator(IteratorType.HORIZONTAL, 1, 1);
        iterator.blacklistColumn(0);
        iterator.blacklistColumn(8);
        page.addToIterator(iterator);

        // Item for creating categories
        // Through prompts
        ItemStack cat = new ItemBuilder(XMaterial.COMPASS)
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Create Category")
                .lore(ChatColor.GRAY + "Click to create a new category")
                .build();

        contents.setItem(5, 1, IntelligentItem.createNew(cat, e -> {
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
