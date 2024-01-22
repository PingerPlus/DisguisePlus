package net.pinger.disguiseplus.inventory;

import io.pnger.gui.GuiBuilder;
import io.pnger.gui.GuiInventory;
import io.pnger.gui.contents.GuiContents;
import io.pnger.gui.item.GuiItem;
import io.pnger.gui.manager.GuiManager;
import io.pnger.gui.pagination.GuiPagination;
import net.pinger.disguise.item.ItemBuilder;
import net.pinger.disguise.item.XMaterial;
import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.skin.SkinPack;
import net.pinger.disguiseplus.inventory.providers.*;
import net.pinger.disguiseplus.rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InventoryManager {

    private final DisguisePlus disguise;
    private final GuiManager inventoryManager;

    public InventoryManager(DisguisePlus disguise) {
        this.inventoryManager = new GuiManager(disguise);
        this.disguise = disguise;
    }

    public GuiInventory getDisguisePlusProvider() {
        return GuiBuilder.of()
                .provider(new DisguisePlusProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "DisguisePlus > " + this.disguise.getDescription().getVersion())
                .manager(this.inventoryManager)
                .size(5, 9)
                .build();
    }

    public GuiInventory getSkinPacksProvider() {
        return GuiBuilder.of()
                .provider(new SkinPacksProvider(this.disguise))
                .title(ChatColor.DARK_GRAY + "Skin Packs > Category")
                .manager(this.inventoryManager)
                .parent(this.getDisguisePlusProvider())
                .build();
    }

    public GuiInventory getCategoryProvider(String category) {
        return GuiBuilder.of()
                .provider(new CategoryProvider(category, this.disguise))
                .title(ChatColor.DARK_GRAY + "Skin Packs > " + category)
                .parent(this.getSkinPacksProvider())
                .manager(this.inventoryManager)
                .build();
    }

    public GuiInventory getExactPackProvider(SkinPack pack) {
        return GuiBuilder.of()
                .provider(new ExactPackProvider(pack, this.disguise))
                .title(ChatColor.DARK_GRAY + String.format("%s > %s", pack.getCategory(), pack.getName()))
                .parent(this.getCategoryProvider(pack.getCategory()))
                .manager(this.inventoryManager)
                .build();
    }

    public GuiInventory getAddSkinProvider(SkinPack pack) {
        return GuiBuilder.of()
                .provider(new AddSkinProvider(this.disguise, pack))
                .manager(this.inventoryManager)
                .title(String.format(ChatColor.DARK_GRAY + "%s > Add Skin", pack.getName()))
                .size(4, 9)
                .parent(this.getExactPackProvider(pack))
                .build();
    }

    public GuiInventory getRankInventory(List<Rank> ranks) {
        return GuiBuilder.of()
                .provider(new RankChooseProvider(this.disguise, ranks))
                .manager(this.inventoryManager)
                .title(ChatColor.DARK_GRAY + "Disguise > Choose Rank")
                .build();
    }

    public static void addPageButtons(int row, GuiContents contents) {
        GuiPagination p = contents.getPagination();

        // If it isn't last page we need next item
        if (!p.isLast()) {
            // Get the item
            ItemStack next = new ItemBuilder(XMaterial.ARROW)
                    .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Next Page")
                    .build();

            contents.setItem(row, 8, GuiItem.of(next, e -> {
                contents.getInventory().open((Player) e.getWhoClicked(), p.next().getPage());
            }));
        } else {
            contents.setItem(row, 8, GuiItem.of(new ItemStack(Material.AIR)));
        }

        // If it isn't first page we need back item
        if (!p.isFirst()) {
            ItemStack previous = new ItemBuilder(Material.ARROW)
                    .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Previous Page")
                    .build();

            contents.setItem(row, 0, GuiItem.of(previous, e -> {
                contents.getInventory().open((Player) e.getWhoClicked(), p.previous().getPage());
            }));
        } else {
            contents.setItem(row, 0, GuiItem.of(new ItemStack(Material.AIR)));
        }
    }

    public static void addReturnButton(int row, int col, GuiContents contents) {
        ItemStack stack = new ItemBuilder(XMaterial.OAK_SIGN)
                .name(ChatColor.AQUA + ChatColor.BOLD.toString() + "Back")
                .lore(ChatColor.GRAY + "Click to go back")
                .build();

        // Creating the ClickableItem
        GuiItem item = GuiItem.of(stack, e -> {
            if (e.isRightClick() || e.isShiftClick())
                return;

            if (e.getClick() == ClickType.NUMBER_KEY) {
                return;
            }

            GuiInventory inv = contents.getInventory().getParent().orElse(null);

            // If inventory is close it
            // Otherwise open it
            if (inv == null) {
                e.getWhoClicked().closeInventory();
                return;
            }

            inv.open((Player) e.getWhoClicked());
        });

        // Adding it to the inventory
        contents.setItem(row, col, item);
    }

}
