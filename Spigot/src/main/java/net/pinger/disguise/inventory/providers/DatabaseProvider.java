package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import joptsimple.internal.Strings;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.database.Database;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.prompts.database.*;
import net.pinger.disguise.utils.ConversationUtil;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DatabaseProvider implements InventoryProvider {

    // Instance of the main
    private final DisguisePlus dp;

    // Utils
    private final Database database;
    private final ConversationUtil conversationUtil;

    public DatabaseProvider(DisguisePlus disguisePlus) {
        this.dp = disguisePlus;

        // Load the utilities
        this.database = this.dp.getSQLDatabase();
        this.conversationUtil = this.dp.getConversationUtil();
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

        // MySQL Database
        ItemStack db = new ItemBuilder(FreshMaterial.ENDER_CHEST.toMaterial())
                .setName(new TwoWayLoadingMask(ChatColor.YELLOW, ChatColor.GOLD).getMaskedString("Database", state))
                .setLore(ChatColor.GRAY + "Click to show the settings")
                .toItemStack();

        contents.set(0, 5, ClickableItem.empty(db));

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

        contents.set(0, 3, ClickableItem.of(cs, e -> {
            this.dp.getInventoryManager().getDisguiseSettings().open((Player) e.getWhoClicked());
        }));

        // First we have the database name
        contents.set(2, 2, ClickableItem.of(this.fromName("Database", state, this.database.getDatabase()), e -> {
            e.getWhoClicked().closeInventory();
            this.conversationUtil.createConversation((Player) e.getWhoClicked(), new SetDatabasePrompt(this.dp));
        }));

        // Host
        contents.set(2, 4, ClickableItem.of(this.fromName("Host", state, this.database.getHost()), e -> {
            e.getWhoClicked().closeInventory();
            this.conversationUtil.createConversation((Player) e.getWhoClicked(), new SetHostPrompt(this.dp));
        }));

        // Username
        contents.set(2, 6, ClickableItem.of(this.fromName("Username", state, this.database.getUsername()), e -> {
            e.getWhoClicked().closeInventory();
            this.conversationUtil.createConversation((Player) e.getWhoClicked(), new SetUsernamePrompt(this.dp));
        }));

        // Password
        contents.set(3, 3, ClickableItem.of(this.fromName("Password", state, "Hidden"), e -> {
            e.getWhoClicked().closeInventory();
            this.conversationUtil.createConversation((Player) e.getWhoClicked(), new SetPasswordPrompt(this.dp));
        }));

        // Port
        contents.set(3, 5, ClickableItem.of(this.fromName("Port", state, this.database.getPort()), e -> {
            e.getWhoClicked().closeInventory();
            this.conversationUtil.createConversation((Player) e.getWhoClicked(), new SetPortPrompt(this.dp));
        }));

        // Return
        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }

    private ItemStack fromName(String name, int state, Object value) {
        // Creating the builder
        ItemBuilder builder = new ItemBuilder(FreshMaterial.PAPER.toMaterial());

        // Setting the name
        builder.setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString(name, state));

        // Lore
        String val = value == null ? "Not set" : value.toString();
        builder.setLore(ChatColor.AQUA + "Click" + ChatColor.GRAY + " to change this " + ChatColor.GOLD + "setting.", Strings.EMPTY, ChatColor.YELLOW + "Current Value:", ChatColor.GRAY + val);

        return builder.toItemStack();
    }

}
