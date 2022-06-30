package net.pinger.disguiseplus.inventory;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.SkinPack;
import net.pinger.disguiseplus.inventory.providers.*;
import net.pinger.disguiseplus.item.FreshMaterial;
import net.pinger.disguiseplus.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.intelligent.inventories.IntelligentInventory;
import org.intelligent.inventories.IntelligentInventoryBuilder;
import org.intelligent.inventories.contents.InventoryContents;
import org.intelligent.inventories.contents.InventoryPagination;
import org.intelligent.inventories.item.IntelligentItem;
import org.intelligent.inventories.manager.IntelligentManager;

public class SimpleInventoryManager {

    // Disguise instance
    private final DisguisePlus disguise;
    private final IntelligentManager inventoryManager;

    public SimpleInventoryManager(DisguisePlus disguise) {
        this.disguise = disguise;

        // Initializing the IM
        this.inventoryManager = new IntelligentManager(disguise);
    }

    public IntelligentInventory getDisguisePlusProvider() {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new DisguisePlusProvider(this.disguise))
                .setTitle(ChatColor.DARK_GRAY + "DisguisePlus 2.0.0")
                .setManager(this.inventoryManager)
                .setSize(5, 9)
                .build();
    }

    public IntelligentInventory getSkinPacksProvider() {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new SkinPacksProvider(this.disguise))
                .setTitle(ChatColor.DARK_GRAY + "Skin Packs > Category")
                .setManager(this.inventoryManager)
                .setParent(this.getDisguisePlusProvider())
                .build();
    }

    public IntelligentInventory getCategoryProvider(String category) {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new CategoryProvider(category, this.disguise))
                .setTitle(ChatColor.DARK_GRAY + "Skin Packs > " + category)
                .setParent(this.getSkinPacksProvider())
                .setManager(this.inventoryManager)
                .build();
    }

    public IntelligentInventory getExactPackProvider(SkinPack pack) {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new ExactPackProvider(pack, this.disguise))
                .setTitle(ChatColor.DARK_GRAY + String.format("%s > %s", pack.getCategory(), pack.getName()))
                .setParent(this.getCategoryProvider(pack.getCategory()))
                .setManager(this.inventoryManager)
                .build();
    }

    public IntelligentInventory getAddSkinProvider(SkinPack pack) {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new AddSkinProvider(this.disguise, pack))
                .setManager(this.inventoryManager)
                .setTitle(String.format(ChatColor.DARK_GRAY + "%s > Add Skin", pack.getName()))
                .setSize(4, 9)
                .setParent(this.getExactPackProvider(pack))
                .build();
    }

    public IntelligentInventory getUserListProvider() {
        return IntelligentInventoryBuilder.newBuilder()
                .setProvider(new UserListProvider(this.disguise))
                .setManager(this.inventoryManager)
                .setTitle(ChatColor.DARK_GRAY + "User List")
                .setParent(this.getDisguisePlusProvider())
                .build();
    }

    public static void addPageButtons(int row, InventoryContents contents) {
        // Pagination
        InventoryPagination p = contents.getPagination();

        if (!p.isLast()) {
            // Get the item
            ItemStack next = new ItemBuilder(Material.ARROW)
                    .setName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Next Page")
                    .toItemStack();

            contents.setItem(row, 8, IntelligentItem.createNew(next, e -> {
                contents.getIntelligentInventory().open((Player) e.getWhoClicked(), p.nextPage().getPage());
            }));
        } else {
            contents.setItem(row, 8, IntelligentItem.createNew(new ItemStack(Material.AIR)));
        }

        if (!p.isFirst()) {
            ItemStack previous = new ItemBuilder(Material.ARROW)
                    .setName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Previous Page")
                    .toItemStack();

            contents.setItem(row, 0, IntelligentItem.createNew(previous, e -> {
                contents.getIntelligentInventory().open((Player) e.getWhoClicked(), p.previousPage().getPage());
            }));
        } else {
            contents.setItem(row, 0, IntelligentItem.createNew(new ItemStack(Material.AIR)));
        }
    }

    public static void addReturnButton(int row, int col, InventoryContents contents) {
        ItemStack stack = new ItemBuilder(Material.OAK_SIGN)
                .setName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Back")
                .setLore(ChatColor.GRAY + "Click to go back")
                .toItemStack();

        // Creating the ClickableItem
        IntelligentItem item = IntelligentItem.createNew(stack, e -> {
            if (e.isRightClick() || e.isShiftClick())
                return;

            if (e.getClick() == ClickType.NUMBER_KEY) {
                return;
            }

            contents.getIntelligentInventory().getParent().ifPresent(inv -> {
                inv.open((Player) e.getWhoClicked());
            });

            if (!contents.getIntelligentInventory().getParent().isPresent()) {
                e.getWhoClicked().closeInventory();
            }
        });

        // Adding it to the inventory
        contents.setItem(row, col, item);
    }

}
