package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.common.lang.Lists;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DSProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public DSProvider(DisguisePlus dp) {
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

        List<String> dbl = Lists.newArrayList();
        dbl.add(ChatColor.GRAY + "Click to edit the settings");

        if (this.dp.getSQLDatabase().isDatabaseSetup()) {
            dbl.add("");
            dbl.add(ChatColor.GRAY + "Status: ");
            dbl.add(ChatColor.YELLOW + "Already Connected");
        }

        // MySQL Database
        ItemStack db = new ItemBuilder(FreshMaterial.ENDER_CHEST.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("Database", state))
                .setLore(dbl)
                .toItemStack();

        contents.set(0, 5, ClickableItem.of(db, e -> {
            Player p = (Player) e.getWhoClicked();

            // Checking if they are OP and they can change the Database
            if (!p.isOp())
                return;

            if (this.dp.getSQLDatabase().isDatabaseSetup()) {
                return;
            }

            this.dp.getInventoryManager().getDatabaseProvider().open(p);
        }));

        // Disabled World
        ItemStack dw = new ItemBuilder(FreshMaterial.OBSIDIAN.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("General Settings", state))
                .setLore(ChatColor.GRAY + "Click to change the settings")
                .hideAllAttributes()
                .addEnchantment(Enchantment.LURE)
                .toItemStack();

        contents.set(0, 4, ClickableItem.of(dw, e -> {
            this.dp.getInventoryManager().getGeneralSettingsProvider().open((Player) e.getWhoClicked());
        }));

        // Coming soon
        ItemStack cs = new ItemBuilder(FreshMaterial.CRAFTING_TABLE.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("Disguise Settings", state))
                .toItemStack();

        contents.set(0, 3, ClickableItem.empty(cs));

        // Cooldown Manager
        ItemStack cm = new ItemBuilder(FreshMaterial.CLOCK.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Cooldown Manager", state))
                .setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to add a cooldown.")
                .toItemStack();

        contents.set(2, 2, ClickableItem.of(cm, e -> {
            this.dp.getInventoryManager().getCooldownProvider().open((Player) e.getWhoClicked());
        }));

        // Nick Creator
        ItemStack nc = new ItemBuilder(Material.STICK)
                .setName(new TwoWayLoadingMask(ChatColor.DARK_AQUA, ChatColor.AQUA).getMaskedString("Nick Creator", state))
                .setLore(ChatColor.AQUA + "Click " + ChatColor.GRAY + "to" + ChatColor.GOLD + " choose " + ChatColor.GRAY + "a custom pattern for your skins.")
                .toItemStack();

        contents.set(2, 6, ClickableItem.of(nc, e -> {
            this.dp.getInventoryManager().getCreatorProvider().open((Player) e.getWhoClicked());
        }));

        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }
}
