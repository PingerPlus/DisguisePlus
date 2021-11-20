package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SkinFactory;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.packs.CreatePackPrompt;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CategoryProvider implements InventoryProvider {

    private final String category;
    private final DisguisePlus dp;

    public CategoryProvider(String category, DisguisePlus dp) {
        this.category = category;
        this.dp = dp;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        // Pagination
        Pagination page = contents.pagination();
        SkinFactory factory = this.dp.getSkinFactory();

        // Get the skins
        List<? extends SkinPack> packs = factory.getSkinPacks(this.category);
        ClickableItem[] items = new ClickableItem[packs.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            SkinPack pack = packs.get(i);

            items[i] = ClickableItem.of(this.getSkinPack(pack, state), e -> {
                this.dp.getInventoryManager().getExactPackProvider(pack).open((Player) e.getWhoClicked());
            });
        }

        page.setItemsPerPage(36);
        page.setItems(items);
        page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        ItemStack cp = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Create Pack", state))
                .setLore(ChatColor.GRAY + "Click to create a new skin pack")
                .toItemStack();

        contents.set(5, 1, ClickableItem.of(cp, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreatePackPrompt(this.dp, this.category));
        }));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getSkinPack(SkinPack pack, int state) {
        ItemBuilder stack = new ItemBuilder(
                pack.getSkins().isEmpty() ?
                        this.dp.getSkullManager().getDefaultPlayerSkull() : pack.getSkins().get(0).toSkull());

        // The name
        stack.setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString(pack.getName(), state));
        stack.setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view " + ChatColor.GOLD + pack.getSkins().size() + ChatColor.GRAY + " skins.");

        return stack.toItemStack();
    }
}
