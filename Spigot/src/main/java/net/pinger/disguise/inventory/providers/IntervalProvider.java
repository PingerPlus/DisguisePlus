package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.cooldown.CooldownManager;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.utils.DateUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

public class IntervalProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public IntervalProvider(DisguisePlus dp) {
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

        // Current Duration
        ItemStack dur = new ItemBuilder(FreshMaterial.EMERALD_BLOCK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Duration", state))
                .setLore(ChatColor.GRAY + DateUtil.getFormattedTime(DateTime.now(), DateTime.now().plus(cld.getInterval() * 1000)))
                .toItemStack();

        contents.set(2, 4, ClickableItem.empty(dur));

        // Add second
        ItemStack as = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Add Second", state))
                .setLore(ChatColor.GRAY + "Click to add a second")
                .toItemStack();

        contents.set(1, 7, ClickableItem.of(as, e -> {
            cld.increaseInterval(1);
        }));

        // Add minute
        ItemStack am = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Add Minute", state))
                .setLore(ChatColor.GRAY + "Click to add a minute")
                .toItemStack();

        contents.set(2, 7, ClickableItem.of(am, e -> {
            cld.increaseInterval(TimeUnit.MINUTES.toSeconds(1));
        }));

        // Add hour
        ItemStack ah = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Add Hour", state))
                .setLore(ChatColor.GRAY + "Click to add an hour")
                .toItemStack();

        contents.set(3, 7, ClickableItem.of(ah, e -> {
            cld.increaseInterval(TimeUnit.HOURS.toSeconds(1));
        }));

        // Add day
        ItemStack ad = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Add Day", state))
                .setLore(ChatColor.GRAY + "Click to add a day")
                .toItemStack();

        contents.set(4, 7, ClickableItem.of(ad, e -> {
            cld.increaseInterval(TimeUnit.DAYS.toSeconds(1));
        }));

        // Remove second
        ItemStack rs = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Remove Second", state))
                .setLore(ChatColor.GRAY + "Click to remove a second")
                .toItemStack();

        contents.set(1, 1, ClickableItem.of(rs, e -> {
            cld.decreaseIfPossible(TimeUnit.SECONDS, 1);
        }));

        // Remove minute
        ItemStack rm = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Remove Minute", state))
                .setLore(ChatColor.GRAY + "Click to remove a minute")
                .toItemStack();

        contents.set(2, 1, ClickableItem.of(rm, e -> {
            cld.decreaseIfPossible(TimeUnit.MINUTES, 1);
        }));

        // Remove an hour
        ItemStack rh = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Remove Hour", state))
                .setLore(ChatColor.GRAY + "Click to remove an hour")
                .toItemStack();

        contents.set(3, 1, ClickableItem.of(rh, e -> {
            cld.decreaseIfPossible(TimeUnit.HOURS, 1);
        }));

        // Add
        ItemStack rd = new ItemBuilder(FreshMaterial.STONE_BUTTON.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.DARK_GREEN, ChatColor.GREEN).getMaskedString("Remove Day", state))
                .setLore(ChatColor.GRAY + "Click to remove a day")
                .toItemStack();

        contents.set(4, 1, ClickableItem.of(rd, e -> {
            cld.decreaseIfPossible(TimeUnit.DAYS, 1);
        }));


        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }
}
