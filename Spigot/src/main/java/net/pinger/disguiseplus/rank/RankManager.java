package net.pinger.disguiseplus.rank;

import net.pinger.disguiseplus.DisguisePlus;
import net.pinger.disguiseplus.configuration.ExternalConfigurationAdapter;
import net.pinger.disguiseplus.user.DisguiseUser;
import net.pinger.disguiseplus.utils.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RankManager extends ExternalConfigurationAdapter {
    private final List<Rank> ranks = new ArrayList<>();
    private final String permission;
    private final boolean enabled;

    public RankManager(DisguisePlus disguise) {
        super(disguise, "ranks.yml");

        // Default settings
        this.permission = this.configuration.getString("defaultPermission", "");
        this.enabled = this.configuration.getBoolean("enabled", false);

        // Loop through each rank
        // And create a default for each
        final ConfigurationSection section = this.configuration.getConfigurationSection("ranks");
        for (final Map.Entry<String, Object> value : section.getValues(false).entrySet()) {
            final String name = value.getKey();
            final ConfigurationSection rank = (ConfigurationSection) value.getValue();
            if (!rank.contains("permission") || rank.get("permission") == null) {
                continue;
            }

            final String permission = rank.getString("permission");
            final String display = Text.translate(rank.getString("display"));
            this.ranks.add(new Rank(name, display, permission));
        }
    }

    public Rank getRank(String name) {
        for (final Rank rank : this.ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public List<Rank> getAvailableRanks(DisguiseUser user) {
        final Player player = user.transform();
        final List<Rank> available = new ArrayList<>();

        // If the player doesn't have permission to load the inventory
        // We want to return null, to say that no rank is loaded
        if (!player.hasPermission(this.permission)) {
            return null;
        }

        // We can simplify loop through all ranks in the manager
        // And see if the player has permission to use it
        // If so, we add it to the available list
        for (final Rank rank : this.ranks) {
            final String perm = rank.getPermission();
            if (perm.isEmpty() || player.hasPermission(perm)) {
                available.add(rank);
            }
        }

        return available;
    }
}
