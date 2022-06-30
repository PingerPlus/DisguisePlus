package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.item.ItemBuilder;
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

public class UserListProvider implements IntelligentProvider {

    private final DisguisePlus dp;

    public UserListProvider(DisguisePlus dp) {
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

        // All online users
        List<? extends User> online = this.dp.getUserManager().getOnlinePlayers();
        IntelligentItem[] items = new IntelligentItem[online.size()];

        for (int i = 0; i < items.length; i++) {
            items[i] = IntelligentItem.createNew(this.getItemStack(online.get(i)), e -> {

            });
        }

        page.setItems(items);
        page.setItemsPerPage(36);
        page.addToIterator(contents.newIterator(IteratorType.HORIZONTAL, 0, 0));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemStack(User user) {
        ItemBuilder builder = new ItemBuilder(this.dp.getSkullManager().getSkullFrom(user.getId()).clone());

        // Set the name
        builder.setName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + user.getDefaultName());
        builder.setLore("");

        return builder.toItemStack();
    }
}
