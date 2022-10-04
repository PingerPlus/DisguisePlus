package net.pinger.disguiseplus.tab;

import org.bukkit.configuration.ConfigurationSection;

public class TabIntegration {

    private final boolean enabled;
    private final String prefix;

    public TabIntegration(boolean enabled, String prefix) {
        this.enabled = enabled;
        this.prefix = prefix;
    }


    public TabIntegration(ConfigurationSection section) {
        this(section.getBoolean("enabled"), section.getString("disguise-prefix"));
    }

    /**
     * This method returns the default prefix of this integration,
     * which will be converted to a placeholder value.
     *
     * @return the prefix
     */

    public String getPrefix() {
        return this.prefix;
    }

    /**
     * This method returns whether this integration is enabled.
     *
     * @return whether the integration is enabled
     */

    public boolean isEnabled() {
        return enabled;
    }
}
