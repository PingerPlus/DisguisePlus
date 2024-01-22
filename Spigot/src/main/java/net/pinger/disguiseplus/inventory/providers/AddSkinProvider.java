package net.pinger.disguiseplus.inventory.providers;

import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.provider.GuiProvider;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.inventory.InventoryManager;
import net.pinger.disguiseplus.prompts.CreateSkinImagePrompt;
import net.pinger.disguiseplus.prompts.CreateSkinNamePrompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddSkinProvider implements GuiProvider {

    private final DisguisePlus dp;
    private final SkinPack pack;

    public AddSkinProvider(DisguisePlus dp, SkinPack pack) {
        this.dp = dp;
        this.pack = pack;
    }

    @Override
    public void initialize(Player player, GuiContents contents) {

    }

    @Override
    public void update(Player player, GuiContents contents) {
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

        contents.setItem(1, 3, GuiItem.of(name, e -> {
            this.dp.getConversation().createConversation((Player) e.getWhoClicked(), new CreateSkinNamePrompt(this.dp, this.pack), 25);
        }));

        // Allows us to catch a skin
        // From an Image Url which gets specified
        // In the prompt
        ItemStack url = new ItemBuilder(XMaterial.MUSIC_DISC_13)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "From Image URL")
                .lore(ChatColor.GRAY + "Create a new skin from an image url")
                .flag()
                .build();

        contents.setItem(1, 5, GuiItem.of(url, e -> {
            this.dp.getConversation().createConversation((Player) e.getWhoClicked(), new CreateSkinImagePrompt(this.dp, this.pack), 25);
        }));

        InventoryManager.addReturnButton(3, 4, contents);
    }
}
