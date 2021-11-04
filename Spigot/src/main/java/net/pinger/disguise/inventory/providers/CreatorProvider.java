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
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.nick.NickCharacter;
import net.pinger.disguise.nick.SimpleNickCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreatorProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public CreatorProvider(DisguisePlus dp) {
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

        // Get the pagination
        Pagination pagination = contents.pagination();

        // Get the creator
        SimpleNickCreator creator = this.dp.getSettings().getCreator();
        List<NickCharacter> flags = creator.getFlags();

        ClickableItem[] items = new ClickableItem[flags.size()];
        for (int i = 0; i < flags.size(); i++) {
            NickCharacter character = flags.get(i);

            items[i] = ClickableItem.of(this.getFromCharacter(character, i + 1), e -> {

            });
        }

        pagination.setItemsPerPage(items.length);
        pagination.setItems(items);
        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 0, 0));

        if (flags.size() < 16) {
            ItemStack cc = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                    .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Add a new character", state))
                    .toItemStack();

            contents.set(5, 1, ClickableItem.of(cc, e -> {

            }));

        } else {
            contents.set(5, 1, ClickableItem.empty(new ItemStack(Material.AIR)));
        }

        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }

    private ItemStack getFromCharacter(NickCharacter character, int count) {
        // Create the item stack
        // If the character is optional it will be blue stained, otherwise green
        FreshMaterial mat = character.isOptional() ? FreshMaterial.BLUE_STAINED_GLASS_PANE : FreshMaterial.GREEN_STAINED_GLASS_PANE;

        ItemBuilder builder = new ItemBuilder(mat.toMaterial(), (short) mat.getData());

        // Set the name
        builder.setName(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + count);

        String optional = character.isOptional() ? "Yes" : "No";

        // Lore
        builder.setLore(ChatColor.GRAY + "Order of this specific character:", ChatColor.YELLOW.toString() + count, Strings.EMPTY,
                ChatColor.GRAY + "Optional: ", ChatColor.YELLOW + optional, Strings.EMPTY,
                ChatColor.GRAY + "Type of Character: ", ChatColor.YELLOW + character.getFlag().getDescription());

        return builder.toItemStack();
    }
}
