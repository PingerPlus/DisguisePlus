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
import net.pinger.disguiseplus.prompts.world.AddWorldPrompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class WorldListProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public WorldListProvider(DisguisePlus dp) {
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

        // Getting the pagination
        Pagination pagination = contents.pagination();

        Set<String> world = this.dp.getSettings().getWorlds();
        ClickableItem[] items = new ClickableItem[world.size()];
        int i = 0;

        for (String s : world) {
            items[i++] = ClickableItem.of(this.getWorldStack(s, state), e -> {
                if (e.getClick() == ClickType.MIDDLE) {
                    this.dp.getSettings().removeWorld(s);
                }
            });
        }

        pagination.setItems(items);
        pagination.setItemsPerPage(9 * 4);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        // Add new world
        ItemStack adw = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                        .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Add World", state))
                        .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to add a new world to the " + ChatColor.GOLD + "ban list.")
                        .toItemStack();

        contents.set(5, 1, ClickableItem.of(adw, e -> {
            e.getWhoClicked().closeInventory();

            // Start the new prompt
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new AddWorldPrompt(this.dp));
        }));

        // Return
        SimpleInventoryManager.addReturnButton(5, 4, contents);
        SimpleInventoryManager.addPageButtons(5, contents);
    }

    private ItemStack getWorldStack(String name, int state) {
        ItemBuilder builder = new ItemBuilder(FreshMaterial.GRAY_STAINED_GLASS_PANE.toMaterial());

        // Name
        builder.setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString(name, state));

        // Lore
        builder.setLore(ChatColor.AQUA + "Middle-Click" + ChatColor.GRAY + " to " + ChatColor.YELLOW + "remove" + ChatColor.GRAY + " the ban for this world.");

        return builder.toItemStack();
    }
}
