package net.pinger.disguiseplus.inventory.providers;

import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.inventory.SimpleInventoryManager;
import net.pinger.disguiseplus.prompts.CreateSkinImagePrompt;
import net.pinger.disguiseplus.prompts.CreateSkinNamePrompt;
import net.pinger.disguiseplus.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.provider.IntelligentProvider;

public class AddSkinProvider implements IntelligentProvider {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public AddSkinProvider(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public void initialize(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.getProperty("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        // Refresh every
        // 2 ticks
        if (refresh % 2 != 0)
            return;

        // Allows us to catch a skin
        // From Player Name specified
        // In the given prompt
        ItemStack name = new ItemBuilder(XMaterial.MUSIC_DISC_11)
                .name(ChatColor.DARK_AQUA + ChatColor.BOLD.toString() + "From Player Name")
                .lore(ChatColor.GRAY + "Create a new skin from a players name")
                .flag()
                .build();

        contents.setItem(1, 3, IntelligentItem.createNew(name, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreateSkinNamePrompt(this.dp, this.pack), 25);
        }));

        // Allows us to catch a skin
        // From an Image Url which gets specified
        // In the prompt
        ItemStack url = new ItemBuilder(XMaterial.MUSIC_DISC_13)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "From Image URL")
                .lore(ChatColor.GRAY + "Create a new skin from an image url")
                .flag()
                .build();

        contents.setItem(1, 5, IntelligentItem.createNew(url, e -> {
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new CreateSkinImagePrompt(this.dp, this.pack), 25);
        }));

        SimpleInventoryManager.addReturnButton(3, 4, contents);
    }
}
