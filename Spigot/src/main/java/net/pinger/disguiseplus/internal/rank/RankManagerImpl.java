package net.pinger.disguiseplus.internal.rank;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.configuration.ExternalConfigurationAdapter;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.rank.RankManager;
import net.pinger.disguiseplus.user.User;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankManagerImpl extends ExternalConfigurationAdapter implements RankManager {

    private final List<Rank> ranks = new ArrayList<>();
    private final String permission;
    private final boolean enabled;

    public RankManagerImpl(DisguisePlus disguise) {
        super(disguise, "ranks.yml");

        // Get whether this feature
        this.permission = this.configuration.getString("defaultPermission", "");
        this.enabled = this.configuration.getBoolean("enabled", false);

        // Loop through each rank
        // And create a default for each
        ConfigurationSection section = this.configuration.getConfigurationSection("ranks");
        for (Map.Entry<String, Object> value : section.getValues(false).entrySet()) {
            // Get the name of the rank
            String name = value.getKey();
            ConfigurationSection rank = (ConfigurationSection) value.getValue();

            // Check if the permission string exists
            if (!rank.contains("permission") || rank.get("permission") == null) {
                continue;
            }

            // Set display name and permission
            String permission = rank.getString("permission").isEmpty() ? "" : rank.getString("permission");
            String display = rank.getString("display");

            // Broadcast this now
            this.ranks.add(new Rank(name, display, permission));
        }
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public List<Rank> getAvailableRanks(User user) {
        Player player = user.transform();
        List<Rank> available = new ArrayList<>();

        // If the player doesn't have permission to load the inventory
        // We want to return null, to say that no rank is loaded
        if (!player.hasPermission(this.permission)) {
            return null;
        }

        // We can simplify loop through all ranks in the manager
        // And see if the player has permission to use it
        // If so, we add it to the available list
        for (Rank rank : this.ranks) {
            String perm = rank.getPermission();

            // If the rank permission is empty
            // Or if the player has the permission of the rank
            // We add it
            if (perm.isEmpty() || player.hasPermission(perm)) {
                available.add(rank);
            }
        }

        return available;
    }
}
