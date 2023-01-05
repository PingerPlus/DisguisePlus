package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguise.Skin;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.contents.InventoryPagination;
import org.intelligent.inventories.contents.IteratorType;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

import java.util.List;

public class RankChooseProvider implements IntelligentProvider {

    private final DisguisePlus disguisePlus;
    private final List<Rank> ranks;

    public RankChooseProvider(DisguisePlus disguisePlus, List<Rank> ranks) {
        this.disguisePlus = disguisePlus;
        this.ranks = ranks;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {
        // Get user from this player
        InventoryPagination page = contents.getPagination();
        User user = this.disguisePlus.getUserManager().getUser(player);

        // Create items for all ranks
        IntelligentItem[] items = new IntelligentItem[this.ranks.size()];
        for (int i = 0; i < items.length; i++) {
            // Get rank for this loop
            Rank rank = this.ranks.get(i);

            items[i] = IntelligentItem.createNew(getItemStack(rank), e -> {
               // If clicked on the rank set the rank and call disguise
                user.setRank(rank);

                // Call disguise
                this.disguisePlus.getExtendedManager().disguise(player);
            });
        }

        page.setItems(items);
        page.setItemsPerPage(36);
        page.addToIterator(contents.newIterator(IteratorType.HORIZONTAL, 0, 0));

        InventoryManager.addReturnButton(5, 4, contents);
        InventoryManager.addPageButtons(5, contents);
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
