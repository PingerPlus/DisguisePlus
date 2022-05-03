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
import net.pinger.disguise.factory.SimpleSkinFactory;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.packs.CreateCategoryPrompt;
import net.pinger.disguise.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            if (simpleSkinFactory.getSkinPacks(category).isEmpty())
                items[i++] = ClickableItem.of(this.getItemFromSkin(this.dp.getSkullManager().getDefaultPlayerSkull(), category, state), e -> {
                    this.dp.getInventoryManager().getCategoryProvider(category).open((Player) e.getWhoClicked());
                });
            else
                items[i++] = ClickableItem.of(this.getItemFromPack(simpleSkinFactory.getSkinPacks(category).get(0), state), e -> {
                    this.dp.getInventoryManager().getCategoryProvider(category).open((Player) e.getWhoClicked());
                });
        }

        page.setItemsPerPage(21);
        page.setItems(items);

        SlotIterator iterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1);
        for (int n = 1; n < 4; n++) {
            for (int m = 0; m < 9; m++) {
                if (m == 0 || m == 8)
                    iterator.blacklist(n, m);
            }
        }

        page.addToIterator(iterator);

        ItemStack cat = new ItemBuilder(FreshMaterial.COMPASS.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Create Category", state))
                .setLore(ChatColor.GRAY + "Click to create a new category")
                .toItemStack();

        contents.set(5, 1, ClickableItem.of(cat, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreateCategoryPrompt(this.dp), 25);
        }));

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

    private ItemStack getItemFromSkin(ItemStack s, String category, int state) {
        ItemBuilder stack = new ItemBuilder(s.clone());

        // Set stack
        stack.setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString(category, state));

        // Set lore
        stack.setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to view the skin packs.");

        return stack.toItemStack();
    }
}
