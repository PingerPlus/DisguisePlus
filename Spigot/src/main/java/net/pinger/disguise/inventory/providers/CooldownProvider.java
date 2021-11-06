package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.cooldown.CooldownManager;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.cooldown.SetCooldownPermission;
import net.pinger.disguise.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joda.time.DateTime;

public class CooldownProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public CooldownProvider(DisguisePlus dp) {
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

        CooldownManager cld = this.dp.getSettings().getCooldownManager();

        // Bypass
        ItemStack bp = new ItemBuilder(FreshMaterial.DIAMOND_SWORD.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Bypass Permission", state))
                .setLore(ChatColor.GRAY + "Players that have " + ChatColor.YELLOW + cld.getPermission() + ChatColor.GRAY + " permission",
                        ChatColor.GRAY + "will be able to bypass this feature.", Strings.EMPTY,
                        ChatColor.AQUA + "Click " + ChatColor.GRAY + "to change.")
                .hideAllAttributes()
                .toItemStack();

        contents.set(1, 2, ClickableItem.of(bp, e -> {
            // The conversation util
            this.dp.getConversationUtil().createConversation((Player) e.getWhoClicked(), new SetCooldownPermission(this.dp), 25);
        }));

        // Interval
        ItemStack it = new ItemBuilder(FreshMaterial.EMERALD_BLOCK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Set Interval", state))
                .setLore(ChatColor.GRAY + "Sets the duration of the " + ChatColor.GOLD + "cooldowns.", Strings.EMPTY,
                        ChatColor.GRAY + "Current Interval:", ChatColor.YELLOW +
                                DateUtil.getFormattedTime(DateTime.now(), DateTime.now().plus(cld.getInterval() * 1000)))
                .toItemStack();

        contents.set(1, 6, ClickableItem.of(it, e -> {
            this.dp.getInventoryManager().getIntervalProvider().open((Player) e.getWhoClicked());
        }));

        String eb = cld.isEnabled() ? "disable." : "enable.";

        // Enable
        ItemStack enable = new ItemBuilder(FreshMaterial.REDSTONE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Configure Cooldowns", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to " + ChatColor.GOLD + eb)
                .toItemStack();

        contents.set(2, 4, ClickableItem.of(enable, e -> {
            cld.setEnabled(!cld.isEnabled());
        }));

        SimpleInventoryManager.addReturnButton(4, 4, contents);
    }
}
