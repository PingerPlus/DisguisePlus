package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.disguise.DisguisePlus;
import org.bukkit.entity.Player;

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

    }
}
