package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.packs.CreateSkinImagePrompt;
import net.pinger.disguise.prompts.packs.CreateSkinNamePrompt;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddSkinProvider implements InventoryProvider {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public AddSkinProvider(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
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

        ItemStack name = new ItemBuilder(FreshMaterial.MUSIC_DISC_11.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("From Player Name", state))
                .setLore(ChatColor.GRAY + "Create a new skin from a players name")
                .hideAllAttributes()
                .toItemStack();

        contents.set(1, 3, ClickableItem.of(name, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreateSkinNamePrompt(this.dp, this.pack), 25);
        }));


        ItemStack url = new ItemBuilder(FreshMaterial.MUSIC_DISC_13.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("From Image URL", state))
                .setLore(ChatColor.GRAY + "Create a new skin from an image url")
                .hideAllAttributes()
                .toItemStack();

        contents.set(1, 5, ClickableItem.of(url, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreateSkinImagePrompt(this.dp, this.pack), 25);
        }));

        SimpleInventoryManager.addReturnButton(3, 4, contents);
    }
}
