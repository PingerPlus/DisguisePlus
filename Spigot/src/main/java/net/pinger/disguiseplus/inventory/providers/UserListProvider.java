package net.pinger.disguiseplus.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class UserListProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public UserListProvider(DisguisePlus dp) {
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

        // All online users
        List<? extends net.pinger.disguiseplus.user.User> online = this.dp.getUserManager().getOnlinePlayers();
        ClickableItem[] items = new ClickableItem[online.size()];

        for (int i = 0; i < items.length; i++) {
            items[i] = ClickableItem.of(this.getItemStack(online.get(i), state), e -> {

            });
        }

        page.setItems(items);
        page.setItemsPerPage(36);
        page.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getItemStack(User user, int state) {
        ItemBuilder builder = new ItemBuilder(this.dp.getSkullManager().getSkullFrom(user.getId()).clone());

        // Set the name
        builder.setName(new TwoWayLoadingMask(ChatColor.LIGHT_PURPLE, ChatColor.DARK_PURPLE).getMaskedString(user.getDefaultName(), state));
        builder.setLore("");

        return builder.toItemStack();
    }
}
