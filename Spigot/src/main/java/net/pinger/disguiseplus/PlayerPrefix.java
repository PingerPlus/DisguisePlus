package net.pinger.disguiseplus;

import me.clip.placeholderapi.PlaceholderAPI;
import net.pinger.disguiseplus.user.User;
import org.bukkit.configuration.ConfigurationSection;

public class PlayerPrefix implements Prefixable<User> {

    private final String prefixDefault;
    private final String prefixDisguised;

    public PlayerPrefix(String prefixDefault, String prefixDisguised) {
        this.prefixDefault = prefixDefault;
        this.prefixDisguised = prefixDisguised;
    }

    public PlayerPrefix(ConfigurationSection section) {
        // Get both the default and disguised value
        this(section.getString("default"), section.getString("disguised"));
    }

    public String getPrefixDefault() {
        return prefixDefault;
    }

    public String getPrefixDisguised() {
        return prefixDisguised;
    }

    @Override
    public String toPrefix(User user) {
        if (user.isDisguised()) {
            // Get the disguised prefix
            return PlaceholderAPI.setPlaceholders(user.transform(), getPrefixDisguised());
        }

        // Otherwise, return the default prefix
        return PlaceholderAPI.setPlaceholders(user.transform(), getPrefixDefault());
    }
}
