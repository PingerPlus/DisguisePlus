package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.item.FreshMaterial;
import net.pinger.disguiseplus.item.ItemBuilder;
import net.pinger.disguiseplus.prompts.CreatePackPrompt;
import net.pinger.disguiseplus.SkinFactory;
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

public class CategoryProvider implements IntelligentProvider {

    private final String category;
    private final DisguisePlus dp;

    public CategoryProvider(String category, DisguisePlus dp) {
        this.category = category;
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh every
        // 2 ticks
        if (refresh % 2 != 0)
            return;

        // Pagination
        InventoryPagination page = contents.getPagination();
        SkinFactory factory = this.dp.getSkinFactory();

        // Get the skins
        List<? extends SkinPack> packs = factory.getSkinPacks(this.category);
        IntelligentItem[] items = new IntelligentItem[packs.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            SkinPack pack = packs.get(i);

            // Add the pack to the inventory
            items[i] = IntelligentItem.createNew(this.getSkinPack(pack), e -> {
                this.dp.getInventoryManager().getExactPackProvider(pack).open((Player) e.getWhoClicked());
            });
        }

        // Add the items to the inventory
        page.setItemsPerPage(36);
        page.setItems(items);
        page.addToIterator(contents.newIterator(IteratorType.HORIZONTAL, 0, 0));

        // Item Responsible
        // For creating new inventories
        // Within the prompt
        ItemStack cp = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                .setName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Create Pack")
                .setLore(ChatColor.GRAY + "Click to create a new skin pack")
                .toItemStack();

        contents.setItem(5, 1, IntelligentItem.createNew(cp, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreatePackPrompt(this.dp, this.category), 25);
        }));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getSkinPack(SkinPack pack) {
        ItemBuilder stack = new ItemBuilder(
                pack.getSkins().isEmpty() ?
                        this.dp.getSkullManager().getDefaultPlayerSkull() : pack.getSkins().get(0).toSkull());

        // The name
        stack.setName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + pack.getName());
        stack.setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view " + ChatColor.GOLD + pack.getSkins().size() + ChatColor.GRAY + " skins.");

        return stack.toItemStack();
    }
}
