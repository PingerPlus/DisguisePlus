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
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.skin.Skin;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExactPackProvider implements InventoryProvider {

    private final SkinPack pack;
    private final DisguisePlus dp;

    public ExactPackProvider(SkinPack pack, DisguisePlus dp) {
        this.pack = pack;
        this.dp = dp;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        // Pagination
        Pagination page = contents.pagination();

        // Get the skins
        List<Skin> skins = this.pack.getSkins();
        ClickableItem[] items = new ClickableItem[skins.size()];

        for (int i = 0; i < items.length; i++) {
            // Get the pack
            Skin skin = skins.get(i);

            items[i] = ClickableItem.of(this.getSkinPack(skin), e -> {

            });
        }

        page.setItemsPerPage(36);
        page.setItems(items);
        page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        // Get the item
        ItemStack next = new ItemBuilder(FreshMaterial.ARROW.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Next", contents.property("state", 1)))
                .toItemStack();
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

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
