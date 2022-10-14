package net.pinger.disguiseplus.internal.rank;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.configuration.ExternalConfigurationAdapter;
import net.pinger.disguiseplus.rank.Rank;
import net.pinger.disguiseplus.rank.RankManager;
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
    public List<Rank> getAvailableRanks(Player player) {
        if (!player.hasPermission(this.permission)) {
            return null;
        }

        List<Rank> permissible = new ArrayList<>();
        for (Rank rank : this.ranks) {
            if (rank.getPermission() == null) {
                continue;
            }

            if (rank.getPermission().isEmpty()) {
                permissible.add(rank);
                continue;
            }

            if (!player.hasPermission(rank.getPermission())) {
                continue;
            }

            permissible.add(rank);
        }

        return permissible;
    }
}