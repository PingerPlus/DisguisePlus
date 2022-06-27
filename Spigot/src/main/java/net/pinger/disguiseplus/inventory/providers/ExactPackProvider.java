package net.pinger.disguiseplus.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.prompts.ConfirmDeletePackPrompt;
import net.pinger.disguiseplus.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ExactPackProvider implements InventoryProvider {

    private final net.pinger.disguiseplus.SkinPack pack;
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
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);

        ItemStack cre = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Add Skin", state))
                .setLore(ChatColor.GRAY + "Click to add a new skin")
                .toItemStack();

        contents.set(5, 1, ClickableItem.of(cre, e -> {
            this.dp.getInventoryManager().getAddSkinProvider(this.pack).open((Player) e.getWhoClicked());
        }));

        ItemStack dl = new ItemBuilder(FreshMaterial.TRIPWIRE_HOOK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Delete Skin Pack", state))
                .setLore(ChatColor.GRAY + "Click to delete this pack")
                .toItemStack();

        contents.set(5, 7, ClickableItem.of(dl, e -> {
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
