package net.pinger.disguise.inventory.providers;

import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import net.pinger.disguise.DisguisePlus;
import net.pinger.disguise.inventory.SimpleInventoryManager;
import net.pinger.disguise.nick.NickCharacter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CreatorProvider implements InventoryProvider {

    private final DisguisePlus dp;

    public CreatorProvider(DisguisePlus dp) {
        this.dp = dp;
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        for (NickCharacter character : this.dp.getSettings().getCreator().getFlags()) {
            Bukkit.broadcastMessage(character.getFlag().name() + " " + character.isOptional());
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        int refresh = contents.property("refresh", 0);
        contents.setProperty("refresh", refresh + 1);

        if (refresh % 2 != 0)
            return;

        int state = contents.property("state", 0);
        contents.setProperty("state", state + 1);



        SimpleInventoryManager.addReturnButton(5, 4, contents);
    }
}
