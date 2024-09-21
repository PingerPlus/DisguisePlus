package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.pagination.GuiPagination;
import io.pnger.gui.provider.GuiProvider;
import io.pnger.gui.slot.GuiIteratorType;
import java.util.function.Consumer;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.meta.PlayerMeta.Builder;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.DisguiseUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RankChooseProvider implements GuiProvider {

    private final DisguisePlus disguisePlus;
    private final List<Rank> ranks;
    private final Consumer<DisguiseUser> disguiseConsumer;

    public RankChooseProvider(DisguisePlus disguisePlus, List<Rank> ranks, Consumer<DisguiseUser> disguiseConsumer) {
        this.disguisePlus = disguisePlus;
        this.ranks = ranks;
        this.disguiseConsumer = disguiseConsumer;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {
        GuiPagination page = contents.getPagination();
        DisguiseUser user = this.disguisePlus.getUserManager().getUser(player);

        // Create items for all ranks
        GuiItem[] items = new GuiItem[this.ranks.size() + (5 - (this.ranks.size() % 5))];
        for (int i = 0; i < this.ranks.size(); i++) {
            Rank rank = this.ranks.get(i);

            items[i] = GuiItem.of(this.getItemStack(rank), e -> {
                final Builder meta = user.getMetaBuilder();
                if (meta == null) {
                    return;
                }
                meta.rank(rank);
                this.disguiseConsumer.accept(user);
            });
        }
        for (int i = this.ranks.size(); i < items.length; i++) {
            items[i] = GuiItem.of(
                new ItemBuilder(XMaterial.BLACK_STAINED_GLASS_PANE)
                    .name("&r ")
                    .build()
            );
        }

        page.setItems(5, items);
        page.addToIterator(contents.newIterator(GuiIteratorType.HORIZONTAL, 0, 2));

        InventoryManager.addPageButtons(0, contents);
    }

    private ItemStack getItemStack(Rank rank) {
        // Create a new item stack with rank display name
        ItemBuilder builder = new ItemBuilder(XMaterial.SUNFLOWER);

        // Set the name of this item
        builder.name(ChatColor.translateAlternateColorCodes('&', rank.getDisplayName()));

        // Add lore
        builder.lore(ChatColor.GRAY + "Name:", ChatColor.YELLOW + rank.getName(), "",
                ChatColor.GRAY + "Permission: ",
                rank.getPermission().isEmpty() ? ChatColor.YELLOW + "None" : ChatColor.YELLOW + rank.getPermission());

        // Build the item
        return builder.build();
    }
}
