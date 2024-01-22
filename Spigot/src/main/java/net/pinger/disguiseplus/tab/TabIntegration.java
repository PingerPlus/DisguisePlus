package net.pinger.disguiseplus.tab;

import net.pinger.disguiseplus.DisguiseFeature;
import net.pinger.disguiseplus.DisguisePlus;
import org.bukkit.configuration.ConfigurationSection;

public class TabIntegration extends DisguiseFeature {

    private boolean enabled;
    private String prefix;

    public TabIntegration(DisguisePlus disguisePlus) {
        super(disguisePlus);
    }

    @Override
    protected void load() {
        ConfigurationSection section =
                this.plugin.getConfig().getConfigurationSection("tab");

        // Set the values
        this.enabled = section.getBoolean("enabled", true);
        this.prefix = section.getString("disguise-prefix");
    }

    @Override
    protected void reload() {
        this.load();
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
