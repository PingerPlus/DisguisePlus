package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.chat.SetFormatPrompt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DMProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public DMProvider(DisguisePlus dp) {
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

        String toggle = this.dp.getSettings().isOverride() ? "disable" : "enable";

        ItemStack tgl = new ItemBuilder(FreshMaterial.EXPERIENCE_BOTTLE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Chat Override", state))
                .setLore(ChatColor.AQUA + "Left Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + toggle + ".",
                        ChatColor.AQUA + "Right Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + "change" + ChatColor.GRAY + " the format.", Strings.EMPTY,
                        ChatColor.GRAY + "Use this option if you don't have another", ChatColor.GRAY + "chat manager plugin.", Strings.EMPTY,
                        ChatColor.GRAY + "Current Format:", ChatColor.YELLOW + this.dp.getSettings().getFormat())
                .toItemStack();

        contents.set(0, 4, ClickableItem.of(tgl, e -> {
            if (e.isLeftClick())
                this.dp.getSettings().reverseOverride();

            if (e.isRightClick()) {
                this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new SetFormatPrompt(this.dp), 30);
            }
        }));

        // The prefix
        ItemStack pf = new ItemBuilder(FreshMaterial.DIAMOND_HELMET.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Prefix", state))
                .setLore(ChatColor.AQUA + "Left Click" + ChatColor.GRAY + " to" + ChatColor.GOLD + " edit normal " + ChatColor.GRAY + "prefix.",
                        ChatColor.AQUA + "Right Click" + ChatColor.GRAY + " to" + ChatColor.GOLD + " edit disguised " + ChatColor.GRAY + "prefix.", Strings.EMPTY,
                        ChatColor.GRAY + "This option controls the value of the " + ChatColor.GOLD + "%dplus_prefix%" + ChatColor.GRAY + " placeholder.", Strings.EMPTY,
                        ChatColor.GRAY + "While Normal:", ChatColor.YELLOW + "&4%vault_rank%", Strings.EMPTY,
                        ChatColor.GRAY + "While Disguised:", ChatColor.YELLOW + "&5")
                .hideAllAttributes()
                .toItemStack();

        contents.set(2, 2, ClickableItem.of(pf, e -> {

        }));

        ItemStack sf = new ItemBuilder(FreshMaterial.CHAINMAIL_HELMET.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Suffix", state))
                .setLore(ChatColor.AQUA + "Left Click" + ChatColor.GRAY + " to" + ChatColor.GOLD + " edit normal " + ChatColor.GRAY + "suffix.",
                        ChatColor.AQUA + "Right Click" + ChatColor.GRAY + " to" + ChatColor.GOLD + " edit disguised " + ChatColor.GRAY + "suffix.", Strings.EMPTY,
                        ChatColor.GRAY + "This option controls the value of the " + ChatColor.GOLD + "%dplus_suffix%" + ChatColor.GRAY + " placeholder.", Strings.EMPTY,
                        ChatColor.GRAY + "While Normal:", ChatColor.YELLOW + "", Strings.EMPTY,
                        ChatColor.GRAY + "While Disguised:", ChatColor.YELLOW + "")
                .hideAllAttributes()
                .toItemStack();

        contents.set(2, 6, ClickableItem.of(sf, e -> {

        }));


        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }
}
