package net.pinger.disguise.inventory;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.InventoryListener;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.Pagination;
import net.pinger.bukkit.item.FreshMaterial;
import net.pinger.bukkit.item.ItemBuilder;
import net.pinger.bukkit.item.mask.impl.TwoWayLoadingMask;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.inventory.providers.*;
import net.pinger.disguise.skin.SkinPack;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class SimpleInventoryManager {

    // Disguise instance
    private final DisguisePlus disguise;

    // The inventory manager
    private final InventoryManager inventoryManager;

    public SimpleInventoryManager(DisguisePlus disguise) {
        this.disguise = disguise;

        // Initializing the IM
        this.inventoryManager = new InventoryManager(disguise);
        this.inventoryManager.init();
    }

    public SmartInventory getDatabaseProvider() {
       return SmartInventory.builder()
                .provider(new DatabaseProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "Settings > MySQL Database")
                .parent(this.getSettingsProvider())
                .manager(this.inventoryManager)
                .size(6, 9)
                .build();
    }

    public SmartInventory getSettingsProvider() {
        return SmartInventory.builder()
                .provider(new SettingsProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + String.format("Disguise Plus %s", this.disguise.getDescription().getVersion()))
                .parent(this.getDisguisePlusProvider())
                .listener(new InventoryListener<>(InventoryDragEvent.class, inv -> {

                }))
                .manager(this.inventoryManager)
                .size(6, 9)
                .build();
    }

    public SmartInventory getGeneralSettingsProvider() {
        return SmartInventory.builder()
                .provider(new GSProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "Settings > General Settings")
                .parent(this.getSettingsProvider())
                .manager(this.inventoryManager)
                .size(6, 9)
                .build();
    }

    public SmartInventory getWorldListProvider() {
        return SmartInventory.builder()
                .provider(new WorldListProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "General Settings > Worlds")
                .parent(this.getGeneralSettingsProvider())
                .manager(this.inventoryManager)
                .size(6, 9)
                .build();
    }

    public SmartInventory getDisplayProvider() {
        return SmartInventory.builder()
                .provider(new DMProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "General Settings > Display")
                .parent(this.getGeneralSettingsProvider())
                .manager(this.inventoryManager)
                .size(6, 9)
                .build();
    }

    public SmartInventory getDisguisePlusProvider() {
        return SmartInventory.builder()
                .provider(new DisguisePlusProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "DisguisePlus 2.0.0")
                .manager(this.inventoryManager)
                .size(5, 9)
                .build();
    }

    public SmartInventory getSkinPacksProvider() {
        return SmartInventory.builder()
                .provider(new SkinPacksProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "Skin Packs > Category")
                .manager(this.inventoryManager)
                .parent(this.getDisguisePlusProvider())
                .build();
    }

    public SmartInventory getCategoryProvider(String category) {
        return SmartInventory.builder()
                .provider(new CategoryProvider(category, this.disguise))
                .title(ChatColor.DARK_GRAY + "Skin Packs > " + category)
                .parent(this.getSkinPacksProvider())
                .manager(this.inventoryManager)
                .build();
    }

    public SmartInventory getExactPackProvider(SkinPack pack) {
        return SmartInventory.builder()
                .provider(new ExactPackProvider(pack, this.disguise))
                .title(ChatColor.DARK_GRAY + String.format("%s > %s", pack.getCategory(), pack.getName()))
                .parent(this.getCategoryProvider(pack.getCategory()))
                .manager(this.inventoryManager)
                .build();
    }

    public SmartInventory getDisguiseSettings() {
        return SmartInventory.builder()
                .provider(new DSProvider(this.disguise))
                .manager(this.inventoryManager)
                .title(ChatColor.DARK_GRAY + "Settings > Disguise")
                .parent(this.getSettingsProvider())
                .build();
    }

    public SmartInventory getCreatorProvider() {
        return SmartInventory.builder()
                .provider(new CreatorProvider(this.disguise))
                .manager(this.inventoryManager)
                .title(ChatColor.DARK_GRAY + "Disguise > Nick Creator")
                .parent(this.getDisguiseSettings())
                .build();
    }
    public SmartInventory getAddSkinProvider(SkinPack pack) {
        return SmartInventory.builder()
                .provider(new AddSkinProvider(this.disguise, pack))
                .manager(this.inventoryManager)
                .title(String.format(ChatColor.DARK_GRAY + "%s > Add Skin", pack.getName()))
                .size(4, 9)
                .parent(this.getExactPackProvider(pack))
                .build();
    }

    public SmartInventory getUserListProvider() {
        return SmartInventory.builder()
                .provider(new UserListProvider(this.disguise))
                .manager(this.inventoryManager)
                .title(ChatColor.DARK_GRAY +  "User List")
                .parent(this.getDisguisePlusProvider())
                .build();
    }

    public static void addReturnButton(int row, int col, InventoryContents contents) {
        ItemStack stack = new ItemBuilder(FreshMaterial.OAK_SIGN.toMaterial())
                    .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Back", contents.property("state", 1)))
                    .setLore(ChatColor.GRAY + "Click to go back")
                    .toItemStack();

        // Creating the ClickableItem
        ClickableItem item = ClickableItem.of(stack, e -> {
           contents.inventory().getParent().ifPresent(inv -> {
               inv.open((Player) e.getWhoClicked());
           });

           if (!contents.inventory().getParent().isPresent()) {
               e.getWhoClicked().closeInventory();
           }
        });

        // Adding it to the inventory
        contents.set(row, col, item);
    }

    public static void addPageButtons(int row, InventoryContents contents) {
        // Pagination
        Pagination p = contents.pagination();

        if (!p.isLast()) {
            // Get the item
            ItemStack next = new ItemBuilder(FreshMaterial.ARROW.toMaterial())
                        .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Next", contents.property("state", 1)))
                        .toItemStack();

            contents.set(row, 8, ClickableItem.of(next, e -> {
                contents.inventory().open((Player) e.getWhoClicked(), p.next().getPage());
            }));
        } else {
            contents.set(row, 8, ClickableItem.empty(new ItemStack(Material.AIR)));
        }

        if (!p.isFirst()) {
            ItemStack previous = new ItemBuilder(FreshMaterial.ARROW.toMaterial())
                    .setName(new TwoWayLoadingMask(ChatColor.AQUA, ChatColor.DARK_AQUA).getMaskedString("Previous", contents.property("state", 1)))
                    .toItemStack();

            contents.set(row, 0, ClickableItem.of(previous, e -> {
                contents.inventory().open((Player) e.getWhoClicked(), p.previous().getPage());
            }));
        } else {
            contents.set(row, 0, ClickableItem.empty(new ItemStack(Material.AIR)));
        }
    }

}
