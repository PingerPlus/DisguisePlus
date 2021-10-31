package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.skin.SkinPack;
import net.pinger.disguise.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class SkinPacksProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public SkinPacksProvider(DisguisePlus dp) {
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

        SimpleSkinFactory simpleSkinFactory = (SimpleSkinFactory) this.dp.getSkinFactory();
        ClickableItem[] items = new ClickableItem[simpleSkinFactory.getSkinCategories().size()];

        int i = 0;
        for (String category : simpleSkinFactory.getSkinCategories()) {
            SkinPack pack = simpleSkinFactory.getSkinPacks(category).get(0);

            items[i++] = ClickableItem.of(this.getItemFromPack(pack, state), e -> {
                this.dp.getInventoryManager().getCategoryProvider(category).open((Player) e.getWhoClicked());
            });
        }

        page.setItemsPerPage(36);
        page.setItems(items);
        page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemFromPack(SkinPack pack, int state) {
        ItemBuilder stack = new ItemBuilder(pack.getSkins().get(0).toSkull().clone());

        // Set stack
        stack.setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString(pack.getCategory(), state));

        // Set lore
        stack.setLore(String.format(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view %s skin packs",
                this.dp.getSkinFactory().getSkinPacks(pack.getCategory()).size()));

        return stack.toItemStack();
    }
}
