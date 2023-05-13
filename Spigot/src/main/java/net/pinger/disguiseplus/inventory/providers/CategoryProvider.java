package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.prompts.CreatePackPrompt;
import net.pinger.disguiseplus.SkinFactory;
import net.pinger.disguiseplus.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CategoryProvider implements GuiProvider {

    private final String category;
    private final DisguisePlus dp;

    public CategoryProvider(String category, DisguisePlus dp) {
        this.category = category;
        this.dp = dp;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {

    }

    @Override
    public void update(Player player, GuiContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh every
        // 2 ticks
        if (refresh % 2 != 0)
            return;

        // Pagination
        GuiPagination page = contents.getPagination();
        SkinFactory factory = this.dp.getSkinFactory();

        // Get the skins
        List<? extends SkinPack> packs = factory.getSkinPacks(this.category);
        GuiItem[] items = new GuiItem[packs.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            SkinPack pack = packs.get(i);

            // Add the pack to the inventory
            items[i] = GuiItem.of(this.getSkinPack(pack), e -> {
                this.dp.getInventoryManager().getExactPackProvider(pack).open((Player) e.getWhoClicked());
            });
        }

        // Add the items to the inventory
        page.setItems(36, items);
        page.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 0));

        // Item Responsible
        // For creating new inventories
        // Within the prompt
        ItemStack cp = new ItemBuilder(XMaterial.COMPASS)
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "Create Pack")
                .lore(ChatColor.GRAY + "Click to create a new skin pack")
                .build();

        contents.setItem(5, 1, GuiItem.of(cp, e -> {
            this.dp.getConversation().createConversation((Player) e.getWhoClicked(), new CreatePackPrompt(this.dp, this.category), 25);
        }));

        // Deleting the skin pack item
        ItemStack dp = new ItemBuilder(XMaterial.TRIPWIRE_HOOK)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Delete Category")
                .lore(ChatColor.GRAY + "Click to delete this category")
                .build();

        contents.setItem(5, 7, GuiItem.of(dp, e -> {
            this.dp.getSkinFactory().deleteCategory(this.category);

            // Open the parent inventory
            this.dp.getInventoryManager().getSkinPacksProvider().open(e.getWhoClicked());
        }));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getSkinPack(SkinPack pack) {
        return new ItemBuilder(pack.getSkins().isEmpty() ? this.dp.getSkullManager().getDefaultPlayerSkull() : pack.getSkins().get(0).toSkull())
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + pack.getName())
                .lore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view " + ChatColor.GOLD + pack.getSkins().size() + ChatColor.GRAY + " skins.")
                .build();
    }
}
